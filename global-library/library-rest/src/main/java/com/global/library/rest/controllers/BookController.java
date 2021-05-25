package com.global.library.rest.controllers;

import com.global.library.api.dto.BookDto;
import com.global.library.api.dto.RatingDto;
import com.global.library.api.services.IBookService;
import com.global.library.api.services.IGenreService;
import com.global.library.api.services.IRatingService;
import com.global.library.api.services.IRequestService;
import com.global.library.service.utils.LogoFileUploader;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping()
public class BookController {

    private final IBookService bookService;
    private final IRatingService ratingService;
    private final IGenreService genreService;
    private final IRequestService requestService;

    public BookController(IBookService bookService, IRatingService ratingService, IGenreService genreService, IRequestService requestService) {
        this.bookService = bookService;
        this.ratingService = ratingService;
        this.genreService = genreService;
        this.requestService = requestService;
    }

    @GetMapping("/books/book")
    public String findBook(@RequestParam(value = "isbn") String isbn, Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("isRatingExistFromCurrentUser",
                    this.ratingService.isRatingExistFromCurrentUser(isbn, principal.getName()));
            model.addAttribute("isRequestExistForCurrentBookFromUser",
                    this.requestService.isRequestExistForCurrentBookFromUser(isbn, principal.getName()));
        } else {
            model.addAttribute("isRatingExistFromCurrentUser", false);
            model.addAttribute("isRequestExistForCurrentBookFromUser", false);
        }
        model.addAttribute("rating", new RatingDto());
        model.addAttribute("book", this.bookService.getBookByIsbn(isbn));
        model.addAttribute("logoUtil", LogoFileUploader.class);
        return "bookPage";
    }

    @GetMapping("/books")
    public String getBooks(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "size", defaultValue = "5") int pageSize,
                           Model model) {
        Page<BookDto> page = this.bookService.getAllBooksWithAvgRating(pageNumber, pageSize);
        model.addAttribute("genres", this.genreService.getAllGenresOrderByName());
        model.addAttribute("bookPage", page);
        model.addAttribute("books", page.getContent());
        model.addAttribute("pageNumbers", this.bookService.getTotalPages(page));
        return "bookAllBooks";
    }

    @GetMapping("/books/s")
    public String getBooksWithSearchOrOrderBy(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                                              @RequestParam(value = "genre", required = false) String genre,
                                              @RequestParam(value = "search", required = false) String search,
                                              @RequestParam(value = "orderBy", required = false) String orderBy,
                                              Model model) {

        Page<BookDto> page = this.bookService.getAllBooksOrderByRequestWithAvgRating(orderBy, genre, pageNumber, pageSize);
        if (!search.isEmpty()) {
            page = this.bookService.getAllBooksBySearchAndOrderByRequestWithAvgRating(genre, search, orderBy, pageNumber, pageSize);
        }
        model.addAttribute("genres", this.genreService.getAllGenresOrderByName());
        model.addAttribute("genreRequest", genre);
        model.addAttribute("searchRequest", search);
        model.addAttribute("orderByRequest", orderBy);
        model.addAttribute("bookPage", page);
        model.addAttribute("books", page.getContent());
        model.addAttribute("pageNumbers", this.bookService.getTotalPages(page));
        return "bookAllBooks";
    }
}
