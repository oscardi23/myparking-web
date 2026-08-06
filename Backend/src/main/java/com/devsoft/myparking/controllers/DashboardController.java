package com.devsoft.myparking.controllers;

import com.devsoft.myparking.services.ClientService;
import com.devsoft.myparking.services.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final ClientService clientService;
    private final VehicleService vehicleService;

    @GetMapping("/list")
    public String showDashboard(Model model){


        model.addAttribute("totalClients", clientService.countClients());
        model.addAttribute("totalVehicles", vehicleService.countVehicles());


        return "dashboard";
    }
}


