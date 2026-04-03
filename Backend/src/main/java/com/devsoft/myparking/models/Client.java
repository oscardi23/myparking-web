package com.devsoft.myparking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "clients")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Client {

    @Id
    private String id;

    private String parkingId;

    private String name;

    private String lastName;

    @Indexed
    private String nationalId;

    private String phone;

    private String email;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

}
