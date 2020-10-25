package actorlibrary.Controllers;

import actorlibrary.Models.Actor;
import actorlibrary.Models.CommonResponse;
import actorlibrary.Repositories.ActorRepository;
import actorlibrary.Utils.Command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.Optional;


@RestController
@RequestMapping(value = "/api/v1")
public class ActorController {

    @Autowired
    private ActorRepository repository;

    @GetMapping("/")
    String hello(){
        return "Hello Hello";
    }

    @GetMapping("/actor/all")
    public ResponseEntity<CommonResponse> getAllActors(HttpServletRequest request) {
        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();

        cr.data = repository.findAll();
        if(repository.count()>0)
            cr.message = "All actors";
        else{
            cr.message = "No actors found";
        }

        HttpStatus resp = HttpStatus.OK;

        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }

    @GetMapping("/actor/{id}")
    public ResponseEntity<CommonResponse> getActorById(HttpServletRequest request, @PathVariable("id") Integer id) {
        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();

        HttpStatus resp;

        if (repository.existsById(id)) {
            cr.data = repository.findById(id);
            cr.message = "Actor with id: " + id;
            resp = HttpStatus.OK;
        } else {
            cr.data = null;
            cr.message = "Actor not found";
            resp = HttpStatus.NOT_FOUND;
        }

        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }

    @PostMapping("/actor")
    public ResponseEntity<CommonResponse> addActor(HttpServletRequest request, HttpServletResponse response, @RequestBody Actor actor) {
        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();

        actor = repository.save(actor);
        cr.data = actor;
        cr.message = "New actor with id: " + actor.id;

        HttpStatus resp = HttpStatus.CREATED;
        response.addHeader("Location", "/actor/" + actor.id);

        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }

    @PatchMapping("/actor/{id}")
    public ResponseEntity<CommonResponse> updateActor(HttpServletRequest request, @RequestBody Actor newActor, @PathVariable Integer id) {
        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if (repository.existsById(id)) {
            Optional<Actor> actorRepo = repository.findById(id);
            Actor actor = actorRepo.get();

            if (newActor.firstname != null) {
                actor.firstname = newActor.firstname;
            }
            if (newActor.lastname != null) {
                actor.lastname = newActor.lastname;
            }
            if (newActor.dateOfBirth != null) {
                actor.dateOfBirth = newActor.dateOfBirth;
            }
            if (newActor.imdbURL != null) {
                actor.imdbURL= newActor.imdbURL;
            }

            repository.save(actor);

            cr.data = actor;
            cr.message = "Updated actor with id: " + actor.id;
            resp = HttpStatus.OK;
        } else {
            cr.message = "Actor not found with id: " + id;
            resp = HttpStatus.NOT_FOUND;
        }
        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }

    @DeleteMapping("/actor/{id}")
    public ResponseEntity<CommonResponse> deleteActor(HttpServletRequest request, @PathVariable Integer id) {
        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if(repository.existsById(id)) {
            repository.deleteById(id);
            cr.message = "Deleted actor with id: " + id;
            resp = HttpStatus.OK;
        } else {
            cr.message = "Actor not found with id: " + id;
            resp = HttpStatus.NOT_FOUND;
        }

        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }

    @GetMapping("/actor/{id}/movies")
    public ResponseEntity<CommonResponse> getMoviesbyActor(HttpServletRequest request, @PathVariable("id") Integer id){
        Command cmd = new Command(request);
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if(repository.existsById(id)) {
            Optional<Actor> actorRepo = repository.findById(id);
            Actor actor = actorRepo.get();
            cr.data = actor.movies;
            cr.message = "Movies by actor with id: " + id;
            resp = HttpStatus.OK;
        } else {
            cr.data = null;
            cr.message = "Actor not found";
            resp = HttpStatus.NOT_FOUND;
        }

        cmd.setResult(resp);
        return new ResponseEntity<>(cr, resp);
    }
}
