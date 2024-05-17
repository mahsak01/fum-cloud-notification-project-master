package ir.fum.cloud.notification.core.util.request;

import ir.fum.cloud.notification.core.domain.model.helper.GenericResponse;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import ir.fum.cloud.notification.core.util.mapper.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseWriterUtil {

    private ResponseWriterUtil() {
    }

    public static void sendResponse(HttpServletResponse response,
                                    HttpStatus status,
                                    GenericResponse<Object> restResponse
    ) throws IOException {
        response.setStatus(status.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(JsonUtils.getWithNoTimestampJson(restResponse));
    }

    public static void sendProcessErrorResponse(HttpServletRequest request,
                                                HttpServletResponse response,
                                                NotificationException e,
                                                HttpStatus status) throws IOException {

        sendResponse(response, status, NotificationExceptionStatus.setErrorMessage(e));
    }

}
