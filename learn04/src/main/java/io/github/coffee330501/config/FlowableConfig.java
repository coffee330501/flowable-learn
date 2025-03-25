package io.github.coffee330501.config;

import io.github.coffee330501.formType.CoffeeFormType;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.variable.api.types.VariableType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FlowableConfig {
    @Resource
    private DataSource dataSource;
    @Resource
    private TransactionManager transactionManager;
    @Resource
    private CoffeeFormType coffeeFormType; // 确保你已经定义了这个Bean

    @Bean(name = "processEngineConfiguration")
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setTransactionManager((PlatformTransactionManager) transactionManager);
        configuration.setDatabaseSchemaUpdate("false"); // 根据需要设置数据库更新策略，如"true"为每次启动时更新数据库结构。
        configuration.setCustomFormTypes(Collections.singletonList(coffeeFormType)); // 使用自定义参数类型。
        return configuration;
    }
}
