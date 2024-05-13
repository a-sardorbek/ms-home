package com.system.uz.filter;

import com.system.uz.config.UserDetailsAuth;
import com.system.uz.enums.Lang;
import com.system.uz.global.GlobalVar;
import com.system.uz.global.JwtConstant;
import com.system.uz.global.Utils;
import com.system.uz.rest.model.admin.user.UserData;
import com.system.uz.rest.service.UserService;
import com.system.uz.rest.service.component.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.system.uz.base.BaseUri.*;

@Component
@RequiredArgsConstructor
public class BeforeFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String URI = request.getRequestURI();

        if(isOpenUri(URI)){
            String acceptLang = request.getHeader("Accept-Language");
            Lang lang = this.parseAcceptLanguage(acceptLang);
            GlobalVar.setLANG(lang);
            String authorizationRequestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationRequestHeader == null || !authorizationRequestHeader.startsWith(JwtConstant.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        if(URI.contains(LOGOUT)){
            String authorizationRequestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationRequestHeader == null || !authorizationRequestHeader.startsWith(JwtConstant.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authorizationRequestHeader.substring(JwtConstant.TOKEN_PREFIX.length());
            String subject = jwtTokenProvider.getSubjectFromToken(token);
            GlobalVar.setUserId(subject);
        }

        if (isAdminUri(URI)) {
            try {
                String authorizationRequestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (authorizationRequestHeader == null || !authorizationRequestHeader.startsWith(JwtConstant.TOKEN_PREFIX)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String token = authorizationRequestHeader.substring(JwtConstant.TOKEN_PREFIX.length());

                String subject = jwtTokenProvider.getSubjectFromToken(token);

                if (jwtTokenProvider.isValidToken(subject, token)) {
                    UserData userData = userService.getAuthUser(subject);
                    GlobalVar.setUserId(userData.getUserId());

                    UserDetailsAuth userDetailsAuth = new UserDetailsAuth(
                            userData.getUsername(),
                            userData.getPassword(),
                            grantedAuthorities(userData.getPermissions())
                    );

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetailsAuth, "", userDetailsAuth.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }catch (Exception e){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("{\"message\": \"\"}");
                response.getWriter().flush();
                return;
            }
        }
        filterChain.doFilter(request, response);

    }

    private Lang parseAcceptLanguage(String acceptLanguage) {
        if (acceptLanguage.contains("ru")) {
            return Lang.RUS;
        } else if (acceptLanguage.contains("en")) {
            return Lang.ENG;
        }else {
            return Lang.UZB;
        }
    }

    private boolean isAdminUri(String url) {
        if(url.contains(ADMIN)){
            return true;
        }
        return false;
    }

    private boolean isOpenUri(String url) {
        final String[] AUTH_WHITELIST = {
                "/swagger-resources",
                "/swagger-ui.html",
                "/v2/api-docs",
                "/v3/api-docs",
                "/webjars"
        };
        for(String uri: AUTH_WHITELIST){
            if(url.contains(uri)){
                return true;
            }
        }

        if(!url.contains(API_V1 + ADMIN)){
            return true;
        }

        return false;
    }


    private static Collection<GrantedAuthority> grantedAuthorities(List<String> permissions) {
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(request, response);
    }

    public static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    public static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}
