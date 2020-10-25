package actorlibrary.Models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @Column(nullable = false)
    public String title;
    @Column
    public String genre;
    @Column
    public String releaseYear;
    @JsonGetter("actors")
    public List<String> actors() {
        return actors.stream()
                .map(actor -> {
                    return "/api/v1/actor/" + actor.id;
                }).collect(Collectors.toList());
    }

    @ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.DETACH)
    @JoinTable(
            name = "Movie_Actor",
            joinColumns = { @JoinColumn(name = "movie_id")},
            inverseJoinColumns = { @JoinColumn(name = "actor_id")}
    )
    public Set<Actor> actors = new HashSet<>();
}
