package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @RequestMapping("/")
    public String home() {
        return "home"; // home.html 템플릿 반환
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // login.html 템플릿 반환
    }

    @ResponseBody
    @RequestMapping("/test")
    public String test() {
        return "OK";
    }

    @ResponseBody
    @RequestMapping("/adminOnly")
    public String adminOnly() {
        return "SecretPage";
    }
}
