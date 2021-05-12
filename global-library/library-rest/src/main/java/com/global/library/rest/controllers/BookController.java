package com.global.library.rest.controllers;

import com.global.library.api.dto.BookDto;
import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.api.dto.RatingDto;
import com.global.library.api.services.IBookService;
import com.global.library.api.services.IRatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        model.addAttribute("averageRating", this.ratingService.getAverageRatingForBook(isbn));
        model.addAttribute("ratings", this.ratingService.getRatingsOrderByDateOfPOst(isbn));
        model.addAttribute("book", this.bookService.getBookByIsbn(isbn));
        return "bookPage";
    }

    @GetMapping("/books")
    public String getBooks(@ModelAttribute("genres") GenreDtoQueryNames queryGenreNames,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "size", defaultValue = "5") int pageSize,
                           Model model) {
        List<BookDto> booksPerPage = this.bookService.getBooksWithPagination(pageNumber, pageSize);
        List<BookDto> allBooks = this.bookService.getBooks();
        model.addAttribute("request", "");
        model.addAttribute("books", booksPerPage);
        model.addAttribute("bookPage", this.bookService.getPageBookDto(booksPerPage, allBooks, pageNumber, pageSize));
        int totalPages =  this.bookService.getPageBookDto(booksPerPage, allBooks, pageNumber, pageSize).getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "bookAllBooks";
    }

    @GetMapping("/books/search")
    public String getBooksBySearch(@ModelAttribute("genres") GenreDtoQueryNames queryGenreNames,
                                   @RequestParam(value = "request") String request,
                                   @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                   @RequestParam(value = "size", defaultValue = "5") int pageSize,
                                   Model model) {
        List<BookDto> booksPerPage = this.bookService.getBooksBySearchRequestWithPagination(request, pageNumber, pageSize);
        List<BookDto> allBooks = this.bookService.getBooksBySearchRequest(request);
        model.addAttribute("request", request);
        model.addAttribute("books", booksPerPage);
        model.addAttribute("bookPage", this.bookService.getPageBookDto(booksPerPage, allBooks, pageNumber, pageSize));
        int totalPages =  this.bookService.getPageBookDto(booksPerPage, allBooks, pageNumber, pageSize).getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "bookAllBooksSearchResult";
    }

    @GetMapping("/books/filter/genre")
    public String getBooksByGenre(@ModelAttribute("genres") GenreDtoQueryNames queryGenreNames,
                                  Model model) {
        model.addAttribute("request", "");
        model.addAttribute("books", this.bookService.getBooksByQueryNames(queryGenreNames));
        return "bookAllBooksCheckBoxByGenreResult";
    }


}
