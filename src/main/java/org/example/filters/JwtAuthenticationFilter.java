package org.example.filters;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.Role;
import org.example.exceptions.handlers.CustomAuthenticationEntryPoint;
import org.example.services.UserService;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@PropertySource("classpath:application.properties")
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomAuthenticationEntryPoint  customAuthenticationEntryPoint;

    @Value("${auth0.domain}")
    private String AUTH0_DOMAIN;



    private JwkProvider provider;

    @PostConstruct
    public void init() {
        this.provider = new JwkProviderBuilder(AUTH0_DOMAIN)
                .cached(10, 24, TimeUnit.HOURS)
                .build();
        log.info("JwtAuthenticationFilter provider initialized");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();


        String token = null;

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                token = (String) session.getAttribute("auth_token");
            }
        }
        if (token != null) {
            try {
                DecodedJWT jwt = JWT.decode(token);
                Jwk jwk = provider.get(jwt.getKeyId());
                Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer(AUTH0_DOMAIN + "/")
                        .build();
                verifier.verify(token);

                String userId = jwt.getSubject();
                request.setAttribute("user_id", userId);

                List<GrantedAuthority> roles = JwtUtil.extractRolesFromToken(token);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userService.findById(userId),
                        null,
                        roles);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                if (requiresAuthentication(request)) {
                    customAuthenticationEntryPoint.commence(
                            request,
                            response,
                            new InsufficientAuthenticationException("Invalid or expired token", e)
                    );
                    return;
                }
                filterChain.doFilter(request, response);
                return;
            }
        }
    filterChain.doFilter(request, response);
    }
    private boolean requiresAuthentication(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !(
                uri.contains("/auth")
        );
    }

}
