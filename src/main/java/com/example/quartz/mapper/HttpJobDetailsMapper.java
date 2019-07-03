package com.example.quartz.mapper;

import com.example.quartz.entity.HttpJobDetails;
import com.example.quartz.entity.vo.HttpJobDetailVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface HttpJobDetailsMapper {

    HttpJobDetails selectByJobNameAndJobGroup(@Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    int insertSelective(HttpJobDetails httpJobDetails);

    List<HttpJobDetailVO> selectHttpJobs(Map<String, Object> map);

    Integer selectHttpJobsCount(Map<String, Object> map);

    List<HttpJobDetailVO> selectHistoryHttpJobs(Map<String, Object> map);

    Integer selectHistoryHttpJobsCount(Map<String, Object> map);

}
