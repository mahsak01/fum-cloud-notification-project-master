package ir.fum.cloud.notification.core.util.request;

import ir.fum.cloud.notification.core.domain.model.helper.GenericResponse;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import ir.fum.cloud.notification.core.util.mapper.GsonUtils;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author Ali Mojahed on 11/26/2022
 * @project noticom
 **/
public class OkHttpHelper {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static Logger logger = LogManager.getLogger(OkHttpHelper.class);
    private OkHttpClient client = new OkHttpClient();

    private String baseUrl;

    public OkHttpHelper(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public <T> GenericResponse post(String endPoint,
                                    String json,
                                    Map<String, String> headers,
                                    Class<T> responseClass) throws NotificationException {

        GenericResponse genericResponse = new GenericResponse<>();

        RequestBody body = RequestBody.create(JSON, json);

        Headers headersBuilder = Headers.of(headers);

        Request request = new Request.Builder()
                .url(baseUrl + endPoint)
                .headers(headersBuilder)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println(responseBody);

                genericResponse.setContent(GsonUtils.getObject(responseBody, responseClass));

            } else {
                String errorDescription = "";

                if (response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);

                    if (jsonObject.has("message"))
                        errorDescription = (String) jsonObject.get("message");

                } else {
                    errorDescription = response.message();
                }

                throw NotificationException.exception(NotificationExceptionStatus.TOOKA_ERROR,
                        errorDescription);
            }


        } catch (IOException e) {
            logger.error(e);
            throw NotificationException.exception(NotificationExceptionStatus.INTERNAL_ERROR);
        }
        return genericResponse;
    }


    public Response get() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl)
                .build();

        Response response = client.newCall(request).execute();

        response.close();

        return response;

    }


    public GenericResponse get(String endPoint,
                               Map<String, String> headers,
                               Map<String, String> queryParams) throws NotificationException {


        GenericResponse genericResponse = new GenericResponse<>();

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl + endPoint))
                .newBuilder();

        queryParams.forEach(urlBuilder::addQueryParameter);


        Headers headersBuilder = Headers.of(headers);


        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .headers(headersBuilder)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println(responseBody);

                genericResponse.setContent("");

            } else {
                String errorDescription = "";

                if (response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);

                    if (jsonObject.has("message"))
                        errorDescription = (String) jsonObject.get("message");

                } else {
                    errorDescription = response.message();
                }

                throw NotificationException.exception(NotificationExceptionStatus.TOOKA_ERROR,
                        errorDescription);
            }


        } catch (Exception e) {
            logger.error(e);
            throw NotificationException.exception(NotificationExceptionStatus.INTERNAL_ERROR);
        }

        return genericResponse;
    }
}
