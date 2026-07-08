package com.example.orn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orn.dto.DashboardDTO;
import com.example.orn.service.DashboardService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="https://ornmanagement.netlify.app")
public class DashboardController 
{
    @Autowired
    DashboardService dashboardService;

    @GetMapping("/index")
        public DashboardDTO dashboard(){
        return dashboardService.getDashboard();
    }
    
}
