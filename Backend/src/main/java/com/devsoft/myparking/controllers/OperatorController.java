package com.devsoft.myparking.controllers;


import com.devsoft.myparking.dtos.UserDTO;
import com.devsoft.myparking.dtos.UserRegisterDTO;
import com.devsoft.myparking.models.Role;
import com.devsoft.myparking.models.User;
import com.devsoft.myparking.security.CustomUserDetails;
import com.devsoft.myparking.services.UserService;
import com.devsoft.myparking.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class OperatorController {


    private final UserService userService;

// show operators
    @GetMapping("/operators")
    public String showOperators(Authentication auth, Model model) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        String parkingId = userDetails.getParkingId();

        List<UserDTO> listOperators = userService.findOperatorByParkingId(parkingId);

        model.addAttribute("operators", listOperators);
        model.addAttribute("isEmpty", listOperators.isEmpty());


        return "admin-operators";
    }


    //create operator

    @PostMapping("operators/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createOperator(@Valid @RequestBody UserRegisterDTO dto, BindingResult result, Authentication auth) {


        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String parkingId = userDetails.getParkingId();

        System.out.println(parkingId);
        System.out.println(dto.getName());
        System.out.println(dto.getLastName());
        System.out.println(dto.getEmail());
        System.out.println(dto.getNumberPhone());
        System.out.println(dto.getPassword());

        if (result.hasErrors()){




                FieldError error = result.getFieldError();

                Map<String, Object> response = new HashMap<>();
                response.put("ok", false);
                response.put("campo", error.getField());
                response.put("valor", error.getRejectedValue());
                response.put("mensaje", error.getDefaultMessage());

                return ResponseEntity.badRequest().body(response);



        }

        /*

        if (result.hasErrors()){

            String errorMsg = result.getFieldErrors()
                    .stream()
                    .map(e -> e.getDefaultMessage())
                    .findFirst()
                    .orElse("Error de validacion");
            return ApiResponse.error(errorMsg);
        }  */


        try {



            UserDTO created = userService.registerOperators(dto, parkingId);

            return ApiResponse.success(created);

        }catch (RuntimeException e){

            return ApiResponse.error(e.getMessage());

        }catch (Exception e){

          return   ApiResponse.serverError("Error creando el operador");
        }


    }



    // enabled disable operator

    @PatchMapping("toggle/{id}")
    @ResponseBody

    public ResponseEntity<Map<String, Object>> toggleOperator(@PathVariable String id){

        try{

            userService.toggleActive(id);
            return ApiResponse.message("Estado del operador actualizado");

        }catch (RuntimeException e){

            return ApiResponse.error(e.getMessage());
        }
    }
}
