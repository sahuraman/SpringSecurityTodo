package com.base.todo.todosecurityhibernate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/home")
    public String home() {
        return "Welcome to /home (authenticated)";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Welcome to /admin (ADMIN only)";
    }



}   