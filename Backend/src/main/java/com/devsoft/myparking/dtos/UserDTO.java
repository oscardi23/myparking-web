package com.devsoft.myparking.dtos;


import com.devsoft.myparking.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String id;
    private String name;
    private String lastName;

    private String email;

    private String numberPhone;

    private String nationalId;

    private Role role;

    private boolean active;

}
