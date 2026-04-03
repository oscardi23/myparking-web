package com.devsoft.myparking.controllers;


import com.devsoft.myparking.dtos.ParkingDTO;
import com.devsoft.myparking.security.CustomUserDetails;
import com.devsoft.myparking.services.ParkingService;
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
import java.util.Optional;

@Controller
@AllArgsConstructor

public class ParkingController {

    private final ParkingService parkingService;

    // show parking admin

    @GetMapping("/admin/parking")
    public String showParking(Authentication auth, Model model) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String parkingId = userDetails.getParkingId();

        if(parkingId == null) {
            model.addAttribute("parking", new ParkingDTO());
            model.addAttribute("isNew", true);
            return "/admin/parking";

        }



        Optional<ParkingDTO> parking = parkingService.getParking(userDetails.getParkingId());

        if (parking.isPresent()) {

            model.addAttribute("parking", parking);
            model.addAttribute("isNew", false);


        }else {
            model.addAttribute("parking", new ParkingDTO());
            model.addAttribute("isNew", true);
        }

        return "admin/parking";
    }


    // show super_admin parkings

    @GetMapping("/super/parkings")
    public String showAllParkings(Model model) {

        List<ParkingDTO> parkingDTOList = parkingService.getAllParkings();

        model.addAttribute("parkings", parkingDTOList);
        model.addAttribute("isEmpty", parkingDTOList.isEmpty());

        return "super/parkings";


    }


    // create parking

    @PostMapping("/admin/parking/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createParking(@Valid @RequestBody ParkingDTO parkingDTO,
                                                             BindingResult result){

        if (result.hasErrors()){

            String errorMsg = result.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .findFirst()
                    .orElse("Error de validacion");

            return ApiResponse.error(errorMsg);
        }
        try{
            ParkingDTO created = parkingService.createParking(parkingDTO);
            return ApiResponse.success(created);
        }catch (Exception e) {
            return ApiResponse.serverError("Error creando el parqueadero");
        }


    }


    // update parking
    @PutMapping("/admin/parking/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateParking(@Valid @RequestBody ParkingDTO parkingDTO, BindingResult result) {

        if (result.hasErrors()) {

            String errorMsg = result.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .findFirst()
                    .orElse("Error de validacion");

            return ApiResponse.error(errorMsg);
        }

        try{

            ParkingDTO updateParking = parkingService.updateParking(parkingDTO);
            return ApiResponse.success(updateParking);
        }catch (RuntimeException e){
            return ApiResponse.error(e.getMessage());
        }


    }

    // enable - disable parking

    @PatchMapping("/admin/parking/toggle")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleActive(Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String parkingId = userDetails.getParkingId();

        try{
            parkingService.toggleActive(parkingId);

            return  ApiResponse.message("Estado del parqueadero actualizado");

        }catch (RuntimeException e) {

            return ApiResponse.error(e.getMessage());
        }

    }


}
