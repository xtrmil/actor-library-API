package actorlibrary.Controllers;

import actorlibrary.Models.CommonResponse;
import actorlibrary.Models.Movie;
import actorlibrary.Repositories.MovieRepository;
import actorlibrary.Utils.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v1")
public class MovieController {

    @Autowired
    private MovieRepository repository;

    @GetMapping("/movie/all")
    public ResponseEntity<CommonResponse> getAllMovies(HttpServletRequest request){

        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();

        cr.data = repository.findAll();
        cr.message = "All Movies";

        HttpStatus resp = HttpStatus.OK;
        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }
    @GetMapping("/movie/{id}")
    public ResponseEntity<CommonResponse> getMovieById(HttpServletRequest request, @PathVariable Integer id){
        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if(repository.existsById(id)) {
            cr.data = repository.findById(id);
            cr.message = "Movie not with id: " + id;
            resp = HttpStatus.OK;
        } else {
            cr.data = null;
            cr.message = "Movie not found";
            resp = HttpStatus.NOT_FOUND;
        }
        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }

    @PostMapping("/movie")
    public ResponseEntity<CommonResponse> addMovie(HttpServletRequest request, @RequestBody Movie movie){
        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();
        movie = repository.save(movie);

        cr.data = movie;
        cr.message = "New Movie with id: " + movie.id;

        HttpStatus resp = HttpStatus.CREATED;
        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }

    @PatchMapping("/movie/{id}")
    public ResponseEntity<CommonResponse> updateMovie(HttpServletRequest request, @RequestBody Movie newMovie, @PathVariable Integer id) {
        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if (repository.existsById(id)) {
            Optional<Movie> movieRepo = repository.findById(id);
            Movie movie = movieRepo.get();

            if (newMovie.title != null) {
                movie.title = newMovie.title;
            }
            if (newMovie.releaseYear != null) {
                movie.releaseYear = newMovie.releaseYear;
            }
            if (newMovie.genre != null) {
                movie.genre = newMovie.genre;
            }
            if(newMovie.actors != null){
                movie.actors = newMovie.actors;
            }

            repository.save(movie);

            cr.data = movie;
            cr.message = "Updated movie with id: " + movie.id;
            resp = HttpStatus.OK;
        } else {
            cr.message = "Movie not found with id: " + id;
            resp = HttpStatus.NOT_FOUND;
        }
        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }

    @DeleteMapping("/movie/{id}")
    public ResponseEntity<CommonResponse> deleteMovie(HttpServletRequest request, @PathVariable Integer id) {
        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if(repository.existsById(id)) {
            repository.deleteById(id);
            cr.message = "Deleted movie with id: " + id;
            resp = HttpStatus.OK;
        } else {
            cr.message = "Movie not found with id: " + id;
            resp = HttpStatus.NOT_FOUND;
        }
        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }
}

