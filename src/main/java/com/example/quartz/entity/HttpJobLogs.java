package com.example.quartz.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

public class HttpJobLogs implements Serializable {

    private static final long serialVersionUID = 2259203435361774854L;

    private Integer id;

    private String jobName;

    private String jobGroup;

    private String requestType;

    private String httpUrl;

    private String httpParams;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date fireTime;

    private String result;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHttpParams() {
        return httpParams;
    }

    public void setHttpParams(String httpParams) {
        this.httpParams = httpParams;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "HttpJobLogs{" +
                "id=" + id +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", requestType='" + requestType + '\'' +
                ", httpUrl='" + httpUrl + '\'' +
                ", httpParams='" + httpParams + '\'' +
                ", fireTime=" + fireTime +
                ", result='" + result + '\'' +
                '}';
    }
}
