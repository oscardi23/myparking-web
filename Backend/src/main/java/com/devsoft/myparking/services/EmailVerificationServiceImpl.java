package com.devsoft.myparking.services;

import com.devsoft.myparking.models.EmailVerification;
import com.devsoft.myparking.repository.EmailVerificationRepository;
import com.devsoft.myparking.util.CodeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor

public class EmailVerificationServiceImpl implements EmailVerificationService{


    private final EmailVerificationRepository repository;

    private final EmailService emailService;

    private final CodeGenerator codeGenerator;

    @Override
    public void generateAndSenCode(String email) {

    String code = codeGenerator.generateCode();

    repository.deleteByEmail(email);

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .code(code)
                .expiration(Instant.now().plusSeconds(600))
                .verified(false)
                .build();


        repository.save(emailVerification);

        emailService.sendVerificationCode(email, code);

    }

    @Override
    public boolean verifyCode(String email, String code) {


        return repository.findByEmail(email)
                .filter( v -> v.getCode().equals(code))
                .filter(v -> v.getExpiration().isAfter(Instant.now()))

                .map(v -> {
                    v.setVerified(true);
                    repository.save(v);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean isEmailVerified(String email) {
        return repository.findByEmail(email)
                .map(EmailVerification::isVerified)
                .orElse(false);
    }

    @Override
    public void deleteByEmail(String email) {

        repository.deleteByEmail(email);

    }
}
