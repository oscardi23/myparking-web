package com.devsoft.myparking.services;


import com.devsoft.myparking.dtos.ParkingDTO;
import com.devsoft.myparking.models.Parking;
import com.devsoft.myparking.repository.ParkingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingRepository parkingRepository;
    @Override
    public ParkingDTO createParking(ParkingDTO parkingDTO) {





        if (parkingRepository.existsByNit(parkingDTO.getNit())) {

            throw  new RuntimeException("Ya existe un parqueadero con es nit");
        }


        Parking newParking = new Parking();

        newParking.setName(parkingDTO.getName());
        newParking.setAddress(parkingDTO.getAddress());
        newParking.setPhone(parkingDTO.getPhone());
        newParking.setNit(parkingDTO.getNit());

        newParking.setTotalSpotsCar(parkingDTO.getTotalSpotsCar());
        newParking.setTotalSpotsBicycle(parkingDTO.getTotalSpotsBicycle());
        newParking.setTotalSpotsMotorcycle(parkingDTO.getTotalSpotsMotorcycle());
        newParking.setTotalSpotsTruck(parkingDTO.getTotalSpotsTruck());

        newParking.setRates(parkingDTO.getRates());

        newParking.setActive(true);

        newParking.setCreatedAt(LocalDateTime.now());
        newParking.setUpdatedAt(LocalDateTime.now());

        Parking saved = parkingRepository.save(newParking);

        return  convertToDTO(saved);


    }

    @Override
    public Optional<ParkingDTO> getParking(String id) {

        return parkingRepository.findById(id)
                .map(this::convertToDTO);

    }

    @Override
    public List<ParkingDTO> getAllParkings() {
        return parkingRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public ParkingDTO updateParking(ParkingDTO parkingDTO) {

        Parking parking = parkingRepository.findById(parkingDTO.getId()).orElseThrow(
                () -> new RuntimeException("Parqueadero no encontrado"));

        parking.setName(parkingDTO.getName());
        parking.setAddress(parkingDTO.getAddress());
        parking.setPhone(parkingDTO.getPhone());
        parking.setNit(parkingDTO.getNit());

        parking.setTotalSpotsCar(parkingDTO.getTotalSpotsCar());
        parking.setTotalSpotsMotorcycle(parkingDTO.getTotalSpotsMotorcycle());
        parking.setTotalSpotsBicycle(parkingDTO.getTotalSpotsBicycle());
        parking.setTotalSpotsTruck(parkingDTO.getTotalSpotsTruck());

        parking.setRates(parkingDTO.getRates());
        parking.setUpdatedAt(LocalDateTime.now());

        Parking saved = parkingRepository.save(parking);

        return  convertToDTO(saved);

    }

    @Override
    public void toggleActive(String id) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parqueadero no encontrado"));

        parking.setActive(!parking.isActive());
        parking.setUpdatedAt(LocalDateTime.now());
        parkingRepository.save(parking);

    }


    private ParkingDTO convertToDTO(Parking parking) {


        ParkingDTO dto = new ParkingDTO();

        dto.setId(parking.getId());
        dto.setName(parking.getName());
        dto.setAddress(parking.getAddress());
        dto.setPhone(parking.getPhone());
        dto.setNit(parking.getNit());

        dto.setTotalSpotsCar(parking.getTotalSpotsCar());
        dto.setTotalSpotsMotorcycle(parking.getTotalSpotsMotorcycle());
        dto.setTotalSpotsBicycle(parking.getTotalSpotsBicycle());
        dto.setTotalSpotsTruck(parking.getTotalSpotsTruck());

        dto.setActive(parking.isActive());
        dto.setRates(parking.getRates());
        return dto;

    }

}
