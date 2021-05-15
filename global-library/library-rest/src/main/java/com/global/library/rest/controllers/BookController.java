package com.global.library.rest.controllers;

import com.global.library.api.dto.BookDto;
import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.api.dto.RatingDto;
import com.global.library.api.services.IBookService;
import com.global.library.api.services.IRatingService;
import com.global.library.rest.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping()
public class BookController {

    private final IBookService bookService;
    private final IRatingService ratingService;

    public BookController(IBookService bookService, IRatingService ratingService) {
        this.bookService = bookService;
        this.ratingService = ratingService;
    }

    @GetMapping("/books/book")
    public String findBook(@RequestParam(value = "isbn") String isbn, Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("isRatingExistFromCurrentUser", this.ratingService.isRatingExistFromCurrentUser(isbn, principal.getName()));
        } else {
            model.addAttribute("isRatingExistFromCurrentUser", false);
        }
        model.addAttribute("rating", new RatingDto());
        model.addAttribute("book", this.bookService.getBookByIsbn(isbn));
        return "bookPage";
    }

    @GetMapping("/books")
    public String getBooks(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "size", defaultValue = "5") int pageSize,
                           @RequestParam(value = "orderBy", required = false) String orderBy,
                           @RequestParam(value = "search", required = false) String search,
                           Model model) {
        List<BookDto> allBooks = this.bookService.getAllBooksOrderByRequestWithAvgRating(orderBy);
        if (search != null) {
            allBooks = this.bookService.getAllBooksBySearchAndOrderByRequestWithAvgRating(search, orderBy);
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<BookDto> page = PaginationUtil.getPageBookDto(allBooks, pageable);
        List<BookDto> booksPerPage = page.getContent();
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("search", search);
        model.addAttribute("bookPage", page);
        model.addAttribute("books", booksPerPage);
        model.addAttribute("genres", new GenreDtoQueryNames());
        model.addAttribute("pageNumbers", PaginationUtil.getListOfPageNumbers(page));
        return "bookAllBooks";
    }

    @PostMapping("/books")
    public String getBooksByGenre(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "size", defaultValue = "5") int pageSize,
                                  @ModelAttribute("genres") GenreDtoQueryNames queryGenreNames,
                                  Model model) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<BookDto> allBooks = this.bookService.getAllBooksByQueryNamesWithAvgRating(queryGenreNames);
        if (allBooks.isEmpty()) {
            allBooks = this.bookService.getAllBooksWithAvgRating();
        }
        Page<BookDto> page = PaginationUtil.getPageBookDto(allBooks, pageable);
        List<BookDto> booksPerPage = page.getContent();
        model.addAttribute("bookPage", page);
        model.addAttribute("books", booksPerPage);
        model.addAttribute("pageNumbers", PaginationUtil.getListOfPageNumbers(page));
        return "bookAllbooks";
    }


}
