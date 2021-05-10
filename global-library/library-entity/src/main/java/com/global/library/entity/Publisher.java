package com.global.library.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "publisher")
@SuperBuilder
@EqualsAndHashCode
public class Publisher extends AEntity<Long>{

    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},orphanRemoval = true,
            mappedBy = "publisher",
            fetch = FetchType.LAZY)
    private List<Book> books;

    public List<Book> getBooks() {
        if (books == null) {
            books = new ArrayList<>();
        }
        return books;
    }
}
