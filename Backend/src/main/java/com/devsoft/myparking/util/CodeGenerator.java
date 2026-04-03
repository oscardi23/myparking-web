package com.devsoft.myparking.util;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Service
public class CodeGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public  String generateCode(){

        int code = 100000  + secureRandom.nextInt(900000);
    return String.valueOf(code);
    }
}
