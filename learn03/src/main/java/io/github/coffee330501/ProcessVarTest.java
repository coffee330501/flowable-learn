package io.github.coffee330501;

import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.variable.api.history.HistoricVariableInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessVarTest {
    public static void main(String[] args) {
        ProcessEngine processEngine = getProcessEngine();

        getVar(processEngine);
    }

    private static void getVar(ProcessEngine processEngine) {
        // 读取流程变量
        // 读取流程实例级别的变量
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Object variable = runtimeService.getVariable("executionId", "name");
        Map<String, Object> variables = runtimeService.getVariables("executionId");
        Object variableLocal = runtimeService.getVariableLocal("executionId", "name");
        // ...

        // 在 task 范围读取
        TaskService taskService = processEngine.getTaskService();
        Object variable1 = taskService.getVariable("executionId", "name");
        // ...

        // 历史变量读取
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId("processInstanceId")
                .list();
    }

    private static void setProcessVar(ProcessEngine processEngine) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 设置流程变量

        // 方式一: runtimeService.startProcessInstanceByXxx
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        runtimeService.startProcessInstanceByKey("my-process", map);

        // 方式二: runtimeService.setVariable 设置流程变量
        runtimeService.setVariables("executionId",map);
        runtimeService.setVariable("executionId","name","张三");
        runtimeService.setVariableLocal("executionId","name","张三");
        runtimeService.setVariablesLocal("executionId",map);
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