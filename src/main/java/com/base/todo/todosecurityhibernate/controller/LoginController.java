package com.base.todo.todosecurityhibernate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        System.out.println("=== LoginController hit, returning view: login ===");
        return "login";
    }
}