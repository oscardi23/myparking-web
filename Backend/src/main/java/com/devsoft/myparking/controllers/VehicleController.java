package com.devsoft.myparking.controllers;


import com.devsoft.myparking.dtos.VehicleDTO;
import com.devsoft.myparking.security.CustomUserDetails;
import com.devsoft.myparking.services.VehicleService;
import com.devsoft.myparking.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RequestMapping("/admin")
@Controller
public class VehicleController {

    private final VehicleService vehicleService;


    // list vehicles of parking

    @GetMapping("/vehicles")
    public String showVehicles(Authentication auth, Model model){

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String parkingId = userDetails.getParkingId();

        List<VehicleDTO> vehicles = vehicleService.getVehicleByParking(parkingId);

        model.addAttribute("vehicles", vehicles);
        model.addAttribute("isEmpty", vehicles.isEmpty());

        return "admin/vehicles";

    }

    // find vehicles by plate

    @GetMapping("vehicles/search")
    @ResponseBody

    public ResponseEntity<Map<String, Object>> searchByPlate(@RequestParam String plate) {


        try{

            VehicleDTO vehicle = vehicleService.getVehicleByPlate(plate);
            return ApiResponse.success(vehicle);

        }catch (RuntimeException e ) {

            return ApiResponse.error("Vehiculo no encontrado con placa" + plate.toUpperCase());
        }

    }

    // get vehicle by ID
    @GetMapping("/vehicles/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getVehicleById(@PathVariable String id){

        try {

            VehicleDTO vehicle = vehicleService.getVehicle(id);
            return  ApiResponse.success(vehicle);

        }catch (RuntimeException e){

            return ApiResponse.error(e.getMessage());
        }

    }


    // find vehicles by client
    @GetMapping("/vehicles/client/client{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> findVehiclesByClient(@PathVariable String clientId) {

        try {
            List<VehicleDTO> vehicles = vehicleService.getVehicleByClient(clientId);
            return  ApiResponse.success(vehicles);
        }catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }


    }


    // create vehicle

    @PostMapping("/vehicles/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO,
                                                             BindingResult result, Authentication auth ) {

        if(result.hasErrors()) {
            String errorMsg = result.getFieldErrors()
                    .stream()
                    .map(e -> e.getDefaultMessage())
                    .findFirst()
                    .orElse("Error de validacion");
            return ApiResponse.error(errorMsg);

        }

        try{

            CustomUserDetails details = (CustomUserDetails) auth.getDetails();
            String parkingId = details.getParkingId();

            VehicleDTO created = vehicleService.createVehicle(vehicleDTO, parkingId);
            return     ApiResponse.success(created);

        }catch (RuntimeException e){
            return ApiResponse.error(e.getMessage());
        }catch (Exception e){
            return ApiResponse.serverError("Error creando el vehiculo");
        }




    }


    // update vehicle

    @PutMapping("vehicles/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateVehicle(@Valid @RequestBody VehicleDTO vehicleDTO,
                                                             BindingResult result){


        if (result.hasErrors()) {

            String errorMsg = result.getFieldErrors()
                    .stream()
                    .map(e -> e.getDefaultMessage())
                    .findFirst()
                    .orElse("Error de validacion");

            return ApiResponse.error(errorMsg);
        }

        try{
            VehicleDTO updated = vehicleService.updateVehicle(vehicleDTO);
            return ApiResponse.success(updated);
        }catch (RuntimeException e) {
            return   ApiResponse.error(e.getMessage());
        }catch (Exception e){
            return  ApiResponse.serverError("Error actualizando el vehiculo");
        }


    }


    // disable vehicle

    @PatchMapping("vehicles/toggle/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleActive(@PathVariable String id) {

        try{

            vehicleService.toggleActive(id);
            return ApiResponse.message("Estado del vehiculo actualizado");

        }catch (RuntimeException e){

            return ApiResponse.error(e.getMessage());
        }

    }




}