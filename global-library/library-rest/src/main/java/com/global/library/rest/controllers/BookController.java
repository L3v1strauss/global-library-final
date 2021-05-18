package com.global.library.rest.controllers;

import com.global.library.api.dto.BookDto;
import com.global.library.api.dto.RatingDto;
import com.global.library.api.dto.SearchAndOrderDto;
import com.global.library.api.services.IBookService;
import com.global.library.api.services.IGenreService;
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
    private final IGenreService genreService;

    public BookController(IBookService bookService, IRatingService ratingService, IGenreService genreService) {
        this.bookService = bookService;
        this.ratingService = ratingService;
        this.genreService = genreService;
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
                           @RequestParam(value = "genre", required = false) String genre,
                           Model model) {
        List<BookDto> allBooks = this.bookService.getAllBooksWithAvgRating();
        if (genre != null) {
            allBooks = this.bookService.getAllBooksByGenre(genre);
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<BookDto> page = PaginationUtil.getPageBookDto(allBooks, pageable);
        List<BookDto> booksPerPage = page.getContent();
        model.addAttribute("searchAndOrder", new SearchAndOrderDto());
        model.addAttribute("genres", this.genreService.getAllGenresOrderByName());
        model.addAttribute("genreRequest", genre);
        model.addAttribute("bookPage", page);
        model.addAttribute("books", booksPerPage);
        model.addAttribute("pageNumbers", PaginationUtil.getListOfPageNumbers(page));
        return "bookAllBooks";
    }

    @PostMapping("/books")
    public String getBooksWithSearchOrOrderBy(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                                              @ModelAttribute("searchAndOrder") SearchAndOrderDto searchAndOrderDto,
                                              @RequestParam(value = "genre", required = false) String genre,
                                              Model model) {
        List<BookDto> allBooks = this.bookService.getAllBooksOrderByRequestWithAvgRating(searchAndOrderDto.getOrderByRequest(), genre);
        if (searchAndOrderDto.getSearchRequest() != null) {
            allBooks = this.bookService.getAllBooksBySearchAndOrderByRequestWithAvgRating(genre,
                    searchAndOrderDto.getSearchRequest(),
                    searchAndOrderDto.getOrderByRequest());
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<BookDto> page = PaginationUtil.getPageBookDto(allBooks, pageable);
        List<BookDto> booksPerPage = page.getContent();
        model.addAttribute("genres", this.genreService.getAllGenresOrderByName());
        model.addAttribute("genreRequest", genre);
        model.addAttribute("bookPage", page);
        model.addAttribute("books", booksPerPage);
        model.addAttribute("pageNumbers", PaginationUtil.getListOfPageNumbers(page));
        return "bookAllBooks";
    }
}
