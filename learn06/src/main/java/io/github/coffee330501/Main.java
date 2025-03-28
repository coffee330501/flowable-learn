package io.github.coffee330501;

import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

import java.time.Duration;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        ProcessEngine processEngine = getProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();

        historyService.createHistoricProcessInstanceQuery()
                .finishedBefore(new Date(new Date().getTime() - 365L * 24 * 60 * 60 * 1000))
                // 批量删除历史变量实例
                .deleteSequentiallyUsingBatch(10, "Custom Delete Batch");
    }

    private static void hisQuery(HistoryService historyService) {
        // 查询历史变量实例
        System.out.println("========= 查询历史变量实例 =========");
        historyService.createHistoricVariableInstanceQuery()
                .processInstanceId("10004")
                .list()
                .forEach(System.out::println);

        // 查询历史活动实例
        System.out.println("========= 查询历史活动实例 =========");
        historyService.createHistoricActivityInstanceQuery()
                .processInstanceId("10004")
                .list()
                .forEach(System.out::println);

        // 查询历史详细信息
        System.out.println("========= 查询历史详细信息 =========");
        historyService.createHistoricDetailQuery()
                .processInstanceId("10004")
                .list()
                .forEach(System.out::println);

        // 查询历史任务实例信息
        System.out.println("========= 查询历史任务实例信息 =========");
        historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("10004")
                .list()
                .forEach(System.out::println);
    }

    private static ProcessEngine getProcessEngine() {
        // 配置流程引擎
        ProcessEngineConfiguration conf = new StandaloneProcessEngineConfiguration()
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true")
                .setDatabaseSchemaUpdate(StandaloneProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                // 允许清理历史
                .setEnableHistoryCleaning(true)
                // 历史清理时间周期 每天凌晨1点清理一次
                .setHistoryCleaningTimeCycleConfig("0 0 1 * * ?")
                // 清理365天之前的历史
                .setCleanInstancesEndedAfter(Duration.ofDays(365))
                // 历史存储级别
                .setHistory(HistoryLevel.AUDIT.getKey());
        // 历史级别存储在数据库中（表 ACT_GE_PROPERTY，属性名为 historyLevel）。从 5.11 开始，此值不再使用，并从数据库中忽略/删除。
//                .setHistoryLevel();
        // 初始化流程引擎
        return conf.buildProcessEngine();
    }
}