package nuce.tatv.noteeverything.Remotes;
import nuce.tatv.noteeverything.Models.Expense;
import nuce.tatv.noteeverything.Models.Note;
import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.Models.User;
import nuce.tatv.noteeverything.Models.Work;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetDataService {
    @Headers("Content-Type: application/json")
    @GET("/user")
    Call<Response> getAllUser();
    @POST("/user")
    Call<Response> postUser(@Body User user);
    @POST("/user/update")
    Call<Response> updateUser(@Body User user);

    @GET("/note")
    Call<Response> getAllNote();

    @GET("/note/{user_name}")
    Call<Response> getNote(@Path("user_name") String user_name);

    @POST("/note/add")
    Call<Response> postNote(@Body Note note);
    @POST("/note/delete")
    Call<Response> deleteNote(@Body Note note);
    @POST("note/update")
    Call<Response> updateNote(@Body Note note);


    @GET("/expense/{user_name}")
    Call<Response> getExpense(@Path("user_name") String user_name);

    @POST("/expense/add")
    Call<Response> postExpense(@Body Expense expense);

    @POST("/expense/update")
    Call<Response> updateExpense(@Body Expense expense);

    @DELETE("/expense/{expense_id}")
    Call<Response> deleteExpense(@Path("expense_id") Integer expense_id);


    @GET("/work/{user_name}")
    Call<Response> getWork(@Path("user_name") String user_name);

    @POST("/work/add")
    Call<Response> postWork(@Body Work work);

    @POST("/work/update")
    Call<Response> updateWork(@Body Work work);

    @DELETE("/work/{work_id}")
    Call<Response> deleteWork(@Path("work_id") Integer work_id);

}
