package com.example.quartz.config.quartz;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Quartz配置类
 *
 * @author hellofly
 * @date 2019/4/9
 */
@Configuration
public class QuartzConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobFactory jobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        try {
            schedulerFactoryBean.setAutoStartup(true);
            schedulerFactoryBean.setDataSource(dataSource);
            schedulerFactoryBean.setJobFactory(jobFactory);
            schedulerFactoryBean.setQuartzProperties(properties());
            schedulerFactoryBean.setOverwriteExistingJobs(true);
            // 延迟3s启动quartz
            schedulerFactoryBean.setStartupDelay(3);
            schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schedulerFactoryBean;

    }

    @Bean
    public Properties properties() throws IOException {
        Properties prop = new Properties();
        prop.load(new ClassPathResource("/quartz.properties").getInputStream());
        return prop;
    }

    @Bean(name = "scheduler")
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

}
