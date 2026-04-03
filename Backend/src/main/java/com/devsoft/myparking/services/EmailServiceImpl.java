package com.devsoft.myparking.services;

import com.devsoft.myparking.repository.EmailVerificationRepository;
import com.devsoft.myparking.util.CodeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class EmailServiceImpl  implements EmailService{

    private final JavaMailSender javaMailSender;




    @Override
    public void sendVerificationCode(String to, String code) {


        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Codigo de verificacion Myparking");
        message.setText(
                "Hola \n\n" +
                        "Tu codigo de verificacion es: " + code
                + "\n\n No lo compartas con nadie."

        );

        javaMailSender.send(message);
    }


}
