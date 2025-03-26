package io.github.coffee330501.controller;

import io.github.coffee330501.service.JpaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/jpa")
public class JpaController {
    @Resource
    private JpaService jpaService;

    @GetMapping("/start")
    public void startProcess() {
        jpaService.startProcess();
    }

    @GetMapping("/approve")
    public void approve() {
        jpaService.approve();
    }
}
