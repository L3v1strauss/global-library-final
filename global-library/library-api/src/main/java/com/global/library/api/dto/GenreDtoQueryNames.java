package com.global.library.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class GenreDtoQueryNames {

    private String fiction;
    private String science;
    private String math;
    private String economy;
    private String computerScience;

    public List<String> getGenreDtoQueryNames(){
        List<String> genreNames = new ArrayList<>();
        genreNames.add(fiction);
        genreNames.add(science);
        genreNames.add(math);
        genreNames.add(economy);
        genreNames.add(computerScience);
        return genreNames;
    }
}
