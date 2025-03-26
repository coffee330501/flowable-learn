package io.github.coffee330501.service;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class JpaService {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private RepositoryService repositoryService;

    @Transactional
    public void startProcess() {
        // 查询流程定义
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey("easy_holiday_2")
                .orderByProcessDefinitionId()
                .desc()
                .list()
                .get(0);

        // 设置参数
        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("name", "张三");
            put("startTime", LocalDate.now());
            put("endTime", LocalDate.now());
            put("reason", "家里有事");
        }};

        // 启动流程实例
        runtimeService.startProcessInstanceById(processDefinition.getId(), map);
//        runtimeService.startProcessInstanceWithForm(processDefinition.getId(), processDefinition.getKey(), map, "提交表单");
    }

    public void approve() {
        // 查询流程定义
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey("easy_holiday_2")
                .orderByProcessDefinitionId()
                .desc()
                .list()
                .get(0);

        List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(processDefinition.getKey()).list();
        if(tasks.isEmpty()) return;

        Task task = tasks.get(0);
        HashMap<String, Object> map = new HashMap<>();
        map.put("approved", true);
        taskService.complete(task.getId(), map);
    }
}
