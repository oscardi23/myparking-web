package com.devsoft.myparking.repository;


import com.devsoft.myparking.models.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

    List<Client> findByParkingId(String parkingId);

    Optional<Client> findByNationalIdAndParkingId(String nationalId, String parkingId);

    boolean existsByNationalIdAndParkingId(String nationalId, String parkingId);
}
