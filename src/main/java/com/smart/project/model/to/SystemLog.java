package com.smart.project.model.to;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "t_log")
public class SystemLog implements Serializable {

    @Id
//    @KeySql
    private String id;

    private String description;

    private String method;

    private Long logType;

    private String requestIp;

    private String exceptioncode;

    private String exceptionDetail;

    private String params;

    private String creater;

    private Long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getLogType() {
        return logType;
    }

    public void setLogType(Long logType) {
        this.logType = logType;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getExceptioncode() {
        return exceptioncode;
    }

    public void setExceptioncode(String exceptioncode) {
        this.exceptioncode = exceptioncode;
    }

    public String getExceptionDetail() {
        return exceptionDetail;
    }

    public void setExceptionDetail(String exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long timeStamp) {
        this.time = timeStamp;
    }
}
