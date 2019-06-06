package com.sd.lawyer.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.sd.lawyer.beans.Page;
import com.sd.lawyer.util.EmptyUtils;
import com.sd.lawyer.util.PageModel;
import com.sd.lawyer.util.ReflectionUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * 基本操作接口MongoDB数据库实现类
 * ClassName: MongoDaoImpl
 * Description:本实现类适用于MongoDB数据库，以下代码仅供参考，本人水平有限，可能会存在些许问题（如有更好方案可告知我，一定虚心学习），
 * 再次提醒，仅供参考！！
 * Created by zjhuang on 2018/2/6 15:35.
 */
public class MongoDaoImpl<T> implements MongoDao<T> {

//    protected abstract Class<T> getEntityClass();

    /**
     * 获得泛型类T
     */
    protected Class<T> getEntityClass() {
        return ReflectionUtils.getSuperClassGenricType(getClass());
    }

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public T save(T entity) {
        mongoTemplate.save(entity);
        return entity;
    }

    @Override
    public List<T> batchSave(List<T> entity) {
        mongoTemplate.insertAll(entity);
        return entity;
    }

    @Override
    public T update(T entity) {

        // 反向解析对象
        Map<String, Object> map = null;
        try {
            map = parseEntity(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ID字段
        String idName = null;
        Object idValue = null;

        // 生成参数
        Update update = new Update();
        if (EmptyUtils.isNotEmpty(map)) {
            for (String key : map.keySet()) {
                if (key.indexOf("{") != -1) {
                    // 设置ID
                    idName = key.substring(key.indexOf("{") + 1, key.indexOf("}"));
                    idValue = map.get(key);
                    idName = "_id";
                    idValue = new ObjectId(idValue.toString());
                } else {
                    update.set(key, map.get(key));
                }
            }
        }
        mongoTemplate.updateFirst(new Query().addCriteria(where(idName).is(idValue)), update, getEntityClass());
        return entity;
    }

    @Override
    public T delete(Serializable id) {
        return mongoTemplate.findAndRemove(createIdQuery(id), getEntityClass());
    }

    @Override
    public List<T> delete(Serializable... ids) {
        if (EmptyUtils.isNotEmpty(ids)) {
            List<T> list = new ArrayList<T>();
            for (Serializable id : ids) {
                list.add(mongoTemplate.findAndRemove(createIdQuery(id), getEntityClass()));
//                mongoTemplate.remove(createIdQuery(id), getEntityClass());
//                mongoTemplate.remove(mongoTemplate.findById(id, getEntityClass()));
            }
            return list;
        }
        return null;
    }

    @Override
    public T findById(Serializable id) {
        ObjectId objectId = new ObjectId(id.toString());
        Query query = new Query();
        query.addCriteria(where("_id").is(objectId));
        return mongoTemplate.findOne(query, getEntityClass());
//        return mongoTemplate.findById(objectId, this.getEntityClass());
    }

    @Override
    public List<T> findAll() {
        return mongoTemplate.findAll(getEntityClass());
    }

    @Override
    public List<T> findAll(String order) {
        List<Order> orderList = parseOrder(order);
        if (EmptyUtils.isEmpty(orderList)) {
            return findAll();
        }
        return mongoTemplate.find(new Query().with(new Sort(orderList)), getEntityClass());
    }

    @Override
    public List<T> findByProp(String propName, Object propValue) {
        return findByProp(propName, propValue, null);
    }

    @Override
    public List<T> findByProp(String propName, Object propValue, String order) {
        Query query = new Query();
        // 参数
        query.addCriteria(where(propName).is(propValue));
        // 排序
        List<Order> orderList = parseOrder(order);
        if (EmptyUtils.isNotEmpty(orderList)) {
            query.with(new Sort(orderList));
        }
        return mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public List<T> findByProps(String[] propName, Object[] propValue) {
        return findByProps(propName, propValue, null);
    }

    @Override
    public List<T> findByProps(String[] propName, Object[] propValue, String order) {
        Query query = createQuery(propName, propValue, order);
        return mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public T findFirstByProps(String[] propName, Object[] propValue) {
        return findFirstByProps(propName, propValue, null);
    }

    @Override
    public T findFirstByProps(String[] propName, Object[] propValue, String order) {
        Query query = createQuery(propName, propValue, order);
        List<T> list = mongoTemplate.find(query, getEntityClass());
        if (EmptyUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<T> findByPropsLogic(String[] propName, Object[] propValue, String logic) {
        return findByPropsLogic(propName, propValue, logic, null);
    }

    @Override
    public List<T> findByPropsLogic(String[] propName, Object[] propValue, String logic, String order) {
        Query query = createQuery(propName, propValue, logic, order);
        return mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public List<T> findByQuery(Query query) {
        return findByQuery(query, null);
    }

    @Override
    public List<T> findByQuery(Query query, String order) {
        // 排序
        List<Order> orderList = parseOrder(order);
        if (EmptyUtils.isNotEmpty(orderList)) {
            query.with(new Sort(orderList));
        }
        //执行查询
        List<T> list = mongoTemplate.find(query, getEntityClass());
        return list;
    }

    @Override
    public T uniqueByProp(String propName, Object propValue) {
        return mongoTemplate.findOne(new Query(where(propName).is(propValue)), getEntityClass());
    }

    @Override
    public T uniqueByProps(String[] propName, Object[] propValue) {
        Query query = createQuery(propName, propValue, null);
        return mongoTemplate.findOne(query, getEntityClass());
    }

    @Override
    public List distinctByQuery(String distinctField, Query query) {
        return distinctByQuery(distinctField, query, null);
    }

    @Override
    public List distinctByQuery(String distinctField, Query query, String order) {
        // 排序
        List<Order> orderList = parseOrder(order);
        if (EmptyUtils.isNotEmpty(orderList)) {
            query.with(new Sort(orderList));
        }
        // 获取集合名称
        String collectionName = this.mongoTemplate.getCollectionName(getEntityClass());
        // distinct查询
        return this.mongoTemplate.getCollection(collectionName).distinct(distinctField, query.getQueryObject());
    }

    @Override
    public Page<T> pageAll(Page page) {
        return pageByQuery(page, new Query());
    }

    @Override
    public Page<T> pageAll(Page page, String order) {
        // 条件
        Query query = new Query();
        // 排序
        List<Order> orderList = parseOrder(order);
        if (EmptyUtils.isNotEmpty(orderList)) {
            query.with(new Sort(orderList));
        }
        return pageByQuery(page, query);
    }

    @Override
    public Page<T> pageByQuery(Page page, Query query) {
        return pageByQuery(page, query, null);
    }

    @Override
    public Page<T> pageByQuery(Page page, Query query, String order) {
        if (page.isInit() && page.getInitPage() == -1) {
            // 设置数据总数、总页数
            page.setTotalCount(mongoTemplate.count(query, getEntityClass()));
            page.setPage((int) page.getTotalPage());
        }
        // 排序
        List<Order> orderList = parseOrder(order);
        if (EmptyUtils.isNotEmpty(orderList)) {
            query.with(new Sort(orderList));
        }
        // 设置数据总数、总页数
        page.setTotalCount(mongoTemplate.count(query, getEntityClass()));
        // skip
        query.skip(page.getShowCount() * (page.getPage() - 1));
        // limit
        query.limit(page.getShowCount());
        // 执行查询
        List<T> list = mongoTemplate.find(query, getEntityClass());
        // 保存查询结果
        page.setRows(list);
        return page;
    }

    @Override
    public PageModel<T> pageAll(int pageNo, int pageSize) {
        return pageAll(pageNo, pageSize, null);
    }

    @Override
    public PageModel<T> pageAll(int pageNo, int pageSize, String order) {
        return pageByProp(pageNo, pageSize, null, null, order);
    }

    @Override
    public PageModel<T> pageByProp(int pageNo, int pageSize, String param, Object value) {
        return pageByProp(pageNo, pageSize, param, value, null);
    }

    @Override
    public PageModel<T> pageByProp(int pageNo, int pageSize, String param, Object value, String order) {
        String[] params = null;
        Object[] values = null;
        if (EmptyUtils.isNotEmpty(param)) {
            params = new String[]{param};
            values = new Object[]{value};
        }
        return pageByProps(pageNo, pageSize, params, values, order);
    }

    @Override
    public PageModel<T> pageByProps(int pageNo, int pageSize, String[] params, Object[] values) {
        return pageByProps(pageNo, pageSize, params, values, null);
    }

    @Override
    public PageModel<T> pageByProps(int pageNo, int pageSize, String[] params, Object[] values, String order) {
        // 创建分页模型对象
        PageModel<T> page = new PageModel<>(pageNo, pageSize);

        // 查询总记录数
        int count = countByCondition(params, values);
        page.setTotalCount(count);

        // 查询数据列表
        Query query = createQuery(params, values, order);

        // 设置分页信息
        query.skip(page.getFirstResult());
        query.limit(page.getPageSize());

        // 封装结果数据
        page.setList(mongoTemplate.find(query, getEntityClass()));

        return page;
    }

    @Override
    public int countByCondition(String[] params, Object[] values) {
        Query query = createQuery(params, values, null);
        Long count = mongoTemplate.count(query, getEntityClass());
        return count.intValue();
    }

    /**
     * 创建用于使用_id进行过滤的Query对象
     *
     * @param _id
     * @return
     */
    public Query createIdQuery(Serializable _id) {
        ObjectId objectId = new ObjectId(_id.toString());
        Query query = new Query();
        query.addCriteria(where("_id").is(objectId));
        return query;
    }

    /**
     * 创建带有where条件（只支持等值）和排序的Query对象
     *
     * @param params 参数数组
     * @param values 参数值数组
     * @param order  排序
     * @return Query对象
     */
    protected Query createQuery(String[] params, Object[] values, String order) {
        Query query = new Query();

        // where 条件
        if (EmptyUtils.isNotEmpty(params) && EmptyUtils.isNotEmpty(values)) {
            for (int i = 0; i < params.length; i++) {
                query.addCriteria(where(params[i]).is(values[i]));
            }
        }

        // 排序
        List<Order> orderList = parseOrder(order);
        if (EmptyUtils.isNotEmpty(orderList)) {
            query.with(new Sort(orderList));
        }

        return query;
    }

    /**
     * 创建带有where条件（只支持等值）和排序的Query对象
     *
     * @param params 参数数组
     * @param values 参数值数组
     * @param logic  逻辑运算符：[and]、[or]、[...]
     * @param order  排序
     * @return BasicQuery对象
     */
    protected BasicQuery createQuery(String[] params, Object[] values, String logic, String order) {
        BasicDBList basicDBList = new BasicDBList();
        // where 条件
        if (EmptyUtils.isNotEmpty(params) && EmptyUtils.isNotEmpty(values)) {
            for (int i = 0; i < params.length; i++) {
                basicDBList.add(new BasicDBObject(params[i], values[i]));
            }
        }
        BasicQuery query = new BasicQuery(new BasicDBObject("$" + logic, basicDBList));

        // 排序
        List<Order> orderList = parseOrder(order);
        if (EmptyUtils.isNotEmpty(orderList)) {
            query.with(new Sort(orderList));
        }

        return query;
    }

    /**
     * 解析Order字符串为所需参数
     *
     * @param order 排序参数，如[id]、[id asc]、[id asc,name desc]
     * @return Order对象集合
     */
    protected List<Order> parseOrder(String order) {
        List<Order> list = null;
        if (EmptyUtils.isNotEmpty(order)) {
            list = new ArrayList<Order>();
            // 共有几组排序字段
            String[] fields = order.split(",");
            Order o = null;
            String[] item = null;
            for (int i = 0; i < fields.length; i++) {
                if (EmptyUtils.isEmpty(fields[i])) {
                    continue;
                }
                item = fields[i].split(" ");
                if (item.length == 1) {
                    o = new Order(Direction.ASC, item[0]);
                } else if (item.length == 2) {
                    o = new Order("desc".equalsIgnoreCase(item[1]) ? Direction.DESC : Direction.ASC, item[0]);
                } else {
                    throw new RuntimeException("排序字段参数解析出错");
                }
                list.add(o);
            }
        }
        return list;
    }

    /**
     * 将对象的字段及值反射解析为Map对象<br>
     * 这里使用Java反射机制手动解析，并且可以识别注解为主键的字段，以达到根据id进行更新实体的目的<br>
     * key：字段名称，value：字段对应的值
     *
     * @param t 要修改的对象
     * @return Map对象，注意：id字段的key封装为“{id字段名称}”，以供后续识别
     * @throws Exception
     */
    protected Map<String, Object> parseEntity(T t) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        /*
         * 解析ID
         */
        String idName = "";
        Field[] declaredFields = getEntityClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                map.put("{" + field.getName() + "}", field.get(t));
                idName = field.getName();
                break;
            }
        }

        /*
         * 解析其他属性
         */
        Method[] methods = getEntityClass().getDeclaredMethods();
        if (EmptyUtils.isNotEmpty(methods)) {
            for (Method method : methods) {
                if ((method.getName().startsWith("get") && method.getModifiers() == Modifier.PUBLIC)
                        || (method.getName().startsWith("is") && method.getModifiers() == Modifier.PUBLIC)) {
                    String fieldName = parse2FieldName(method.getName());
                    if ((!fieldName.equals(idName))
                            && (!getEntityClass().getDeclaredField(fieldName).isAnnotationPresent(Transient.class))) {
                        map.put(fieldName, method.invoke(t));
                    }
                }
            }
        }

        return map;
    }

    /**
     * 将get或is方法名转换为对应的字段名称
     *
     * @param methodName 如：getName
     * @return 如：name
     */
    private String parse2FieldName(String methodName) {
        String name = null;
        if (methodName.startsWith("is")) {
            name = methodName.replace("is", "");
        } else if (methodName.startsWith("get")) {
            name = methodName.replace("get", "");
        }
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        return name;
    }

    /**
     * 获取GridFS对象
     *
     * @return gridFS
     */
    private GridFS getGridFS() {
        DB db = this.mongoTemplate.getDb();
        GridFS gridFS = new GridFS(db);
        return gridFS;
    }

    /**
     * 保存进mongo
     *
     * @param path     文件路径
     * @param fileName 文件名
     */
    public Boolean saveFileToMongo(String path, String fileName) {
        Boolean flag = true;
        try {
            File file = new File(path);
            GridFS gridFS = getGridFS();
            GridFSInputFile gridFSFile = gridFS.createFile(file);
            gridFSFile.setFilename(fileName);
            gridFSFile.save();
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    /**
     * 根据文件名从mongo中获取文件
     *
     * @param fileName 文件名
     * @return InputStream流
     */
    public GridFSDBFile getFileFromMongo(String fileName) {
        GridFS gridFS = getGridFS();
        GridFSDBFile gridFSDBFile = gridFS.findOne(fileName);
        if (gridFSDBFile == null) {
            return null;
        }
        return gridFSDBFile;
    }

}
