package com.grace.gateway.user.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class UserController {

    @GetMapping("/api/user/ping1")
    public String ping1() {
        return "this is user ping1";
    }

    @GetMapping("/api/user/ping2")
    public String ping2() {
        return "this is user ping2";
    }

}
