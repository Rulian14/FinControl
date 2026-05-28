package com.fincontrol.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincontrol.dto.board.DashboardDTO;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard(){
        return null;
    }
}
