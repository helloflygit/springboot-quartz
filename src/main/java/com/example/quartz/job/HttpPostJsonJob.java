package com.example.quartz.job;

import com.example.quartz.constants.Constant;
import com.example.quartz.entity.HttpJobLogs;
import com.example.quartz.mapper.HttpJobLogsMapper;
import com.example.quartz.util.HttpClientUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@DisallowConcurrentExecution
public class HttpPostJsonJob implements Job {

    private static final Logger logger = LogManager.getLogger(HttpPostJsonJob.class);

    @Autowired
    private HttpJobLogsMapper httpJobLogsMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        String jobName = jobDetail.getKey().getName();
        String jobGroup = jobDetail.getKey().getGroup();

        Map<String, Object> jobParamsMap = jobDetail.getJobDataMap();

        String requestType = (String) jobParamsMap.get(Constant.REQUEST_TYPE);
        String url = (String) jobParamsMap.get(Constant.URL);
        String jsonParam = (String) jobParamsMap.get(Constant.PARAMS);

        HttpJobLogs httpJobLogs = new HttpJobLogs();
        httpJobLogs.setJobName(jobName);
        httpJobLogs.setJobGroup(jobGroup);
        httpJobLogs.setRequestType(requestType);
        httpJobLogs.setHttpUrl(url);
        httpJobLogs.setHttpParams(jsonParam);

        String result = HttpClientUtil.postJson(url, jsonParam);
        httpJobLogs.setResult(result);

        logger.info("Success in execute [{}_{}]", jobName, jobGroup);

        httpJobLogsMapper.insertSelective(httpJobLogs);
    }

}
