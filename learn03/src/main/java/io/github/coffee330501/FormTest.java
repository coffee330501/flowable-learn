package io.github.coffee330501;

import org.flowable.engine.FormService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.HashMap;
import java.util.Map;

public class FormTest {
    public static void main(String[] args) {
        ProcessEngine processEngine = getProcessEngine();
    }

    private static void submitFormData(ProcessEngine processEngine) {
        // 提交表单数据
        FormService formService = processEngine.getFormService();
        Map<String, String> formData = new HashMap<>();
        formData.put("1","李四");
        formData.put("2","生病");
        formService.submitStartFormData("my-process",formData);
        formService.submitTaskFormData("taskId",formData);
    }

    private static void getFormData(ProcessEngine processEngine) {
        // 获取表单数据
        FormService formService = processEngine.getFormService();
        StartFormData startFormData = formService.getStartFormData("my-process");
        TaskFormData taskFormData = formService.getTaskFormData("taskId");
    }

    private static ProcessEngine getProcessEngine() {
        // 配置流程引擎
        ProcessEngineConfiguration conf = new StandaloneProcessEngineConfiguration()
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true")
                .setDatabaseSchemaUpdate(StandaloneProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // 初始化流程引擎
        return conf.buildProcessEngine();
    }
}
