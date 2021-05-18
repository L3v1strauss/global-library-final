package com.global.library.rest.controllers;

import com.global.library.api.dto.BookDto;
import com.global.library.api.services.IBookService;
import com.global.library.api.services.IGenreService;
import com.global.library.rest.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminBookController {

    private final IBookService bookService;
    private final IGenreService genreService;

    @Autowired
    public AdminBookController(IBookService bookService, IGenreService genreService) {
        this.bookService = bookService;
        this.genreService = genreService;
    }

    @GetMapping("/addbook")
    public String addBook(@ModelAttribute("book") BookDto book, Model model) {
        model.addAttribute("genres", this.genreService.getAllGenresOrderByName());
        return "adminAddBook";
    }

    @PostMapping("/addbook")
    public String addBook( @ModelAttribute("book") @Valid  BookDto book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("chooseGenreError", "Please choose genre");
            model.addAttribute("genres", this.genreService.getAllGenresOrderByName());
            return "adminAddBook";
        }
        this.bookService.addBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/{id}")
    public String deleteBook(@PathVariable("id") long id, Model model) {
        model.addAttribute("book", this.bookService.getBookById(id));
        return "bookPage";
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        this.bookService.deleteBook(id);
        return "redirect:/admin/books";
    }

    @GetMapping("/books")
    public String getBooks(Model model,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "size", defaultValue = "5") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<BookDto> allBooks = this.bookService.getAllBooksOrderByDateOfCreation();
        Page<BookDto> page = PaginationUtil.getPageBookDto(allBooks, pageable);
        List<BookDto> booksPerPage = page.getContent();
        model.addAttribute("bookPage", page);
        model.addAttribute("books", booksPerPage);
        model.addAttribute("pageNumbers", PaginationUtil.getListOfPageNumbers(page));
        return "adminAllBooks";
    }

    @GetMapping("/books/search")
    public String getBooksBySearch(@RequestParam(value = "request") String request,
                                   @RequestParam(value = "genre") String genre,
                                   @RequestParam(value = "orderBy", required = false) String orderBy,
                                   @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                   @RequestParam(value = "size", defaultValue = "5") int pageSize,
                                   Model model) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<BookDto> allBooks = this.bookService.getAllBooksBySearchAndOrderByRequestWithAvgRating(request, orderBy, genre);
        Page<BookDto> page = PaginationUtil.getPageBookDto(allBooks, pageable);
        List<BookDto> booksPerPage = page.getContent();
        model.addAttribute("request", request);
        model.addAttribute("bookPage", page);
        model.addAttribute("books", booksPerPage);
        model.addAttribute("pageNumbers", PaginationUtil.getListOfPageNumbers(page));
        return "adminAllbooks";
    }


}
