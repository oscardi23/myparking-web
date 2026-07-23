package com.devsoft.myparking.services;

import com.devsoft.myparking.dtos.UserDTO;
import com.devsoft.myparking.dtos.UserRegisterDTO;
import com.devsoft.myparking.models.Role;
import com.devsoft.myparking.models.User;
import com.devsoft.myparking.repository.UserRepository;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;







    @Override
    public User registerUser(UserRegisterDTO userDTO) {


        // validation email
        Optional<User> exist = userRepository.findByEmail(userDTO.getEmail());

        if (exist.isPresent()) {
            throw new RuntimeException("Correo ya se encuentra registrado");
        }

        if (!(userDTO.getPassword().equals(userDTO.getConfirmPassword()))) {

            throw new RuntimeException("Contraseñas no coinciden");
        }


        // create new user

        User newUser = new User();

        newUser.setName(userDTO.getName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());

        // encoding password
       String hashes = passwordEncoder.encode(userDTO.getPassword());
       newUser.setPassword(hashes);
       newUser.setRole(Role.ADMIN);
       newUser.setActive(true);
       newUser.setEmailVerified(true);
       newUser.setCreatedAt(LocalDateTime.now());
       newUser.setUpdateAt(LocalDateTime.now());



        return userRepository.save(newUser);
    }

    @Override
    public UserDTO findUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("No se ha encontrado un usuario con el correo ingresado " + email));

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getNumberPhone(),
                user.getNationalId(),
                user.getRole(),
                user.isActive(),
                user.getParkingId()
        ) ;
    }

    @Override
    public void delete(String email) {

        // find user
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Usuario no encontrado con el email + " + email)
        );

        userRepository.delete(user);



    }

    private UserDTO convertToDTO(User user) {

        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setLastName(user.getLastName());
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setNumberPhone(user.getNumberPhone());
        userDTO.setParkingId(user.getParkingId());

        return userDTO;
    }

    @Override
    public List<UserDTO> findAllUser() {
        return userRepository.findAll().stream().map(this::convertToDTO).toList();



    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {

        User user = userRepository.findById(userDTO.getId()).orElseThrow( () ->
                new RuntimeException("el usuario no se encuentra registrado"));



        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setNumberPhone(userDTO.getNumberPhone());
        user.setActive(userDTO.isActive());
        user.setNationalId(userDTO.getNationalId());

        User userSaved = userRepository.save(user);

      return convertToDTO(userSaved);

    }

    @Override
    public boolean existByEmail(String email) {

        return userRepository.existsByEmail(email);

    }

    @Override
    public void resetPassword(String email, String newPassword) {


        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateAt(LocalDateTime.now());

        userRepository.save(user);

    }


    // operators
    @Override
    public UserDTO registerOperators(UserRegisterDTO dto, String parkingId) {

        if (userRepository.existsByEmail(dto.getEmail())){

            throw new RuntimeException("Ya existe un usuario con ese correo");        }

        User operator = new User();

        operator.setName(dto.getName());
        operator.setLastName(dto.getLastName());
        operator.setEmail(dto.getEmail());
        operator.setActive(true);
        operator.setNumberPhone(dto.getNumberPhone());
        operator.setPassword(passwordEncoder.encode(dto.getPassword()));
        operator.setRole(Role.OPERATOR);
        operator.setParkingId(parkingId);
        operator.setEmailVerified(true);
        operator.setCreatedAt(LocalDateTime.now());
        operator.setUpdateAt(LocalDateTime.now());

        User saved = userRepository.save(operator);

        return convertToDTO(saved);



    }

    @Override
    public List<UserDTO> findOperatorByParkingId(String parkingId) {
        return userRepository.findByParkingIdAndRole(parkingId, Role.OPERATOR)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public void toggleActive(String userId) {

        User user = userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setActive(!user.isActive());
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
    }

}
