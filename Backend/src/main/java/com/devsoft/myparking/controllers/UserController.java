package com.devsoft.myparking.controllers;


import com.devsoft.myparking.dtos.UserDTO;
import com.devsoft.myparking.dtos.UserRegisterDTO;
import com.devsoft.myparking.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // list users
    @GetMapping
    public String listUser(Model model) {

        model.addAttribute("users", userService.findAllUser());
        return "users/list";
    }





    // show form

    @GetMapping("/edit/{email}")
    public String showFormEdition(@PathVariable String email, Model model) {
        UserDTO userDTO = userService.findUserByEmail(email);
        model.addAttribute("user", userDTO);
        return "user/edit";
    }


    // update user

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") UserDTO dto){
        userService.updateUser(dto);
        return "redirect:/users";
    }

    // delete user
    @GetMapping("/delete/{email}")
    public String deleteUser(@PathVariable String email){

        userService.delete(email);
        return "redirect:/users";
    }
}
