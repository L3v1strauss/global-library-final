package com.global.library.rest.controllers;

import com.global.library.api.dto.BookDto;
import com.global.library.api.services.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
//        if(this.bookService.isBookExistByIsbn(book.getIsbn())){
//            model.addAttribute("isbnError", "Book with that ISBN already exists");
//        }
        this.bookService.addBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books")
    public String getBooks(Model model,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "size", defaultValue = "5") int pageSize) {
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
    public String getBooksBySearch(@RequestParam(value = "request") String request,
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
        return "adminAllBooksSearchResult";
    }


}
