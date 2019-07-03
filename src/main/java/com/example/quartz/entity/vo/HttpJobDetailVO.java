package com.example.quartz.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class HttpJobDetailVO {

    private String jobName;

    private String jobGroup;

    private String description;

    private String jobStatusInfo;

    private String requestType;

    private String httpUrl;

    private String httpParams;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date nextFireTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String cronExpression;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobStatusInfo() {
        return jobStatusInfo;
    }

    public void setJobStatusInfo(String jobStatusInfo) {
        this.jobStatusInfo = jobStatusInfo;
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

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
