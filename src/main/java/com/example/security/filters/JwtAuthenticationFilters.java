package com.example.security.filters;

import com.example.SpringSecurityJwtApplication;
import com.example.models.UserEntity;
import com.example.security.jwt.JwtUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilters extends UsernamePasswordAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilters.class);
    @Autowired
    private JwtUtils jwtUtils;

    public JwtAuthenticationFilters(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        UserEntity userEntity =null;
        String username = "";
        String passsword = "";
        try {
            userEntity = new ObjectMapper().readValue(request.getInputStream(),UserEntity.class);
            username =userEntity.getUsername();
            passsword = userEntity.getPassword();
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,passsword);
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String token = jwtUtils.generateTokenAccess(user.getUsername());
        logger.error(token);

        response.addHeader("Authorization",token);
        Map<String,Object> httpResponse =new HashMap<>();
        httpResponse.put("Message","Authentication correct");
        httpResponse.put("Username",user.getUsername());
        httpResponse.put("token",token);
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().flush();
        super.successfulAuthentication(request, response, chain, authResult);
    }


}
