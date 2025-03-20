package io.github.coffee330501;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ProcessEngine processEngine = getProcessEngine();
        hisQuery(processEngine);
    }

    private static void hisQuery(ProcessEngine processEngine) {
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processDefinitionId("holidayRequest3:1:22503")
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();
        for (HistoricActivityInstance historicActivityInstance : list) {
            System.out.println("activity " + historicActivityInstance.getActivityId() + " took " + historicActivityInstance.getDurationInMillis() + "ms");
        }
    }

    private static void delProcess(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 已经有流程实例，则删除流程定义时会报错
//        repositoryService.deleteDeployment("1");
        // 级联删除流程实例
        repositoryService.deleteDeployment("1", true);
    }

    private static void agreeReq(ProcessEngine processEngine) {
        Task task = processEngine.getTaskService().createTaskQuery()
                .processDefinitionKey("holidayRequest4")
                .taskAssignee("lisi")
                .singleResult();
        // 添加流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", true); // 同意请假
        processEngine.getTaskService().complete(task.getId(), variables);
    }

    private static void rejectReq(ProcessEngine processEngine) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService
                .createTaskQuery()
                .taskAssignee("lisi")
                .processDefinitionKey("holidayRequest4")
                .singleResult();
        // 添加流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", false); // 拒绝请假
        // 完成任务
        taskService.complete(task.getId(), variables);
    }

    private static void queryTask(ProcessEngine processEngine) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService
                .createTaskQuery()
                .processDefinitionKey("holidayRequest")
                .taskAssignee("lisi")
                .list();
        for (Task task : tasks) {
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getName() = " + task.getName());
        }
    }

    private static void startEvent(ProcessEngine processEngine) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee", "张三");// 谁申请请假
        variables.put("nrOfHolidays", 3); // 请几天假
        variables.put("description", "工作累了，想出去玩玩"); // 请假的原因
        // 启动流程，第一个参数是流程定义的Id
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest4", variables);
        // 输出流程实例信息
        System.out.println("流程实例ID:" + processInstance.getId());
        System.out.println("流程定义ID:" + processInstance.getProcessDefinitionId());
        System.out.println("当前活动ID:" + processInstance.getActivityId()); // null
    }

    private static void deploy(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService
                .createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .name("请假流程")
                .deploy(); // 执行部署，每次部署都会往数据库中插入一条记录
        System.out.println("deployment id = " + deployment.getId());
        System.out.println("deployment name = " + deployment.getName());
    }

    private static void queryDeployment(ProcessEngine processEngine) {
        Deployment deployment = processEngine.getRepositoryService()
                .createDeploymentQuery()
                .deploymentId("1")
                .singleResult();
        System.out.println("deployment id = " + deployment.getId());
        System.out.println("deployment name = " + deployment.getName());
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