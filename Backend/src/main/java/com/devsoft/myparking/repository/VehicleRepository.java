package com.devsoft.myparking.repository;

import com.devsoft.myparking.models.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {

    List<Vehicle> findByParkingId(String parkingId);

    List<Vehicle> findByClientId(String vehicleId);

    Optional<Vehicle> findByPlate(String plate);

    boolean existsByPlate(String plate);

}
