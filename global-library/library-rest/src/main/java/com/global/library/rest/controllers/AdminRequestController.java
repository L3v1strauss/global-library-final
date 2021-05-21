package com.global.library.rest.controllers;

import com.global.library.api.enums.RequestStatusName;
import com.global.library.api.services.IRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminRequestController {

    private final IRequestService requestService;

    public AdminRequestController(IRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requests")
    public String getAdminPage(@RequestParam(value = "status", required = false) String status,Model model) {
        model.addAttribute("statuses", RequestStatusName.values());
        model.addAttribute("currentStatus", status);
        model.addAttribute("requests", this.requestService.getAllRequests(status));
        return "adminMainPage";
    }

    @GetMapping("/request/processed/{id}")
    public String processRequest() {
        return "";
    }

    @PatchMapping("/request/processed/{id}")
    public String processRequest(@PathVariable("id") long id) {
        this.requestService.processRequest(id);
        return "redirect:/admin/requests?status=confirmed";
    }

    @GetMapping("/requests/returned/{id}")
    public String returnedRequest() {
        return "";
    }

    @PatchMapping("/request/returned/{id}")
    public String returnedRequest(@PathVariable("id") long id) {
        this.requestService.returnRequest(id);
        return "redirect:/admin/requests?status=processed";

    }


    @GetMapping("/requests/s")
    public String getRequestBySearch(@RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "search", required = false) String search,
                                     Model model, HttpServletRequest request) {
        if (status == null) {
            String referer = request.getHeader("Referer");
            status = referer.substring(referer.indexOf("=") + 1);
        }
        model.addAttribute("statuses", RequestStatusName.values());
        model.addAttribute("currentStatus", status);
        model.addAttribute("searchRequest", search);
        model.addAttribute("requests", this.requestService.getAllRequestsBySearch(status, search));
        return "adminMainPage";
    }

}
