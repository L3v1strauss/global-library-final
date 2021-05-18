package com.global.library.rest.controllers;

import com.global.library.api.dto.BookDto;
import com.global.library.api.dto.GenreDto;
import com.global.library.api.services.IGenreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminGenreController {

    private IGenreService genreService;

    public AdminGenreController(IGenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/addgenre")
    public String addBook(@ModelAttribute("genre") BookDto book) {
        return "adminAddGenre";
    }

    @PostMapping("/addgenre")
    public String addBook(@ModelAttribute("genre") GenreDto genreDto, Model model) {
        if (this.genreService.isGenreExist(genreDto.getName())) {
            model.addAttribute("genreExistError", "This genre already exist");
            return "adminAddGenre";
        }
        this.genreService.addGenre(genreDto);
        return "redirect:/admin/genres";
    }

    @GetMapping("/genres")
    public String getBooks(Model model) {
        model.addAttribute("genres", this.genreService.getAllGenresOrderByName());
        return "adminAllGenres";
    }

    @GetMapping("/genres/{id}")
    public String deleteBook(@PathVariable("id") long id, Model model) {
        model.addAttribute("book", this.genreService.getGenreById(id));
        return "";
    }

    @DeleteMapping("/genres/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        this.genreService.deleteGenre(id);
        return "redirect:/admin/books";
    }
}
