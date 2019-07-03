package com.example.quartz.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.quartz.constants.Constant;
import com.example.quartz.entity.HttpJobDetails;
import com.example.quartz.entity.HttpJobLogs;
import com.example.quartz.entity.Page;
import com.example.quartz.entity.param.AddHttpJobParam;
import com.example.quartz.entity.vo.HttpJobDetailVO;
import com.example.quartz.job.HttpGetJob;
import com.example.quartz.job.HttpPostFormDataJob;
import com.example.quartz.job.HttpPostJsonJob;
import com.example.quartz.mapper.HttpJobDetailsMapper;
import com.example.quartz.mapper.HttpJobLogsMapper;
import com.example.quartz.service.HttpJobService;
import com.example.quartz.util.JobUtil;
import com.example.quartz.util.JsonValidUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http类型任务Service
 *
 * @author hellofly
 * @date 2019/4/9
 */
@Service
public class HttpJobServiceImpl implements HttpJobService {

    private static final Logger logger = LogManager.getLogger(HttpJobServiceImpl.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private HttpJobLogsMapper httpJobLogsMapper;

    @Autowired
    private HttpJobDetailsMapper httpJobDetailsMapper;

    @Resource
    private JobUtil jobUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addHttpJob(AddHttpJobParam addHttpJobParam) {

        String jobName = addHttpJobParam.getJobName();
        String jobGroup = addHttpJobParam.getJobGroup();
        HttpJobDetails httpJobDetails = httpJobDetailsMapper.selectByJobNameAndJobGroup(jobName, jobGroup);
        if (httpJobDetails != null) {
            //通过jobName和jobGroup确保任务的唯一性
            throw new RuntimeException("任务名称重复!");
        }

        String requestType = addHttpJobParam.getRequestType();
        String description = addHttpJobParam.getDescription();
        String cronExpression = addHttpJobParam.getCronExpression();
        String url = addHttpJobParam.getUrl();
        String jsonParamsStr = addHttpJobParam.getParams();

        httpJobDetails = new HttpJobDetails();
        httpJobDetails.setJobName(jobName);
        httpJobDetails.setJobGroup(jobGroup);
        httpJobDetails.setDescription(description);
        httpJobDetails.setRequestType(requestType);
        httpJobDetails.setHttpUrl(url);
        if (!JsonValidUtil.isJson(jsonParamsStr)) {
            throw new RuntimeException("请将请求参数转为合法的json字符串!");
        }

        Map<String, Object> jobParamsMap = new HashMap<>();
        jobParamsMap.put(Constant.URL, url);
        jobParamsMap.put(Constant.PARAMS, jsonParamsStr);

        JobDetail jobDetail = null;
        //根据不同类型的job构建job信息
        switch (requestType) {
            //postJson
            case Constant.POST_JSON:
                jobDetail = JobBuilder.newJob(HttpPostJsonJob.class)
                        .withIdentity(jobName, jobGroup)
                        .build();

                //jsonStr的参数直接用
                if (StringUtils.isNotEmpty(jsonParamsStr)) {
                    httpJobDetails.setHttpParams(jsonParamsStr);
                }
                break;

            //postFormData
            case Constant.POST_FORM_DATA:
                jobDetail = JobBuilder.newJob(HttpPostFormDataJob.class)
                        .withIdentity(jobName, jobGroup)
                        .build();

                //jsonStr参数转为formData的Map
                Map<String, Object> formDataParamMap;
                if (StringUtils.isEmpty(jsonParamsStr)) {
                    formDataParamMap = null;
                } else {
                    formDataParamMap = JSON.parseObject(jsonParamsStr, Map.class);
                    httpJobDetails.setHttpParams(formDataParamMap.toString());
                }
                jobParamsMap.put(Constant.PARAMS, formDataParamMap);

                break;

            //get
            case Constant.GET:
                jobDetail = JobBuilder.newJob(HttpGetJob.class)
                        .withIdentity(jobName, jobGroup)
                        .build();

                //jsonStr参数转为formData的Map
                Map<String, Object> paramMap;
                if (StringUtils.isEmpty(jsonParamsStr)) {
                    paramMap = null;
                } else {
                    paramMap = JSON.parseObject(jsonParamsStr, Map.class);
                    httpJobDetails.setHttpParams(paramMap.toString());
                }
                jobParamsMap.put(Constant.PARAMS, paramMap);

                break;
        }

        //任务信息
        jobDetail.getJobDataMap().putAll(jobParamsMap);
        jobDetail.getJobDataMap().put(Constant.REQUEST_TYPE, requestType);

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder;
        try {
            scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        } catch (Exception e) {
            throw new RuntimeException("Illegal CronExpression!");
        }

        TriggerKey triggerKey = jobUtil.getTriggerKeyByJob(jobName, jobGroup);

        //构建一个trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startNow()
                .withSchedule(scheduleBuilder).build();

        try {
            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException("Schedule Exception.", e);
        }

        httpJobDetailsMapper.insertSelective(httpJobDetails);
        logger.info("Success in addJob, [{}]-[{}]", jobName, jobGroup);

    }

    @Override
    public Page<HttpJobDetailVO> getHttpJobs(String searchParam, Integer pageSize, Integer pageNum) {
        Integer beginIndex = (pageNum - 1) * pageSize;

        Map<String, Object> sqlMap = new HashMap<>();
        sqlMap.put("searchParam", searchParam);
        sqlMap.put("pageSize", pageSize);
        sqlMap.put("beginIndex", beginIndex);

        List<HttpJobDetailVO> httpJobDetailVOList = httpJobDetailsMapper.selectHttpJobs(sqlMap);

        for (HttpJobDetailVO httpJobDetailVO : httpJobDetailVOList) {
            //设置jobStatusInfo
            String jobStatusInfo = jobUtil.getJobStatusInfo(httpJobDetailVO.getJobName(), httpJobDetailVO.getJobGroup());
            httpJobDetailVO.setJobStatusInfo(jobStatusInfo);
            //任务状态正常，根据cron表达式计算下次运行时间
            if (StringUtils.equals(jobStatusInfo, Constant.JOB_STATUS_NORMAL)) {
                httpJobDetailVO.setNextFireTime(jobUtil.getNextFireDate(httpJobDetailVO.getCronExpression()));
            }
        }

        Page<HttpJobDetailVO> httpJobDetailVOPageVO = new Page<>();
        httpJobDetailVOPageVO.setPageNum(pageNum);
        httpJobDetailVOPageVO.setPageSize(pageSize);
        httpJobDetailVOPageVO.setCount(httpJobDetailVOList.size());
        httpJobDetailVOPageVO.setTotalCount(httpJobDetailsMapper.selectHttpJobsCount(sqlMap));
        httpJobDetailVOPageVO.setResultList(httpJobDetailVOList);

        return httpJobDetailVOPageVO;
    }

    @Override
    public Page<HttpJobDetailVO> getHistoryHttpJobs(String searchParam, Integer pageSize, Integer pageNum) {
        Integer beginIndex = (pageNum - 1) * pageSize;

        Map<String, Object> sqlMap = new HashMap<>();
        sqlMap.put("searchParam", searchParam);
        sqlMap.put("pageSize", pageSize);
        sqlMap.put("beginIndex", beginIndex);

        List<HttpJobDetailVO> httpJobDetailVOList = httpJobDetailsMapper.selectHistoryHttpJobs(sqlMap);

        Page<HttpJobDetailVO> httpJobDetailVOPageVO = new Page<>();
        httpJobDetailVOPageVO.setPageNum(pageNum);
        httpJobDetailVOPageVO.setPageSize(pageSize);
        httpJobDetailVOPageVO.setCount(httpJobDetailVOList.size());
        httpJobDetailVOPageVO.setTotalCount(httpJobDetailsMapper.selectHistoryHttpJobsCount(sqlMap));
        httpJobDetailVOPageVO.setResultList(httpJobDetailVOList);

        return httpJobDetailVOPageVO;
    }

    @Override
    public Page<HttpJobLogs> getHttpJobLogs(String jobName, String jobGroup, String searchParam, Integer pageSize, Integer pageNum) {
        Integer beginIndex = (pageNum - 1) * pageSize;

        Map<String, Object> sqlMap = new HashMap<>();
        sqlMap.put("jobName", jobName);
        sqlMap.put("jobGroup", jobGroup);
        sqlMap.put("searchParam", searchParam);
        sqlMap.put("pageSize", pageSize);
        sqlMap.put("beginIndex", beginIndex);

        List<HttpJobLogs> httpJobLogsList = httpJobLogsMapper.selectHttpJobLogs(sqlMap);

        Page<HttpJobLogs> httpJobDetailVOPageVO = new Page<>();
        httpJobDetailVOPageVO.setPageNum(pageNum);
        httpJobDetailVOPageVO.setPageSize(pageSize);
        httpJobDetailVOPageVO.setCount(httpJobLogsList.size());
        httpJobDetailVOPageVO.setTotalCount(httpJobLogsMapper.selectHttpJobLogsCount(sqlMap));
        httpJobDetailVOPageVO.setResultList(httpJobLogsList);

        return httpJobDetailVOPageVO;
    }

}
