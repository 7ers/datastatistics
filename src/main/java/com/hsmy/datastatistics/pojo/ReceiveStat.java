package com.hsmy.datastatistics.pojo;

import java.util.Date;

public class ReceiveStat {
    private Integer id;

    private Date statDate;

    private Long statCount;

    private Date createtime;

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    private Long duration;

    public Long getIdfaCount() {
        return idfaCount;
    }

    public void setIdfaCount(Long idfaCount) {
        this.idfaCount = idfaCount;
    }

    private Long idfaCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Long getStatCount() {
        return statCount;
    }

    public void setStatCount(Long statCount) {
        this.statCount = statCount;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}