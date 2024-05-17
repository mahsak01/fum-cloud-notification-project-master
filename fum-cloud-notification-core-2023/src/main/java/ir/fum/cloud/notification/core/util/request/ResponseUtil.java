package ir.fum.cloud.notification.core.util.request;

import ir.fum.cloud.notification.core.domain.model.helper.GenericResponse;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.util.mapper.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ResponseUtil {

    public static <T> GenericResponse<T> getResponse(T content, long totalCount) {
        GenericResponse<T> genericResponse = new GenericResponse<>();
        genericResponse.setContent(content);
        genericResponse.setTotalCount(totalCount);
        genericResponse.setStatus(200);
        return genericResponse;
    }

    public static <T> GenericResponse<T> getResponse(T content) {
        return getResponse(content, 1);
    }

    public static GenericResponse<Object> getErrorGenericResponse(NotificationException e) {

        return GenericResponse.builder()
                .errorDescription(e.getMessage())
                .hasError(true)
                .content(null)
                .status(e.getCode() == 0 ? 200 : e.getCode())
                .build();

    }

    public <T> void sendResponse(HttpServletResponse response,
                                 HttpStatus status,
                                 GenericResponse<T> restResponse,
                                 String serverVersion,
                                 String serverName) throws IOException {
        setResponseHeaders(response, status, serverVersion, serverName);
        response.getWriter().write(JsonUtils.getWithNoTimestampJson(restResponse));
    }

    private void setResponseHeaders(HttpServletResponse response,
                                    HttpStatus status,
                                    String serverVersion,
                                    String serverName) {
        response.setStatus(status.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Server", getServer(serverVersion, serverName));
        response.setHeader("X-Powered-By", getServer(serverVersion, serverName));
    }

    private String getServer(String serverVersion,
                             String serverName) {
        return " Server v" + serverVersion + " #" + serverName;
    }

}

