package com.devsoft.myparking.controllers;


import com.devsoft.myparking.dtos.UserRegisterDTO;
import com.devsoft.myparking.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@AllArgsConstructor
public class RegisterController {

    private final UserService userService;


    @GetMapping("/form")
    public String showRegisterForm(Model model) {

        model.addAttribute("user", new UserRegisterDTO());

        return "register";

    }

    @PostMapping("/register")

    public String processRegister(@Valid UserRegisterDTO user, BindingResult result, Model model) {

        if (!user.getPassword().equals(user.getConfirmPassword())){

            result.rejectValue("confirmPassword",
                    "error.user",
                    "la contraseñas no coinciden");

        }

        if (result.hasErrors()){
            return "register";
        }


        try{
            userService.registerUser(user);
        }catch (RuntimeException e){

            model.addAttribute("error", e.getMessage());
            return "register";
        }

        return "redirect:/login";
    }

}
