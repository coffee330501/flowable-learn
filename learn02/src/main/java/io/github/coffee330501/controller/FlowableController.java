package io.github.coffee330501.controller;


import io.github.coffee330501.service.FlowableService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/flowable")
public class FlowableController {
    @Resource
    private FlowableService flowableService;

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public String test() {
        // 建议套层DTO，task有些属性是懒加载的，无法直接序列化返回
        return String.valueOf(flowableService.tasks());
    }

    /**
     * 不同的部署方法测试
     */
    @GetMapping(value = "/deploy-methods/{type}")
    public Long testDeployMethods(@PathVariable String type) throws IOException {
        switch (type) {
            case "0":
                return flowableService.deployByClasspath();
            case "1":
                return flowableService.deployByString();
            case "2":
                return flowableService.deployByZip();
            case "3":
                return flowableService.deployByBpmnModel();
            default:
                return 0L;
        }

    }
}
