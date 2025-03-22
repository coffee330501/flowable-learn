package io.github.coffee330501.service;

import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipInputStream;

@Service
public class FlowableService {
    @Resource
    private TaskService taskService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private RepositoryService repositoryService;

    public List<Task> tasks() {
        return taskService.createTaskQuery().list();
    }

    /**
     * 通过spring boot启动自动扫描 resources/processes 下的 *.bpmn20.xml 文件部署
     * 启动项目会自动部署，部署一次后，再次启动项目不会部署
     */
    public long deployByClasspath() {
        return repositoryService.createDeploymentQuery().processDefinitionKey("newholidayRequest").count();
    }

    /**
     * 通过字符串部署
     * 多次执行会部署多次
     */
    public Long deployByString() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"\n" +
                "             xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "             xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "             xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"\n" +
                "             xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\"\n" +
                "             xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\"\n" +
                "             xmlns:flowable=\"http://flowable.org/bpmn\"\n" +
                "             typeLanguage=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "             expressionLanguage=\"http://www.w3.org/1999/XPath\"\n" +
                "             targetNamespace=\"http://www.flowable.org/processdef\">\n" +
                "\n" +
                "    <process id=\"deployByString\" name=\"Holiday Request\" isExecutable=\"true\">\n" +
                "\n" +
                "        <startEvent id=\"startEvent\"/>\n" +
                "        <sequenceFlow sourceRef=\"startEvent\" targetRef=\"approveTask\"/>\n" +
                "\n" +
                "        <userTask id=\"approveTask\" name=\"Approve or reject request\" flowable:assignee=\"lisi\"/>\n" +
                "        <sequenceFlow sourceRef=\"approveTask\" targetRef=\"decision\"/>\n" +
                "\n" +
                "        <exclusiveGateway id=\"decision\"/>\n" +
                "        <sequenceFlow sourceRef=\"decision\" targetRef=\"agreeReq\">\n" +
                "            <conditionExpression xsi:type=\"tFormalExpression\">\n" +
                "                <![CDATA[\n" +
                "          ${approved}\n" +
                "        ]]>\n" +
                "            </conditionExpression>\n" +
                "        </sequenceFlow>\n" +
                "        <sequenceFlow  sourceRef=\"decision\" targetRef=\"sendRejectionMail\">\n" +
                "            <conditionExpression xsi:type=\"tFormalExpression\">\n" +
                "                <![CDATA[\n" +
                "          ${!approved}\n" +
                "        ]]>\n" +
                "            </conditionExpression>\n" +
                "        </sequenceFlow>\n" +
                "\n" +
                "        <serviceTask id=\"agreeReq\" name=\"Enter holidays in external system\"\n" +
                "                     flowable:class=\"io.github.coffee330501.AgreeReq\"/>\n" +
                "        <sequenceFlow sourceRef=\"agreeReq\" targetRef=\"holidayApprovedTask\"/>\n" +
                "\n" +
                "        <userTask id=\"holidayApprovedTask\" name=\"Holiday approved\"/>\n" +
                "        <sequenceFlow sourceRef=\"holidayApprovedTask\" targetRef=\"approveEnd\"/>\n" +
                "\n" +
                "        <serviceTask id=\"sendRejectionMail\" name=\"Send out rejection email\"\n" +
                "                     flowable:class=\"io.github.coffee330501.SendRejectionMail\"/>\n" +
                "        <sequenceFlow sourceRef=\"sendRejectionMail\" targetRef=\"rejectEnd\"/>\n" +
                "\n" +
                "        <endEvent id=\"approveEnd\"/>\n" +
                "\n" +
                "        <endEvent id=\"rejectEnd\"/>\n" +
                "    </process>\n" +
                "\n" +
                "</definitions>";
        repositoryService.createDeployment().addString("deployByString.bpmn20.xml", xml).deploy().getId();
        return repositoryService.createDeploymentQuery().processDefinitionKey("deployByString").count();
    }

    /**
     * 通过zip部署
     */
    public Long deployByZip() throws IOException {
        repositoryService
                .createDeployment()
                .addZipInputStream(
                        new ZipInputStream(
                                Files.newInputStream(new ClassPathResource("bpmn.zip").getFile().toPath())
                        )).deploy();
        return repositoryService.createDeploymentQuery().processDefinitionKey("deployByZip").count();
    }

    /**
     * 通过BpmnModel部署
     */
    public Long deployByBpmnModel() {
        BpmnModel bpmnModel = new BpmnModel();

        Process process = new Process();
        process.setId("deployByBpmnModel");
        process.setName("Deploy By BpmnModel"); // name必需
        bpmnModel.addProcess(process);

        // 开始事件
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        process.addFlowElement(startEvent);

        // 用户任务
        UserTask userTask = new UserTask();
        userTask.setId("userTask");
        process.addFlowElement(userTask);

        // 结束事件
        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        process.addFlowElement(endEvent);

        // 顺序流
        process.addFlowElement(new SequenceFlow("startEvent", "userTask"));
        process.addFlowElement(new SequenceFlow("userTask", "endEvent"));

        // 部署
        repositoryService.createDeployment().addBpmnModel("deployByBpmnModel.bpmn20.xml", bpmnModel).name("Deploy By BpmnModel").deploy();
        long deployByBpmnModel = repositoryService.createDeploymentQuery().processDefinitionKey("deployByBpmnModel").count();
        return deployByBpmnModel;
    }
}
