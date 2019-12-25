package nuce.tatv.noteeverything.Remotes;
import nuce.tatv.noteeverything.Models.Note;
import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.Models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetDataService {
    @Headers("Content-Type: application/json")
    @GET("/user")
    Call<Response> getAllUser();
    @POST("/user")
    Call<User> postUser(@Body User user);

    @GET("/note")
    Call<Response> getAllNote();
    @POST("/note/add")
    Call<Note> postNote(@Body Note note);
    @POST("/note/delete")
    Call<Note> deleteNote(@Body Note note);
    @POST("note/update")
    Call<Note> updateNote(@Body Note note);

}
