package com.devsoft.myparking.dtos;

import com.devsoft.myparking.models.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VehicleDTO {



    private String id;
    private String parkingId;
    private String clientId;

    @NotBlank(message = "La placa es obligatoria")
    private String plate;

    @NotNull(message = "El tipo de vehículo es obligatorio")
    private VehicleType type;

    private String brand;
    private String color;

    private boolean active;

    private String clientName;
    private String clientLastName;
    private String clientPhone;

}
