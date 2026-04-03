package com.devsoft.myparking.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;


import java.time.Instant;


@Document(collection = "email_verifications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmailVerification {

    @Id
    private String id;
    private String email;
    private String code;

    @Indexed(expireAfter = "10m")
    private Instant expiration;

    private boolean verified;

    private int attempts;

}
