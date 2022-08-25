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
import com.nvt.service.common.constants.Messages;
import com.nvt.service.common.constants.Status;
import com.nvt.service.entities.Users;
import com.nvt.service.models.AuthResponse;
import com.nvt.service.models.Tokens;
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
        Integer count = user.getLoginFailCount();
        if (checkUserNull(user)) {
            authResponse.setStatus(Status.ERROR);
            authResponse.setMessage(Messages.NOT_FOUND_USER);
            return authResponse;
        }
        if (checkLockUser(user)) {
            authResponse.setStatus(Status.ERROR);
            authResponse.setMessage(Messages.USER_IS_LOOK);
            return authResponse;
        }
        if (checkPassword(user, password)) {
            count = user.getLoginFailCount();
            authResponse.setStatus(Status.ERROR);
            authResponse.setMessage(Messages.WRONG_PASSWORD + count + "lần");
            return authResponse;
        }
        if (count > 0) {
            user.setLoginFailCount(0);
            usersRepository.save(user);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            Tokens tokens = generateToken(request, authentication);
            authResponse.setStatus(Status.SUCCESS);
            authResponse.setMessage("Đăng nhập thành công!");
            authResponse.setExpiresIn((tokens.getExpiresIn()));
            authResponse.setAccessToken(tokens.getAccessToken());
            authResponse.setRefreshToken(tokens.getRefreshToken());
        } catch (Exception e) {
            authResponse.setStatus(Status.ERROR);
            authResponse.setMessage("Đăng nhập thất bại: "+e.getMessage());
        }
        return authResponse;
    }

    private Boolean checkPassword(Users user, String password) {
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return false;
        } else {
            Integer count = user.getLoginFailCount();
            if (count == 4) {
                Date date = new Date();
                user.setLoginFailCount(count + 1);
                user.setLoginFailDate(date);
            } else {
                user.setLoginFailCount(count + 1);
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

    private Boolean checkUserNull(Users user) {
        if (user == null) {
            return true;
        } else {
            return false;
        }
    }

    private Tokens generateToken (HttpServletRequest request, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(Index.SECRET_CODE.getBytes());
        Date date = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(date)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);;
        return new Tokens(access_token, refresh_token, date.toString());
    }

}
