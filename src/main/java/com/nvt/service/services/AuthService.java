package com.nvt.service.services;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nvt.service.common.constants.Index;
import com.nvt.service.entities.Users;
import com.nvt.service.models.AuthResponse;
import com.nvt.service.repositories.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HttpServletRequest request;

    public AuthResponse checkLogin(String username, String password) throws AuthenticationException {
        Users user = usersRepository.findByUserName(username);
        AuthResponse authResponse = new AuthResponse();
        if (checkLockUser(user)) {
            authResponse.setStatus("LOCK");
            authResponse.setMessage("Tài khoản đã bị khóa");
            return authResponse;
        }
        if (checkPassword(user, password)) {
            authResponse.setStatus("ERROR");
            authResponse.setMessage("Sai mật khẩu");
            return authResponse;
        }
        try {
            Authentication authentication = authentication(user.getUserName(), user.getPassword());
            String access_token = generateToken(request, authentication);
            authResponse.setStatus("SUCCESS");
            authResponse.setMessage("Đăng nhập thành công");
            authResponse.setAccessToken(access_token);
            authResponse.setRefreshToken(access_token);
        } catch (Exception e) {
            authResponse.setStatus("ERROR");
            authResponse.setMessage("Đăng nhập thất bại");
        }
        return authResponse;
    }

    private Boolean checkPassword(Users user, String password) {
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return false;
        } else {
            Integer count = user.getLoginFailCount();
            if (count < 4) {
                user.setLoginFailCount(count + 1);
            } else {
                Date date = new Date();
                user.setLoginFailCount(count + 1);
                user.setLoginFailDate(date);
            }
            usersRepository.save(user);
            return true;
        }
    }

    private Boolean checkLockUser(Users user) {
        Integer count = user.getLoginFailCount();
        if (count >= 5 || user.getLoginFailDate() != null) {
            return true;
        } else {
            return false;
        }
    }

    private Authentication authentication(String username, String password) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        return authenticationManager.authenticate(authenticationToken);
    }

    private String generateToken (HttpServletRequest request, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(Index.SECRET_CODE.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
                
        return access_token;
    }

}
