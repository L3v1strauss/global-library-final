package com.global.library.rest.controllers;

import com.global.library.api.dto.UserDto;
import com.global.library.api.enums.RequestStatusName;
import com.global.library.api.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IUserService userService;


    @Autowired
    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public String findUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "userPage";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("statuses", RequestStatusName.values());
        model.addAttribute("users", this.userService.getAllUsers());
        return "adminAllUsers";
    }

    @GetMapping("/users/rolechange/{id}")
    public String roleChangeUsers() {
        return "";
    }

    @PatchMapping("/users/rolechange/{id}")
    public String roleChangeUsers(@PathVariable("id") long id, @ModelAttribute("users") UserDto user) {
        this.userService.roleChangeUser(id, user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/activate/{id}")
    public String activateUsers() {
        return "";
    }

    @PatchMapping("/users/activate/{id}")
    public String activateUsers(@PathVariable("id") long id, @ModelAttribute("users") UserDto user) {
        this.userService.statusChangeUser(id, user.getStatus());
        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        this.userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/search")
    public String getUsersBySearch(@RequestParam(value = "request") String request, Model model) {
        model.addAttribute("request", request);
        model.addAttribute("users", this.userService.getUsersBySearchRequest(request));
        return "adminAllUsers";
    }
}
