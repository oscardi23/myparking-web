package com.devsoft.myparking.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClientDTO {

    private String id;

    private String parkingID;

    @NotBlank(message = "el nombre es obligatorio")
    private String name;

    @NotBlank(message = "el apellido es obligatorio")
    private String lastName;

    @NotBlank(message = "la cedula es obligatoria")
    private String nationalId;

    @NotBlank(message = "el numero telefono es obligatorio")
    private String phone;

    private String email;

    private boolean active;
}
