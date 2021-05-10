package com.global.library.rest.controllers;

import com.global.library.api.dto.UserDtoEmailForgotPassword;
import com.global.library.api.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping()
public class MainController {

    private final IUserService userService;

    @Autowired
    public MainController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getFirstPage () {
        return "mainIndex";
    }

    @GetMapping("/login")
    public String login() {
        return "mainLogin";
    }

    @GetMapping("/login/forgotpassword")
    public String forgotPassword(Model model) {
        model.addAttribute("UserDtoEmail", new UserDtoEmailForgotPassword());
        return "mainLoginForgotPassword";
    }

    @PatchMapping("/login/forgotpassword")
    public String forgotPassword(@ModelAttribute("UserDtoEmail")@Valid UserDtoEmailForgotPassword userDtoEmail, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "mainLoginForgotPassword";
        }
        if (!this.userService.isUserExist(userDtoEmail.getEmail())) {
            model.addAttribute("emailForgotPasswordError", "Email doesn't exist");
            return "mainLoginForgotPassword";
        }
        userService.changeForgotPassword(userDtoEmail.getEmail());
        return "redirect:/login";
    }

}
