package com.nvt.service.services.impl;

import com.nvt.service.entities.Roles;
import com.nvt.service.entities.Users;
import com.nvt.service.repositories.RolesRepository;
import com.nvt.service.repositories.UsersRepository;
import com.nvt.service.services.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UserDetailsService, UsersService {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // get password from request
        String password = request.getParameter("password");
        Users user = checkLoginFailCount(username, password);
        log.info("user :{}", bCryptPasswordEncoder.matches(bCryptPasswordEncoder.encode(password), user.getPassword()));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<Roles> roles = rolesRepository.Authentications(username);
        for (Roles role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
        }
        return new User(user.getUserName(), user.getPassword(), authorities);
    }

    private Users checkLoginFailCount(String username, String password) {
        Users user = usersRepository.findByUserName(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database");
            if (checkPassword(username, password)) {
                return null;
            }
        }
        return user;

    }

    public Boolean checkPassword(String username, String password) {
        Users user = usersRepository.findByUserName(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database");
            if (!bCryptPasswordEncoder.matches(bCryptPasswordEncoder.encode(password), user.getPassword())) {
                if (user.getLoginFallDate() != null) {
                    return true;
                } else {
                    Integer count = user.getLoginFallCount();
                    if (count == null || count < 5) {
                        user.setLoginFallCount(count + 1);
                        System.out.println("sai mat khau");
                        usersRepository.save(user);
                        return true;
                    } else {
                        System.out.println("sai mat khau qua 5 lan");
                        Date date = new Date();
                        user.setLoginFallDate(date);
                        usersRepository.save(user);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
