package com.sd.lawyer.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 分页响应对象
 *
 * @author 肖文杰 https://github.com/xwjie/
 */
public class Page<T> {
    /**
     * 初始化状态
     */
    private boolean init = true;
    /**
     * 分页初始化时默认显示第几页
     */
    private int initPage = 1;
    /**
     * 数据列表
     */
    private List<T> rows;
    /**
     * 当前页
     */
    private int page = initPage;
    /**
     * 每页显示记录数
     */
    private int showCount = 10;
    /**
     * 总页数
     */
    private long totalPage;
    /**
     * 数据总数
     */
    private long totalCount;
    /**
     * 分页组件代码
     */
    private String pageContent;
    /**
     * 排序字段
     */
    private String sortField = "";
    /**
     * 排序逻辑：增序、降序
     */
    private String sort = "";

    public Page() {
    }

    public Page(int page, int totalPage) {
        this.page = page;
        this.totalPage = totalPage;
    }

    public Page(org.springframework.data.domain.Page<T> page) {
        this.rows = page.getContent();
        this.page = page.getNumber() + 1;
        this.totalPage = page.getSize();
        this.totalCount = page.getTotalElements();
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        int remainder = this.totalCount % this.showCount > 0 ? 1 : 0;
        this.totalPage = this.totalCount / this.showCount + remainder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public int getInitPage() {
        return initPage;
    }

    public void setInitPage(int initPage) {
        this.initPage = initPage;
        if (init) {
            page = initPage;
        }
    }

    /**
     * 获取分页组件的页面代码
     *
     * @return
     */
    public String getPageContent() {
        StringBuffer sb = new StringBuffer();
        if (this.totalCount > 0) {
            if (this.totalPage == 1) {
                sb.append("<ul class=\"pagination pull-right\" >");
                sb.append("<li class=\"footable-page-arrow disabled\"><a>共<span class=\"text-danger\">" + this.totalCount + "</span>条</a></li>");
                sb.append("<li class=\"footable-page-arrow disabled\"><a data-page=\"first\">首页</a></li>");
                sb.append("<li class=\"footable-page-arrow disabled\"><a data-page=\"prev\">上一页</a></li>");
                sb.append(getNearPage(this.page, this.totalPage, 5));
                sb.append("<li class=\"footable-page-arrow disabled\"><a data-page=\"next\">下一页</a> </li>");
                sb.append("<li class=\"footable-page-arrow disabled\"><a data-page=\"last\">尾页</a> </li>");
                sb.append("<li class=\"footable-page-arrow disabled\"><a>共<span class=\"text-danger\">" + this.totalPage + "</span>页</a></li>");
                sb.append("</ul>");
            } else if (this.page == 1) {
                sb.append("<ul class=\"pagination pull-right\" >");
                sb.append("<li class=\"footable-page-arrow disabled\"><a>共<span class=\"text-danger\">" + this.totalCount + "</span>条</a></li>");
                sb.append("<li class=\"footable-page-arrow disabled\"><a data-page=\"first\">首页</a></li>");
                sb.append("<li class=\"footable-page-arrow disabled\"><a data-page=\"prev\">上一页</a></li>");
                sb.append(getNearPage(this.page, this.totalPage, 5));
                sb.append("<li class=\"footable-page-arrow\"><a data-page=\"next\" onclick=\"gotoPage(" + (this.page + 1) + ")\">下一页</a> </li>");
                sb.append("<li class=\"footable-page-arrow\"><a data-page=\"last\" onclick=\"gotoPage(" + this.totalPage + ")\">尾页</a> </li>");
                sb.append("<li class=\"footable-page-arrow disabled\"><a>共<span class=\"text-danger\">" + this.totalPage + "</span>页</a></li>");
                sb.append("</ul>");
            } else if (this.page == this.totalPage) {
                sb.append("<ul class=\"pagination pull-right\" >");
                sb.append("<li class=\"footable-page-arrow disabled\"><a>共<span class=\"text-danger\">" + this.totalCount + "</span>条</a></li>");
                sb.append("<li class=\"footable-page-arrow\"><a data-page=\"first\" onclick=\"gotoPage(1)\">首页</a></li>");
                sb.append("<li class=\"footable-page-arrow\"><a data-page=\"prev\" onclick=\"gotoPage(" + (this.page - 1) + ")\">上一页</a></li>");
                sb.append(getNearPage(this.page, this.totalPage, 5));
                sb.append("<li class=\"footable-page-arrow disabled\"><a data-page=\"next\" >下一页</a> </li>");
                sb.append("<li class=\"footable-page-arrow disabled\"><a data-page=\"last\" >尾页</a> </li>");
                sb.append("<li class=\"footable-page-arrow disabled\"><a>共<span class=\"text-danger\">" + this.totalPage + "</span>页</a></li>");
                sb.append("</ul>");
            } else {
                sb.append("<ul class=\"pagination pull-right\" >");
                sb.append("<li class=\"footable-page-arrow disabled\"><a>共<span class=\"text-danger\">" + this.totalCount + "</span>条</a></li>");
                sb.append("<li class=\"footable-page-arrow\"><a data-page=\"first\" onclick=\"gotoPage(1)\">首页</a></li>");
                sb.append("<li class=\"footable-page-arrow\"><a data-page=\"prev\" onclick=\"gotoPage(" + (this.page - 1) + ")\">上一页</a></li>");
                sb.append(getNearPage(this.page, this.totalPage, 5));
                sb.append("<li class=\"footable-page-arrow\"><a data-page=\"next\" onclick=\"gotoPage(" + (this.page + 1) + ")\">下一页</a> </li>");
                sb.append("<li class=\"footable-page-arrow\"><a data-page=\"last\"  onclick=\"gotoPage(" + this.totalPage + ")\">尾页</a> </li>");
                sb.append("<li class=\"footable-page-arrow disabled\"><a>共<span class=\"text-danger\">" + this.totalPage + "</span>页</a></li>");
                sb.append("</ul>");
            }
        }
        // 实现跳转页面方法
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("function gotoPage(pageNum) {\n");
        sb.append("var form = document.forms[0];\n");
        sb.append("if(!form) {\n");
        sb.append("alert(\"分页插件缺少相应的form元素\");\n");
        sb.append("}\n");
        sb.append("var url = form.getAttribute(\"action\");\n");
        sb.append("if(url.indexOf(\"?\") != -1) {url += \"&\";}\n");
        sb.append("else {url += \"?\";}\n");
        sb.append("url += \"page=\" + pageNum + \"&showCount=" + this.showCount + "&init=false\"\n");
        sb.append("form.action = url;\n");
        sb.append("console.log(url);\n");
        sb.append("form.submit();\n");
        sb.append("}\n");
        sb.append("</script>");
        this.pageContent = sb.toString();
        return this.pageContent;
    }

    /**
     * 获取页码
     *
     * @param currentPage 当前页
     * @param maxPage     最大页
     * @param pageNum     显示多少页
     * @return
     */
    public String getNearPage(int currentPage, long maxPage, int pageNum) {
        StringBuffer sb = new StringBuffer();
        Integer[] nearPages = getNearPageNum(currentPage, maxPage, pageNum);
        for (int p : nearPages) {
            if (currentPage == p) {
                sb.append("<li class=\"footable-page active\"><a data-page=\"" + p + "\">" + p + "</a></li>");
            } else {
                sb.append("<li class=\"footable-page\"><a data-page=\"" + p + "\" onclick=\"gotoPage(" + p + ")\">" + p + "</a></li>");
            }
        }
        return sb.toString();
    }

    /**
     * 获取当前页最近的pageNum个页码
     *
     * @param currentPage 当前页码
     * @param maxPage     最大页码
     * @param pageNum     显示多少页
     * @return
     */
    public Integer[] getNearPageNum(int currentPage, long maxPage, int pageNum) {
        List<Integer> numberList = new ArrayList<>();
        if (currentPage == 1) {
            while (pageNum > 0 && currentPage <= maxPage) {
                numberList.add(currentPage);
                currentPage++;
                pageNum--;
            }
        } else if (currentPage == maxPage) {
            while (pageNum > 0 && currentPage >= 1) {
                numberList.add(currentPage);
                currentPage--;
                pageNum--;
            }
        } else {
            numberList.add(currentPage);
            pageNum--;
            int i = 0;
            while (pageNum > 0) {
                i++;
                int nextPage = currentPage + i;
                int lastPage = currentPage - i;
                if (nextPage > maxPage && lastPage < 1) {
                    break;
                }
                if (nextPage <= maxPage && pageNum > 0) {
                    numberList.add(nextPage);
                    pageNum--;
                }
                if (lastPage >= 1 && pageNum > 0) {
                    numberList.add(lastPage);
                    pageNum--;
                }
            }
        }
        Integer[] numbers = numberList.toArray(new Integer[numberList.size()]);
        Arrays.sort(numbers);
        return numbers;
    }

    @Override
    public String toString() {
        return "Page{" +
                "rows=" + rows +
                ", page=" + page +
                ", showCount=" + showCount +
                ", totalPage=" + totalPage +
                ", totalCount=" + totalCount +
                ", pageContent='" + pageContent + '\'' +
                '}';
    }
}
