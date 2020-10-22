package actorlibrary.Models;

public class ErrorResponse {
    public String error;
    public String message;

    public ErrorResponse(){};

    public ErrorResponse(String error){
        this.error = error;
    };

    public ErrorResponse(String error, String message){
        this.error = error;
        this.message = message;
    };
}
