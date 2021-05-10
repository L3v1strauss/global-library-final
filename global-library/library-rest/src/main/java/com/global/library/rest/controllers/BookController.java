package com.global.library.rest.controllers;

import com.global.library.api.dto.BookDto;
import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.api.services.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping()
public class BookController {

    private final IBookService bookService;

    @Autowired
    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/book/get")
    public String findBook(@RequestParam(value = "isbn") String isbn, Model model) {
        model.addAttribute("book", this.bookService.getBookByIsbn(isbn));
        return "bookPage";
    }

    @GetMapping("/books")
    public String getBooks(@ModelAttribute("genres") GenreDtoQueryNames queryGenreNames,
                           @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "size", defaultValue = "5") Integer pageSize,
                           Model model) {
        model.addAttribute("request", "");
        model.addAttribute("books", this.bookService.getBooksWithPagination(pageNumber, pageSize));
        Pageable page = PageRequest.of(pageNumber - 1, pageSize);
        int totalRows = this.bookService.getBooks().size();
        Page<BookDto> result = new PageImpl<>(this.bookService.getBooksWithPagination(pageNumber, pageSize),
                PageRequest.of(page.getPageNumber(), page.getPageSize()),
                this.bookService.getBooks().size());
        model.addAttribute("bookPage", result);
        int totalPages =  result.getTotalPages();
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
                                   Model model) {
        model.addAttribute("request", request);
        model.addAttribute("books", this.bookService.getBooksBySearchRequest(request));
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
