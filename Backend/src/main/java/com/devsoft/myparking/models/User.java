package com.devsoft.myparking.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String name;
    private String lastName;

    private String nationalId;

    private String numberPhone;
    @Indexed(unique = true)
    private String email;
    private String password;

    private Role role;
    private boolean active;
    private boolean emailVerified;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    private String parkingId;





}
