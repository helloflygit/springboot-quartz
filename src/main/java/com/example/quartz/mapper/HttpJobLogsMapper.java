package com.example.quartz.mapper;

import com.example.quartz.entity.HttpJobLogs;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface HttpJobLogsMapper {

    int insertSelective(HttpJobLogs httpJobLogs);

    List<HttpJobLogs> selectHttpJobLogs(Map<String, Object> map);

    Integer selectHttpJobLogsCount(Map<String, Object> map);
}
