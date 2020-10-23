package actorlibrary.Models;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


import javax.persistence.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
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

    @JsonGetter("movies")
    public List<String> actors() {
        return movies.stream()
                .map(movie-> {
                    return "/movies/" + movie.id;
                }).collect(Collectors.toList());
    }

    @ManyToMany(mappedBy = "actors",fetch=FetchType.LAZY)
    public Set<Movie> movies = new HashSet<Movie>();

}
