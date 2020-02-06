package nuce.tatv.noteeverything.Remotes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
//    private static final String BASE_URL = "http://192.168.0.103:8888";
//    private static final String BASE_URL = "http://192.168.43.156:8888";
    private static final String BASE_URL = "http://192.168.56.153:8888";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
