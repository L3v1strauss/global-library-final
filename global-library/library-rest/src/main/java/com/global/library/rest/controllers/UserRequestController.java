package com.global.library.rest.controllers;

import com.global.library.api.services.IRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserRequestController {

    private final IRequestService requestService;

    public UserRequestController(IRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requests")
    public String getRequests(Principal principal, Model model) {
        model.addAttribute("requests", this.requestService.getAllCreatedRequestsFromUserByEmail(principal.getName()));
        return "userMyRequests";
    }

    @GetMapping("/requests/delete/{id}")
    public String deleteRequest() {
        return "";
    }

    @DeleteMapping("/requests/delete/{id}")
    public String deleteRequest(@PathVariable("id") long id) {
     this.requestService.deleteRequest(id);
     return "redirect:/user/requests";
    }

    @PatchMapping("/requests")
    public String confirmRequests(Principal principal) {
        this.requestService.confirmRequests(this.requestService.getAllCreatedRequestsFromUserByEmail(principal.getName()));
        return "redirect:/user/requests/confirmed";
    }

    @GetMapping("/requests/confirmed")
    public String getConfirmdedRequests(Principal principal, Model model) {
        model.addAttribute("requests", this.requestService.getAllConfirmedRequestsFromUserByEmail(principal.getName()));
        return "userMyConfirmedRequests";
    }
}
