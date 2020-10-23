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

//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.Unirest;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;


@RestController
public class ActorController {

    public static void main(String[]args) throws Exception{

//        getActorsMovies("https://www.imdb.com/name/nm0000216/");


    }

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
        if(repository.count()>0)
            cr.message = "All actors";
        else{
            cr.message = "No actors found";
        }

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
    @GetMapping
    public ResponseEntity<CommonResponse> getMovieById(HttpServletRequest request, @PathVariable("id") Integer id) {
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

    @DeleteMapping("api/v1/actor/{id}")
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
        Logger.getInstance().logCommand(cmd);
        return new ResponseEntity<>(cr, resp);
    }

//    public static void getActorsMovies(String imdbUrl) throws Exception{
//        ArrayList<Movie> allMovies = new ArrayList<>();
//        String querry = "https://rapidapi.p.rapidapi.com/actors/get-all-filmography?nconst=" +imdbUrl.substring(26,35);
//
//
//        HttpResponse<String> response = Unirest.get(querry)
//                .header("x-rapidapi-host", "imdb8.p.rapidapi.com")
//                .header("x-rapidapi-key", "3ff559fc9dmsh90e7d601c0dcaa5p15bf2djsnd7c89f0e1067")
//                .asString();
//        //Prettifying
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonParser jp = new JsonParser();
//        JsonElement je = jp.parse(response.getBody().toString());
//        String prettyJsonString = gson.toJson(je);
//        System.out.println(prettyJsonString);
//
//    }

}
