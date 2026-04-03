package com.devsoft.myparking.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "vehicles")

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor


public class Vehicle {

    @Id
    private String id;

    private String parkingId;

    private String clientId;

    @Indexed(unique = true)
    private String plate;

    private VehicleType type;

    private String brand;

    private String color;

    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;


}
