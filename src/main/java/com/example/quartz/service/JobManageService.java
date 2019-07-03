package com.example.quartz.service;

public interface JobManageService {

    /**
     * 暂停任务
     *
     * @param jobName
     * @param jobGroup
     */
    void pauseJob(String jobName, String jobGroup);

    /**
     * 恢复任务
     *
     * @param jobName
     * @param jobGroup
     */
    void resumeJob(String jobName, String jobGroup);

    /**
     * 删除任务
     *
     * @param jobName
     * @param jobGroup
     */
    void deleteJob(String jobName, String jobGroup);

    /**
     * 更新任务cron表达式
     *
     * @param jobName
     * @param jobGroup
     * @param cronExpression
     */
    void updateCronExpression(String jobName, String jobGroup, String cronExpression);

}
