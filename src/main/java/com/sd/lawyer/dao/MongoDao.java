package com.sd.lawyer.dao;

import com.sd.lawyer.beans.Page;
import com.sd.lawyer.util.PageModel;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;

/**
 * 通用基本操作方法接口
 * ClassName: MongoDao
 * Description:本类中的所有带条件查询的方法不具有完全的通用性，只能用作一般等值条件的比较<br>
 * 当有一些特殊需求时，比如大于、小于、大于等于、小于等于、like查询、 or查询等等，此时，这些<br>
 * 方法将不再满足需求，需要通过在子类dao中自定义方法实现
 * Created by zjhuang on 2018/2/6 15:35.
 */
public interface MongoDao<T> {

    /**
     * 保存实体
     * 备注：执行完成本方法后，所引用实体的主键id会自动赋上值
     *
     * @param entity 实体类
     * @return
     */
    public T save(T entity);

    List<T> batchSave(List<T> entity);

    /**
     * 更新文档信息
     *
     * @param entity 实体类
     * @return
     */
    public T update(T entity);

    /**
     * 更新文档信息
     *
     * @param id 实体ID
     * @return
     */
    public T delete(Serializable id);

    /**
     * 删除实体[数组]
     *
     * @param ids 实体ID或数组
     */
    public List<T> delete(Serializable... ids);

    /**
     * 根据主键id查询
     *
     * @param id 实体的主键ID
     * @return
     */
    public T findById(Serializable id);

    /**
     * 查询所有记录<br>
     * [不分页]
     *
     * @return 结果集合
     */
    public List<T> findAll();

    /**
     * 查询所有记录并排序<br>
     * [不分页]
     *
     * @param order 排序字段，例如：id或id asc、或id asc,name desc<br>
     *              为空则不排序，不指定排序方式则默认升序排序
     * @return 结果集合
     */
    public List<T> findAll(String order);

    /**
     * 根据单一参数查询记录<br>
     * [不分页]
     *
     * @param propName  属性名称，对应实体类字段名称
     * @param propValue 属性值
     * @return 结果列表 或 null
     */
    public List<T> findByProp(String propName, Object propValue);


    /**
     * 根据单一参数查询记录并排序<br>
     * [不分页]
     *
     * @param propName  属性名称，对应实体类字段名
     * @param propValue 属性值
     * @param order     排序字段，例如：id或id asc、或id asc,name desc<br>
     *                  为空则不排序，不指定排序方式则默认升序排序
     * @return 结果集合 或 null
     */
    public List<T> findByProp(String propName, Object propValue, String order);

    /**
     * 根据多个参数查询记录
     * [不分页]
     *
     * @param propName  参数数组
     * @param propValue 参数值数组
     * @return 结果集合 或 null
     */
    public List<T> findByProps(String[] propName, Object[] propValue);

    /**
     * 根据多个参数查询记录 并排序
     * [不分页]
     *
     * @param propName  参数数组
     * @param propValue 参数值数组
     * @param order     排序字段，例如：id或id asc、或id asc,name desc<br>
     *                  为空则不排序，不指定排序方式则默认升序排序
     * @return 结果集合 或 null
     */
    public List<T> findByProps(String[] propName, Object[] propValue, String order);

    /**
     * 根据多个参数查询第一条记录
     * [不分页]
     *
     * @param propName  参数数组
     * @param propValue 参数值数组
     * @return 结果集合 或 null
     */
    public T findFirstByProps(String[] propName, Object[] propValue);

    /**
     * 根据多个参数查询第一条记录
     * [不分页]
     *
     * @param propName  参数数组
     * @param propValue 参数值数组
     * @param order     排序字段，例如：id或id asc、或id asc,name desc<br>
     *                  为空则不排序，不指定排序方式则默认升序排序
     * @return 结果集合 或 null
     */
    public T findFirstByProps(String[] propName, Object[] propValue, String order);

    /**
     * 创建带有where条件（只支持等值）和排序的Query对象
     *
     * @param propName  参数数组
     * @param propValue 参数值数组
     * @param logic     逻辑运算符：[and]、[or]、...
     * @return BasicQuery对象
     */
    public List<T> findByPropsLogic(String[] propName, Object[] propValue, String logic);

    /**
     * 创建带有where条件（只支持等值）和排序的Query对象
     *
     * @param propName  参数数组
     * @param propValue 参数值数组
     * @param logic     逻辑运算符：[and]、[or]、...
     * @param order     排序
     * @return BasicQuery对象
     */
    public List<T> findByPropsLogic(String[] propName, Object[] propValue, String logic, String order);

    /**
     * 多条件筛选查询满足条件的结果集
     *
     * @param query 条件对象
     * @return
     */
    public List<T> findByQuery(Query query);

    /**
     * 多条件筛选查询满足条件的结果集
     *
     * @param query 条件对象
     * @param order 排序字段，例如：id或id asc、或id asc,name desc<br>
     *              为空则不排序，不指定排序方式则默认升序排序
     * @return
     */
    public List<T> findByQuery(Query query, String order);

    /**
     * 根据单一参数查询唯一结果<br>
     *
     * @param propName  属性名称，对应实体类字段名
     * @param propValue 属性值
     * @return 唯一结果 或 null
     */
    public T uniqueByProp(String propName, Object propValue);

    /**
     * 根据多个参数查询唯一结果<br>
     *
     * @param propName  参数数组
     * @param propValue 参数值数组
     * @return 唯一结果 或 null
     */
    public T uniqueByProps(String[] propName, Object[] propValue);

    /**
     * 根据条件查询总记录数
     *
     * @param params 参数数组
     * @param values 参数值数组
     * @return 总记录数
     */
    public int countByCondition(String[] params, Object[] values);

    /**
     * 去重查询满足条件的字段结果集
     *
     * @param distinctField 去重查询字段
     * @param query         条件对象
     * @return
     */
    public List distinctByQuery(String distinctField, Query query);

    /**
     * 去重查询满足条件的字段结果集
     *
     * @param distinctField 去重查询字段
     * @param query         条件对象
     * @param order         排序字段，例如：id或id asc、或id asc,name desc<br>
     *                      为空则不排序，不指定排序方式则默认升序排序
     * @return
     */
    public List distinctByQuery(String distinctField, Query query, String order);

    /**
     * 分页查询所有结果集
     *
     * @param page 分页对象
     * @return
     */
    public Page<T> pageAll(Page page);

    /**
     * 分页查询所有结果集
     *
     * @param page  分页对象
     * @param order 排序字段，例如：id或id asc、或id asc,name desc<br>
     *              为空则不排序，不指定排序方式则默认升序排序
     * @return
     */
    public Page<T> pageAll(Page page, String order);

    /**
     * 分页查询满足条件的结果集
     *
     * @param page  分页对象
     * @param query 条件对象
     * @return
     */
    public Page<T> pageByQuery(Page page, Query query);

    /**
     * 分页查询满足条件的结果集
     *
     * @param page  分页对象
     * @param query 条件对象
     * @param order 排序字段，例如：id或id asc、或id asc,name desc<br>
     *              为空则不排序，不指定排序方式则默认升序排序
     * @return
     */
    public Page<T> pageByQuery(Page page, Query query, String order);

    /**
     * 分页查询所有结果集合<br>
     * [分页]
     *
     * @param pageNo   当前页码
     * @param pageSize 页容量
     * @return 分页模型对象（不会为null）
     */
    public PageModel<T> pageAll(int pageNo, int pageSize);

    /**
     * 分页查询所有结果集合 并排序<br>
     * [分页]
     *
     * @param pageNo   当前页码
     * @param pageSize 页容量
     * @param order    排序字段，例如：id或id asc、或id asc,name desc<br>
     *                 为空则不排序，不指定排序方式则默认升序排序
     * @return 分页模型对象（不会为null）
     */
    public PageModel<T> pageAll(int pageNo, int pageSize, String order);

    /**
     * 根据参数分页查询结果集合<br>
     * [分页]
     *
     * @param pageNo   当前页码
     * @param pageSize 页容量
     * @param param    参数
     * @param value    参数值
     * @return 分页模型对象（不会为null）
     */
    public PageModel<T> pageByProp(int pageNo, int pageSize, String param, Object value);

    /**
     * 根据参数分页查询结果集合并排序<br>
     * [分页]
     *
     * @param pageNo   当前页码
     * @param pageSize 页容量
     * @param param    参数
     * @param value    参数值
     * @param order    排序字段，例如：id或id asc、或id asc,name desc<br>
     *                 为空则不排序，不指定排序方式则默认升序排序
     * @return 分页模型对象（不会为null）
     */
    public PageModel<T> pageByProp(int pageNo, int pageSize, String param, Object value, String order);

    /**
     * 根据参数分页查询结果集合<br>
     * [分页]
     *
     * @param pageNo   当前页码
     * @param pageSize 页容量
     * @param params   参数数组
     * @param values   参数值数组
     * @return 分页模型对象（不会为null）
     */
    public PageModel<T> pageByProps(int pageNo, int pageSize, String[] params, Object[] values);

    /**
     * 根据参数分页查询结果集合 并排序<br>
     * [分页]
     *
     * @param pageNo   当前页码
     * @param pageSize 页容量
     * @param params   参数数组
     * @param values   参数值数组
     * @param order    排序字段，例如：id或id asc、或id asc,name desc<br>
     *                 为空则不排序，不指定排序方式则默认升序排序
     * @return 分页模型对象（不会为null）
     */
    public PageModel<T> pageByProps(int pageNo, int pageSize, String[] params, Object[] values, String order);


}


