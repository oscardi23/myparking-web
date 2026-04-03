package com.devsoft.myparking.dtos;


import com.devsoft.myparking.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {

    @NotBlank(message = "el nombre es obligatorio")
    private String name;

    @NotBlank(message = "el apellido es obligatorio")
    private String lastName;

    @Email(message = "email invalido")
    @NotBlank(message = "el correo es obligatorio")
    private String email;

    @Size(min = 8, message = "Minimo 8 caracteres")
    private String password;

    @NotBlank
    private String confirmPassword;







}
