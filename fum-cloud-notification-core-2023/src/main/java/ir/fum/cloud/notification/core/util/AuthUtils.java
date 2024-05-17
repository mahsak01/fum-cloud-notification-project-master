package ir.fum.cloud.notification.core.util;


import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utils for user authentication
 */

@Component
public class AuthUtils {

    public static boolean isAuthenticated() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context != null &&
                context.getAuthentication() != null &&
                context.getAuthentication().getPrincipal() != null &&
                !context.getAuthentication().getPrincipal().equals("_ANONYMOUS_");
    }

    /**
     * Retrieves user information from security context holder
     *
     * @return the current user entity
     */
    public static UserVO getCurrentUser() throws NotificationException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken || authentication.getPrincipal() == null) {
            throw NotificationException.exception(NotificationExceptionStatus.UNAUTHORIZED);
        }

        UserVO userPrincipal = (UserVO) authentication.getPrincipal();

        return userPrincipal;
    }

}