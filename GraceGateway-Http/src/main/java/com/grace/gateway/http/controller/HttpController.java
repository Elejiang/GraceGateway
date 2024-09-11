package com.grace.gateway.http.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class HttpController {

    @GetMapping("/http-server/ping1")
    public String ping1() {
        log.info("http ping1 server received request");
        return "this is ping1";
    }

    @GetMapping("/http-server/ping2")
    public String ping2() {
        log.info("http ping2 server received request");
        return "this is ping2";
    }

}
