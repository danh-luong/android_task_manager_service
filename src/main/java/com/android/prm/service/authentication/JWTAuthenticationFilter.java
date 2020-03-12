package com.android.prm.service.authentication;

import com.android.prm.service.accountdto.UserInfoAdminDTO;
import com.android.prm.service.controller.UserController;
import com.android.prm.service.mapper.UserMapper;
import com.android.prm.service.model.request.AccountRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AccountRequest user;
    private AccountRequest userInfo;

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            user = new ObjectMapper()
                    .readValue(request.getInputStream(), AccountRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoAdminDTO userInfoAdminDTO = UserController.userMapper.loadUserProfileByUsernameFromAdmin(user.getUsername());
        if (userInfoAdminDTO.getStatus().equalsIgnoreCase("deactive")) {
            throw new BadCredentialsException("Deactive Account");
        } else {
            user = UserController.userMapper.loadUserByUsername(user.getUsername());
            userInfo = new AccountRequest();
            userInfo.setUsername(user.getUsername());
            userInfo.setRoleId(user.getRoleId());
            String accountRequestString = objectMapper.writeValueAsString(userInfo);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(accountRequestString);
        }
    }
}
