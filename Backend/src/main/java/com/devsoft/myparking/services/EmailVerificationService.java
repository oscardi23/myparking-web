package com.devsoft.myparking.services;

public interface EmailVerificationService {

    void generateAndSenCode(String email);

    boolean verifyCode(String email, String code);

    boolean isEmailVerified(String email);

    void deleteByEmail(String email);

}
