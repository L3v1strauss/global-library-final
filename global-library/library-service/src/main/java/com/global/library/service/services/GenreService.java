package com.global.library.service.services;

import com.global.library.api.dao.IGenreDao;
import com.global.library.api.dto.GenreDto;
import com.global.library.api.mappers.GenreMapper;
import com.global.library.api.services.IGenreService;
import com.global.library.entity.Genre;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenreService implements IGenreService {

    private IGenreDao genreDao;

    public GenreService(IGenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    @Transactional
    public GenreDto getGenreById(long id) {
        return GenreMapper.mapGenreDto(this.genreDao.getGenreById(id));
    }

    @Override
    @Transactional
    public boolean isGenreExist(String name) {
        return this.genreDao.isGenreExistByName(name);
    }

    @Override
    @Transactional
    public void addGenre(GenreDto genreDto) {
        Genre genre = Genre.builder().name(genreDto.getName()).build();
        this.genreDao.create(genre);

    }

    @Override
    @Transactional
    public List<GenreDto> getAllGenresOrderByName() {
        return GenreMapper.mapAllGenresDto(this.genreDao.findAllGenresOrderByName());
    }

    @Override
    @Transactional
    public void deleteGenre(long id) {
        this.genreDao.delete(this.genreDao.getGenreById(id));
    }

}
