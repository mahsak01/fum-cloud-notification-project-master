package ir.fum.cloud.notification.core.util.request.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ir.fum.cloud.notification.core.util.MyDateTypeAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Date;

public class RetrofitUtil {

    public static Retrofit authRetrofit = null;


    public static synchronized Retrofit getInstance(Retrofit retrofit, String baseUrl) {

        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new MyDateTypeAdapter())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    public static synchronized Retrofit getInstanceWithoutConverter(Retrofit retrofit, String baseUrl) {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .build();
        }

        return retrofit;
    }


}
