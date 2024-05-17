package ir.fum.cloud.notification.core.util.request.retrofit.api;

import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("user/validate")
    Call<UserVO> validateUser(@Header("Authorization") String authorization);
}
