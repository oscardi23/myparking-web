package com.devsoft.myparking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "parkings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class Parking {

    @Id
    private String id;

    private String name;
    private String address;
    private String phone;
    private String nit;

    private int totalSpotsCar;
    private int totalSpotsMotorcycle;
    private int totalSpotsBicycle;
    private int TotalSpotsTruck;

    private Map<VehicleType, ParkingRate> rates;

    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
