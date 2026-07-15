package com.xxs.aispringbooot.controller;

import com.xxs.aispringbooot.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Test {
    @GetMapping("/test")
    public Result test(){
        return Result.success("SHAYISI");
    }
}
