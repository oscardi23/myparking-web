package com.devsoft.myparking.repository;

import com.devsoft.myparking.dtos.UserDTO;
import com.devsoft.myparking.models.Role;
import com.devsoft.myparking.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String > {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByParkingIdAndRole(String parkingId, Role role);

}
