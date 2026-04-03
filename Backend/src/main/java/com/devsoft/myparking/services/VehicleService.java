package com.devsoft.myparking.services;

import com.devsoft.myparking.dtos.VehicleDTO;

import java.util.List;

public interface VehicleService {


    VehicleDTO createVehicle(VehicleDTO vehicleDTO, String parkingId);

    VehicleDTO getVehicle(String vehicleId);

    VehicleDTO getVehicleByPlate(String plate);

    List<VehicleDTO> getVehicleByParking(String parkingId);

    List<VehicleDTO> getVehicleByClient(String clientId);

    VehicleDTO updateVehicle(VehicleDTO vehicleDTO);

    void toggleActive(String vehicleId);




}
