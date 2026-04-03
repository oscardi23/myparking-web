package com.devsoft.myparking.controllers;


import com.devsoft.myparking.dtos.ClientDTO;
import com.devsoft.myparking.security.CustomUserDetails;
import com.devsoft.myparking.services.ClientService;
import com.devsoft.myparking.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor

@RequestMapping("/admin")
public class ClientController {


    private final ClientService clientService;


    //list clients

    @GetMapping("/clients")
    public String showClients(Authentication auth, Model model) {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String parkingId = userDetails.getParkingId();

        List<ClientDTO> clients = clientService.getClientsByParkingId(parkingId);

        model.addAttribute("clients", clients);
        model.addAttribute("isEmpty", clients.isEmpty());

        return "admin/clients";

    }

    //create clients

    @PostMapping("/clients/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createClient(@Valid @RequestBody ClientDTO clientDTO, BindingResult result,
                                                            Authentication auth){



        if (result.hasErrors()){

            String msgError = result.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .findFirst()
                    .orElse("Error de validacion");

            return ApiResponse.error(msgError);

        }

        try {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            String parkingId = userDetails.getParkingId();


            ClientDTO client = clientService.createClient(clientDTO, parkingId);

            return ApiResponse.success(client);

        }catch (RuntimeException e){
            return  ApiResponse.error(e.getMessage());
        }catch (Exception e) {
            return  ApiResponse.serverError("Error creando el cliente");
        }

    }

    // get client by id
    @GetMapping("/clients/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getClientById (@PathVariable String id){

        try{

            ClientDTO client = clientService.getClient(id);

            return ApiResponse.success(client);


        }catch (RuntimeException e){

            return  ApiResponse.error(e.getMessage());
        }


    }

    // update client
    @PutMapping("/clients/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clientUpdate(@Valid @RequestBody ClientDTO clientDTO,
                                                            BindingResult result) {

        if (result.hasErrors()) {

            String msgError = result.getFieldErrors()
                    .stream()
                    .map(e -> e.getDefaultMessage())
                    .findFirst()
                    .orElse("Error de validacion");

            return ApiResponse.error(msgError);

        }

        try{
            ClientDTO updated = clientService.updateClient(clientDTO);
            return ApiResponse.success(updated);

        }catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }catch (Exception e){
            return ApiResponse.serverError("Error actualizando el cliente");
        }

    }

    // enable- disable client

    @PatchMapping("/clients/toggle/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleActive(@PathVariable String id) {


        try{

            clientService.toggleActive(id);
            return ApiResponse.message("Estado del cliente actualizado");
        }catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }

    }




}
