package com.global.library.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "genre")
@SuperBuilder
@EqualsAndHashCode
public class Genre extends AEntity<Long>{

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},orphanRemoval = true,
            mappedBy = "genre",
            fetch = FetchType.LAZY)
    private List<Book> books;

    public void addBookToGenre(Book book) {
        if (books == null) {
            books = new ArrayList<>();
        }
        books.add(book);
        book.setGenre(this);
    }
}
