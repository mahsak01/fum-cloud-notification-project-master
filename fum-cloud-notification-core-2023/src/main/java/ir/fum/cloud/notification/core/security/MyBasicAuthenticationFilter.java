package ir.fum.cloud.notification.core.security;


import ir.fum.cloud.notification.core.configuration.ProjectConfiguration;
import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import ir.fum.cloud.notification.core.util.request.ResponseWriterUtil;
import ir.fum.cloud.notification.core.util.request.retrofit.RetrofitHelper;
import ir.fum.cloud.notification.core.util.request.retrofit.RetrofitUtil;
import ir.fum.cloud.notification.core.util.request.retrofit.api.AuthApi;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import retrofit2.Call;
import retrofit2.Response;

import javax.annotation.Priority;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@WebFilter
@Priority(1000)
public class MyBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final ProjectConfiguration projectConfiguration;

    public MyBasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                       ProjectConfiguration projectConfiguration) {
        super(authenticationManager);
        this.projectConfiguration = projectConfiguration;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) {

        if (!checkTokenNeedsCheck(request)) {
            chain.doFilter(request, response);
            return;
        }

        try {

            AuthApi api = RetrofitUtil.getInstance(RetrofitUtil.authRetrofit, projectConfiguration.getAuthServiceUrl())
                    .create(AuthApi.class);

            String token = request.getHeader("Authorization");

            Call<UserVO> userPrincipal = api.validateUser(token);

            Response<UserVO> authResponse = RetrofitHelper.request(userPrincipal);

            if (!authResponse.isSuccessful()) {
                throw NotificationException.exception(
                        NotificationExceptionStatus.INVALID_TOKEN
                );
            }

            UserVO principal = authResponse.body();

            SecurityContext securityContext = SecurityContextHolder.getContext();

            if (principal != null) {

                UsernamePasswordAuthenticationToken authentication = getAuthentication(
                        new UserVO(principal.getUser_id())
                );

                securityContext.setAuthentication(authentication);

                log.debug("Authentication set to security context");
            }

        } catch (NotificationException e) {
            log.error(e.getDeveloperMessage());
            ResponseWriterUtil.sendProcessErrorResponse(request, response, e, HttpStatus.valueOf(e.getStatus()));
            return;
        } catch (Exception e) {

            log.error(e.getMessage());

            ResponseWriterUtil.sendProcessErrorResponse(request,
                    response,
                    NotificationException.exception(NotificationExceptionStatus.INTERNAL_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }


        log.info("User is authenticated");

        doFilter(request, response, chain);

    }

    private boolean checkTokenNeedsCheck(@NotNull HttpServletRequest request) {
        return !request.getRequestURI().toLowerCase().contains("swagger") &&
                !request.getRequestURI().toLowerCase().contains("docs") &&
                !request.getRequestURI().contains("webhook");
    }

    private UsernamePasswordAuthenticationToken getAuthentication(UserVO credential) {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        return new UsernamePasswordAuthenticationToken(credential, credential, authorities);
    }


}
