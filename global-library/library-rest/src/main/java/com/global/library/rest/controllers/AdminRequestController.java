package com.global.library.rest.controllers;

import com.global.library.api.dto.BookDto;
import com.global.library.api.enums.RequestStatusName;
import com.global.library.api.services.IRequestService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminRequestController {

    private final IRequestService requestService;

    public AdminRequestController(IRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping()
    public String getAdminPage(Model model) {
        model.addAttribute("requests", this.requestService.getAllConfirmedRequests());
        return "adminMainPage";
    }

    @GetMapping("/requests/processed")
    public String getRequestsPage(Model model) {
        model.addAttribute("requests", this.requestService.getAllProcessedRequests());
        return "adminRequestProcessed";
    }

    @GetMapping("/request/processed/{id}")
    public String processRequest() {
        return "";
    }

    @PatchMapping("/request/processed/{id}")
    public String processRequest(@PathVariable("id") long id) {
        this.requestService.processRequest(id);
        return "redirect:/admin";
    }

    @GetMapping("/requests/returned/{id}")
    public String returnedRequest() {
        return "";
    }

    @PatchMapping("/request/returned/{id}")
    public String returnedRequest(@PathVariable("id") long id) {
        this.requestService.returnRequest(id);
        return "redirect:/admin/requests/processed";
    }


    @GetMapping("/requests/s")
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
