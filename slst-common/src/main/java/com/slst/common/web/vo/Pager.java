package com.slst.common.web.vo;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
public class Pager implements Serializable {
    private List data;
    private Integer pageSize = 10;
    private Integer pageNum;
    private Integer pages;
    private Integer total;
    private List content;

    public Pager(Integer total , List data) {
       this(total , data , null);
    }
    public Pager(Integer total , List data , Integer pageSize) {
        this.total = total;
        this.data = data;
        this.content=data;
        if(pageSize != null && pageSize > 0){
            this.pageSize = pageSize;
        }
        if(total == null || total < 1 || CollectionUtils.isEmpty(data)){
            this.data = Lists.newArrayList();
    }
    }

    public Pager() {
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public List getContent(){return content;}

    public void setContent(List content){this.content = content;}

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPages() {
        if(this.pageSize == null || this.pageSize < 1){
            this.pageSize = 10;
        }

        this.pages = this.total / this.pageSize;

        if(this.total % this.pageSize != 0){

            this.pages = this.pages + 1;
        }
        return this.pages;
    }


    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;

    }

    @Override
    public String toString() {
        return "Pager{" +
                ", pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", pages=" + pages +
                ", total=" + total +
                '}';
    }
}
