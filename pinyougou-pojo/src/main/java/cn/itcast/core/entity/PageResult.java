package cn.itcast.core.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页结果
 *
 * @param <T>
 */
public class PageResult<T> implements Serializable {
    //结果集
    private List<T> rows;
    //总条数
    private Long total;

    public PageResult(List<T> rows, Long total) {
        this.rows = rows;
        this.total = total;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }


}
