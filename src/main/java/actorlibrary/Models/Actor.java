package actorlibrary.Models;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Actor {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer id;

    @Column
    public String firstname;

    @Column
    public String lastname;

    @Column
    public LocalDate dateOfBirth;

    @Column
    public URL imdbURL;

//    @JsonGetter("books")
//    public List<String> movies() {
//        return books.stream()
//                .map(movie -> {
//                    return "/book/" + book.id;
//                }).collect(Collectors.toList());
//    }

}
