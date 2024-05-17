package ir.fum.cloud.notification.core.util.request.retrofit;

import lombok.extern.log4j.Log4j2;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Log4j2
public class RetrofitHelper {

    private RetrofitHelper() {
    }

    public static <T> Response request(Call<T> addContactService) throws IOException {
        return addContactService.execute();
    }

}
