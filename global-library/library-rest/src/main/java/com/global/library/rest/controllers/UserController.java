package com.global.library.rest.controllers;

import com.global.library.api.dto.UserDto;
import com.global.library.api.dto.UserDtoMyAccount;
import com.global.library.api.dto.UserDtoPasswordChange;
import com.global.library.api.services.IUserService;
import com.global.library.service.utils.LogoFileUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping()
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String createAccount(@ModelAttribute("user") UserDto user) {
        return "mainCreateNewAccount";
    }

    @PostMapping("/new")
    public String createUserAccount(@ModelAttribute("user") @Valid UserDto user,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "mainCreateNewAccount";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Passwords don't match");
            return "mainCreateNewAccount";
        }
        if (this.userService.isUserExist(user.getEmail())) {
            model.addAttribute("emailError", "Email already exists");
            return "mainCreateNewAccount";
        }
        userService.createUser(user);
        return "redirect:/";
    }

    @GetMapping("/new/privilege")
    public String createPrivilegeAccount(@ModelAttribute("user") UserDto user) {
        return "mainCreateStudentProfessorAccount";
    }

    @PostMapping("/new/privilege")
    public String createPrivilegeAccount(@ModelAttribute("user") @Valid UserDto user,
                                         BindingResult bindingResult,
                                         Model model) {
        if (bindingResult.hasErrors()) {
            return "mainCreateStudentProfessorAccount";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Passwords don't match");
            return "mainCreateStudentProfessorAccount";
        }
        if (this.userService.isUserExist(user.getEmail())) {
            model.addAttribute("emailError", "Email already exists");
            return "mainCreateStudentProfessorAccount";
        }
        userService.createUser(user);
        return "redirect:/";
    }

    @GetMapping("/user/myaccount")
    public String editUser(Principal principal, Model model) {
        model.addAttribute("isLogoExist", LogoFileUploader.isLogoExist(principal.getName()));
        model.addAttribute("user", this.userService.getUserByEmail(principal.getName()));
        return "userMyAccount";
    }

    @PutMapping("/user/myaccount")
    public String updateUser(Principal principal,
                             @ModelAttribute("user") @Valid UserDtoMyAccount user,
                             Model model,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "userMyAccount";
        }
        if (this.userService.isUserExist(user.getEmail()) && !principal.getName().equals(user.getEmail())) {
            model.addAttribute("emailError", "Email already exists");
            return "userMyAccount";
        }
        this.userService.updateUser(principal, user, file);
        return "redirect:/user/myaccount";


    }

    @GetMapping("/user/changepass")
    public String changePassword(Principal principal, Model model) {
        model.addAttribute("user", this.userService.getUserByEmail(principal.getName()));
        return "userChangePassword";
    }

    @PatchMapping("/user/changepass")
    public String changePassword(Principal principal, @ModelAttribute("user") @Valid UserDtoPasswordChange userDtoPasswordChange, Model model) {
        if (this.userService.isPasswordMatches(principal, userDtoPasswordChange)) {
            this.userService.updateUserPassword(principal, userDtoPasswordChange);
            return "redirect:/user/myaccount";
        }
        model.addAttribute("confirmPasswordError", "Current password is incorrect");
        return "userChangePassword";
    }

    private String createAccount(UserDto user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "mainCreateNewAccount";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Passwords don't match");
            return "mainCreateNewAccount";
        }
        if (this.userService.isUserExist(user.getEmail())) {
            model.addAttribute("emailError", "Email already exists");
            return "mainCreateNewAccount";
        }
        userService.createUser(user);

       return  "redirect:/";
    }

}
