package com.system.uz.config;

import com.system.uz.env.AuthUser;
import com.system.uz.filter.BeforeFilter;
import com.system.uz.exceptions.handler.AccessHandler;
import com.system.uz.exceptions.handler.AuthHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import static com.system.uz.base.BaseUri.*;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final BeforeFilter beforeFilter;
    private final AuthUser authUser;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/webjars/**"
    };

    private static final String[] PERMIT_URLS = {
            API_V1 + LOGIN,
            API_V1 + LOGOUT,
            API_V1 + RESET_PASSWORD,
            API_V1 + FILE,
            API_V1 + FILE + PHOTO,
            API_V1 + BLOG + LIST,
            API_V1 + BLOG + BY_ID,
            API_V1 + CLIENT,
            API_V1 + FREQUENT + LIST,
            API_V1 + FREQUENT + BY_ID,
            API_V1 + PRODUCT + LIST,
            API_V1 + PRODUCT + BY_ID,
            API_V1 + CATEGORY + LIST,
            API_V1 + CATEGORY + BY_ID
    };

//    static {
//        try {
//            turnOffSSL();
//        } catch (KeyManagementException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void turnOffSSL() throws KeyManagementException, NoSuchAlgorithmException {
//        HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);
//        final var sslContext = SSLContext.getInstance("SSL");
//        sslContext.init(null, new X509TrustManager[]{new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
//            }
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
//            }
//
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//                return new X509Certificate[0];
//            }
//        }}, new SecureRandom());
//        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(PERMIT_URLS).permitAll()
                .anyRequest().authenticated();
        http.exceptionHandling()
                .accessDeniedHandler(new AccessHandler())
                .authenticationEntryPoint(new AuthHandler());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(this.beforeFilter, UsernamePasswordAuthenticationFilter.class);
        http.httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(authUser.getUsername())
                .password(this.passwordEncoder.encode(authUser.getPassword()))
                .roles(authUser.getRole());
    }
}
