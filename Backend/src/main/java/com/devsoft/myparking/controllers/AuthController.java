package com.devsoft.myparking.controllers;


import com.devsoft.myparking.dtos.UserRegisterDTO;
import com.devsoft.myparking.services.EmailVerificationService;
import com.devsoft.myparking.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final EmailVerificationService emailVerificationService;
    private final UserService userService;




    // page login
    @GetMapping("/login")
    public String showLogin(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model){
        if(error != null) {

            model.addAttribute("error", "Credenciales incorrectas");
        }

        if (logout != null) {
            model.addAttribute("msg", "Sesion cerrada exitosamente");
        }

        return ("/login");

    }

    // show forgot password
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(){
        return "/forgot_password";
    }



    // password request forgot

    @PostMapping("/forgot-password")
    @ResponseBody
    public boolean forgotPassword(@RequestParam String email) {
        if(!userService.existByEmail(email)) return false;
        emailVerificationService.generateAndSenCode(email);
        return true;
    }


    // reset password
    @PostMapping("reset-password")
    @ResponseBody
    public Map<String, Boolean> resetPassword(@RequestParam String email,
                                              @RequestParam String code,
                                              @RequestParam String newPassword) {

        if (!emailVerificationService.verifyCode(email, code)) {

            return Map.of("ok", false);
        }

        userService.resetPassword(email, newPassword);
        emailVerificationService.deleteByEmail(email);

        return Map.of("ok", true);
    }




    // page register

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        model.addAttribute("user", new UserRegisterDTO());

        return "register";

    }

    // send code

    @PostMapping("/send-code")
    @ResponseBody
    public boolean sendCode(@RequestParam String email) {

        if (userService.existByEmail(email)) {

            return false;

        }

        emailVerificationService.generateAndSenCode(email);

        return true;

    }

    // validate code

    @PostMapping("/verify-code")
    @ResponseBody
    public boolean verifyCode(@RequestParam String email,
                              @RequestParam String code
                              )
    {

        return emailVerificationService.verifyCode(email, code);


    }

    @PostMapping("/complete-register")
    @ResponseBody
    public Map<String, Boolean> completeRegister(@RequestBody UserRegisterDTO userRegister) {



        if (!emailVerificationService.isEmailVerified(userRegister.getEmail())){

            return Map.of("ok", false);

        }

        userService.registerUser(userRegister);

        emailVerificationService.deleteByEmail(userRegister.getEmail());

        return Map.of("ok", true);
    }


    @GetMapping("/error")
    public String showPageError(){

        return "/error";
    }

}
