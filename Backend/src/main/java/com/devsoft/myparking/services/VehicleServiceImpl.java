package com.devsoft.myparking.services;


import com.devsoft.myparking.dtos.ClientDTO;
import com.devsoft.myparking.dtos.VehicleDTO;
import com.devsoft.myparking.models.Vehicle;
import com.devsoft.myparking.repository.ClientRepository;
import com.devsoft.myparking.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService{


    private final VehicleRepository vehicleRepository;
    private final ClientService clientService;


    @Override
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO, String parkingId) {

        if (vehicleRepository.existsByPlate(vehicleDTO.getPlate().toUpperCase())){

            throw new RuntimeException("Ya existe un vehiculo con ese numero de placa");
        }


        Vehicle vehicle = new Vehicle();

        vehicle.setParkingId(parkingId);
        vehicle.setClientId(vehicleDTO.getClientId());
        vehicle.setPlate(vehicleDTO.getPlate().toUpperCase());
        vehicle.setType(vehicleDTO.getType());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setActive(true);
        vehicle.setCreatedAt(LocalDateTime.now());
        vehicle.setUpdateAt(LocalDateTime.now());


        return  convertToDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleDTO getVehicle(String vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));
    }

    @Override
    public VehicleDTO getVehicleByPlate(String plate) {
        return vehicleRepository.findByPlate(plate)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado con numero de placa: " + plate));
    }

    @Override
    public List<VehicleDTO> getVehicleByParking(String parkingId) {
        return vehicleRepository.findByParkingId(parkingId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<VehicleDTO> getVehicleByClient(String clientId) {
        return vehicleRepository.findByClientId(clientId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public VehicleDTO updateVehicle(VehicleDTO vehicleDTO) {

        Vehicle vehicle = vehicleRepository.findById(vehicleDTO.getId()).orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        vehicle.setPlate(vehicleDTO.getPlate().toUpperCase());
        vehicle.setType(vehicleDTO.getType());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setUpdateAt(LocalDateTime.now());

        return convertToDTO(vehicleRepository.save(vehicle));



    }

    @Override
    public void toggleActive(String vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        vehicle.setActive(!vehicle.isActive());
        vehicle.setUpdateAt(LocalDateTime.now());

        vehicleRepository.save(vehicle);

    }

    private VehicleDTO convertToDTO(Vehicle vehicle){


        VehicleDTO dto = new VehicleDTO();

        dto.setId(vehicle.getId());
        dto.setParkingId(vehicle.getParkingId());
        dto.setClientId(vehicle.getClientId());
        dto.setPlate(vehicle.getPlate());
        dto.setType(vehicle.getType());
        dto.setBrand(vehicle.getBrand());
        dto.setColor(vehicle.getColor());
        dto.setActive(vehicle.isActive());


        try{

            ClientDTO client = clientService.getClient(vehicle.getClientId());
            dto.setClientName(client.getName());
            dto.setClientLastName(client.getLastName());
            dto.setClientPhone(client.getPhone());

        }catch (RuntimeException e){


        }

        return dto;
    }
}
