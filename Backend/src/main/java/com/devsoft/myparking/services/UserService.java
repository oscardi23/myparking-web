package com.devsoft.myparking.services;

import com.devsoft.myparking.dtos.UserDTO;
import com.devsoft.myparking.dtos.UserRegisterDTO;
import com.devsoft.myparking.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(UserRegisterDTO userDTO);
    UserDTO findUserByEmail(String email);

    void delete(String email);

    List<UserDTO> findAllUser();

    UserDTO updateUser(UserDTO userDTO);


    boolean existByEmail (String email);

    void resetPassword(String email, String newPassword);


}
