package actorlibrary.Controllers;

import actorlibrary.Models.Actor;
import actorlibrary.Models.CommonResponse;
import actorlibrary.Repositories.ActorRepository;
import actorlibrary.Utils.Command;
import actorlibrary.Utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@RestController
public class ActorController {


    @Autowired
    private ActorRepository repository;
    @GetMapping("/")
    String hello(){
        return "Hello Hello";
    }

    @GetMapping("/api/v1/actor/all")
    public ResponseEntity<CommonResponse> getAllActors(HttpServletRequest request) {
        Command cmd = new Command(request);


        CommonResponse cr = new CommonResponse();
        cr.data = repository.findAll();
        cr.message = "All actors";

        HttpStatus resp = HttpStatus.OK;


        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);

    }

    @GetMapping("/api/v1/actor/{id}")
    public ResponseEntity<CommonResponse> getActorById(HttpServletRequest request, @PathVariable("id") Integer id) {
        Command cmd = new Command(request);

        //process
        CommonResponse cr = new CommonResponse();
        HttpStatus resp;

        if (repository.existsById(id)) {
            cr.data = repository.findById(id);
            cr.message = "Author with id: " + id;
            resp = HttpStatus.OK;
        } else {
            cr.data = null;
            cr.message = "Author not found";
            resp = HttpStatus.NOT_FOUND;
        }

        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

    @PostMapping("api/v1/actor")
    public ResponseEntity<CommonResponse> addActor(HttpServletRequest request, HttpServletResponse response, @RequestBody Actor actor) {
        Command cmd = new Command(request);

        actor = repository.save(actor);

        CommonResponse cr = new CommonResponse();
        cr.data = actor;
        cr.message = "New actor with id: " + actor.id;

        HttpStatus resp = HttpStatus.CREATED;

        response.addHeader("Location", "/actor/" + actor.id);

        cmd.setResult(resp);
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

    @PatchMapping("api/v1/actor/{id}")
    public ResponseEntity<CommonResponse> updateAuthor(HttpServletRequest request, @RequestBody Actor newActor, @PathVariable Integer id) {
        Command cmd = new Command(request);

        //process
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
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }
}
