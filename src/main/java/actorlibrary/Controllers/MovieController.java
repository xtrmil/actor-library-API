package actorlibrary.Controllers;


import actorlibrary.Models.Actor;
import actorlibrary.Models.CommonResponse;
import actorlibrary.Models.Movie;
import actorlibrary.Repositories.ActorRepository;
import actorlibrary.Repositories.MovieRepository;
import actorlibrary.Utils.Command;
import actorlibrary.Utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v1")
public class MovieController {



    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/movie/all")
    public ResponseEntity<CommonResponse> getAllMovies(HttpServletRequest request){
        Command cmd = new Command(request);

        //process
        CommonResponse cr = new CommonResponse();
        cr.data = movieRepository.findAll();
        cr.message = "All Movies";

        HttpStatus resp = HttpStatus.OK;

        //log and return
        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }
    @GetMapping("/movie/{id}")
    public ResponseEntity<CommonResponse> getMovieById(HttpServletRequest request, @PathVariable Integer id){
        Command cmd = new Command(request);

        //process
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if(movieRepository.existsById(id)) {
            cr.data = movieRepository.findById(id);
            cr.message = "Book with id: " + id;
            resp = HttpStatus.OK;
        } else {
            cr.data = null;
            cr.message = "Book not found";
            resp = HttpStatus.NOT_FOUND;
        }

        //log and return
        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

    @PutMapping("/movie/{id}")
    public ResponseEntity<CommonResponse> replaceMovie(HttpServletRequest request, @RequestBody Movie newMovie, @PathVariable Integer id) {
        Command cmd = new Command(request);

        //process
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if(movieRepository.existsById(id)) {
            Optional<Movie> movieRepo = movieRepository.findById(id);
            Movie movie = movieRepo.get();

            movie.title = newMovie.title;
            movie.releaseYear = newMovie.releaseYear;
            movie.genre = newMovie.genre;

            movie.actors = new HashSet<Actor>();
            for(Actor actor : newMovie.actors) {
                movie.actors.add(actor);
            }

            movieRepository.save(movie);

            cr.data = movie;
            cr.message = "Replaced movie with id: " + movie.id;
            resp = HttpStatus.OK;
        } else {
            cr.message = "Movie not found with id: " + id;
            resp = HttpStatus.NOT_FOUND;
        }

        //log and return
        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

    @PostMapping("/movie")
    public ResponseEntity<CommonResponse> addMovie(HttpServletRequest request, @RequestBody Movie movie){
        Command cmd = new Command(request);

        //process
        movie = movieRepository.save(movie);

        CommonResponse cr = new CommonResponse();
        cr.data = movie;
        cr.message = "New book with id: " + movie.id;

        HttpStatus resp = HttpStatus.CREATED;

        //log and return
        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

}

