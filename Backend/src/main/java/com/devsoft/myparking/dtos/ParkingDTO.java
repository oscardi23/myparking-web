package com.devsoft.myparking.dtos;


import com.devsoft.myparking.models.ParkingRate;
import com.devsoft.myparking.models.VehicleType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ParkingDTO {

    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La dirección es obligatorio")
    private String address;

    @NotBlank(message = "El numero del NIT es obligatorio")
    private String nit;

    @NotBlank(message = "El numero de telefono es obligatorio")
    private String phone;

    @Min(value = 0, message = "los cupos no pueden ser negativos")
    private int totalSpotsCar;

    @Min(value = 0, message = "los cupos no pueden ser negativos")
    private int totalSpotsMotorcycle;

    @Min(value = 0, message = "los cupos no pueden ser negativos")
    private int totalSpotsTruck;

    @Min(value = 0, message = "los cupos no pueden ser negativos")
    private int totalSpotsBicycle;

    @NotNull(message = "Las tarifas son obligatorias")
    private Map<VehicleType, ParkingRate> rates;

    private boolean active;

}
