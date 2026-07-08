package com.example.orn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController
{
     @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/index")
    public String dashboard() {
        return "index";
    }

    @GetMapping("/excel-upload")
    public String excelUpload() {
        return "excel-upload";
    }

    @GetMapping("/orn-entry")
    public String ornEntry(){
        return "orn-entry";
    }

    @GetMapping("/orn-matching")
    public String ornMatching(){
        return "orn-matching";
    }
    @GetMapping("/reports")
    public String reports(){
        return "reports";
    }
    @GetMapping("/expensetracker")
    public String expenseTracker(){
        return "expensetracker";
    }
}
