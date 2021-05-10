package com.global.library.rest.controllers;

import com.global.library.api.dto.BookDto;
import com.global.library.api.services.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminBookController {

    private final IBookService bookService;

    @Autowired
    public AdminBookController(IBookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/addbook")
    public String addBook(@ModelAttribute("book") BookDto book) {
        return "adminAddBook";
    }

    @PostMapping("/addbook")
    public String addBook(@ModelAttribute("book") @Valid BookDto book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "adminAddBook";
        }
        this.bookService.addBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books")
    public String getBooks(Model model) {
        model.addAttribute("request", new String());
        model.addAttribute("books", this.bookService.getBooks());
        return "adminAllBooks";
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

    @GetMapping("/books/search")
    public String getBooksBySearch(@RequestParam(value = "request") String request, Model model) {
        model.addAttribute("request", request);
        model.addAttribute("books", this.bookService.getBooksBySearchRequest(request));
        return "adminAllBooksSearchResult";
    }


}
