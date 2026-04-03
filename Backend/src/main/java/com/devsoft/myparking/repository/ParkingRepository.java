package com.devsoft.myparking.repository;

import com.devsoft.myparking.models.Parking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingRepository extends MongoRepository<Parking, String> {

    Optional<Parking> findByNit(String nit);

    boolean existsByNit(String nit);

}
