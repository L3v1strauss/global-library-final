package com.global.library.rest.controllers;

import com.global.library.api.services.IBookService;
import com.global.library.api.services.IRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping()
public class RequestController {

    private final IRequestService requestService;

    public RequestController(IRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/books/book/request/crfor/{isbn}")
    public String createRequest() {
        return "bookpage";
    }

    @PostMapping("/books/book/request/crfor/{isbn}")
    public String createRequest(@PathVariable("isbn") String isbn, Principal principal) {
        this.requestService.createRequest(isbn, principal.getName());
        return "redirect:/books/book?isbn={isbn}";
    }
}
