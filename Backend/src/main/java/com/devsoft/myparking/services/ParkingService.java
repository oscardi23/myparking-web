package com.devsoft.myparking.services;

import com.devsoft.myparking.dtos.ParkingDTO;

import java.util.List;
import java.util.Optional;

public interface ParkingService {

    ParkingDTO createParking(ParkingDTO parkingDTO);

   Optional<ParkingDTO> getParking(String id);

    List<ParkingDTO> getAllParkings();

    ParkingDTO updateParking(ParkingDTO parkingDTO);

    void toggleActive(String id);
}
