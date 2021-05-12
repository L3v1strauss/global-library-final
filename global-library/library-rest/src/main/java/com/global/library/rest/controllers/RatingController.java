package com.global.library.rest.controllers;

import com.global.library.api.dto.RatingDto;
import com.global.library.api.services.IBookService;
import com.global.library.api.services.IRatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
@RequestMapping()
public class RatingController {

    private final IRatingService ratingService;
    private final IBookService bookService;

    public RatingController(IRatingService ratingService, IBookService bookService) {
        this.ratingService = ratingService;
        this.bookService = bookService;
    }

    @PostMapping("/books/book")
    public RedirectView addRating(@ModelAttribute("rating") RatingDto ratingDto,
                            @RequestParam(value = "isbn") String isbn,
                            Model model,
                            Principal principal,
                            RedirectAttributes attributes){
        model.addAttribute("isRatingExistFromCurrentUser", this.ratingService.isRatingExistFromCurrentUser(isbn, principal.getName()));
        model.addAttribute("book", this.bookService.getBookByIsbn(isbn));
        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
        attributes.addAttribute("isbn", isbn);
        this.ratingService.addRating(principal, isbn, ratingDto);
        return new RedirectView("/books/book");
    }
}
