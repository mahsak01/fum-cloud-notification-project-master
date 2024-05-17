package ir.fum.cloud.notification.core.filter;

import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Configure an application to perform certain actions whenever an unauthenticated client tries to access private resources.
 */

@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().println(NotificationExceptionStatus.setErrorMessage(
                NotificationExceptionStatus.ACCESS_DENIED.getDescription(),
                NotificationExceptionStatus.ACCESS_DENIED).toString());
    }

}
