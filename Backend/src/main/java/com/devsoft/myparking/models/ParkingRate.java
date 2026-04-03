package com.devsoft.myparking.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ParkingRate {

    private BigDecimal minimumRate;
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    private BigDecimal monthlyRate;



}
