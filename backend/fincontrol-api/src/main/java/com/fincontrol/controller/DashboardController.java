package com.fincontrol.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fincontrol.dto.board.DashboardDTO;
import com.fincontrol.service.DashBoardService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;



@RestController
@RequestMapping("/dashboard")
@Validated 
public class DashboardController {

    private final DashBoardService dashBoardService;

    public DashboardController(DashBoardService dashBoardService){
        this.dashBoardService = dashBoardService;
    }
    @GetMapping
  
    public ResponseEntity<DashboardDTO> getDashboard(Authentication authentication, @RequestParam  @Min(2000)  int ano, @RequestParam  @Min(1) @Max(12) int mes){
        Long idUsuario = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(dashBoardService.obterDashboardDTO(idUsuario, ano, mes));
    }
}
