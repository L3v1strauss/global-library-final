package com.global.library.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Entity
@Table(name = "book")
@EqualsAndHashCode
public class Book extends AEntity<Long> {

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private String picture;

    @Column(name = "description")
    private String description;

    @Column(name = "date_creation")
    private LocalDateTime dateOfCreation;

    @Column(name = "publishing_year")
    private LocalDate yearOfPublishing;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    private Set<Author> authors;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    private Request request;

    public Set<Author> getAuthors() {
        if (authors == null){
            authors = new HashSet<>();
        }
        return authors;
    }
}
