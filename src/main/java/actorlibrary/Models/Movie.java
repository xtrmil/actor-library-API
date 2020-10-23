package actorlibrary.Models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDate;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "movieId")
public class Movie extends Actor{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer movieId;

    @Column
    public String title;

    @Column
    public String year;

    @Column
    public String job;

    @Column
    public URL imdbURL;
}
