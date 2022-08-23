package com.nvt.service.services.impl;

import com.nvt.service.entities.Roles;
import com.nvt.service.entities.Users;
import com.nvt.service.repositories.RolesRepository;
import com.nvt.service.repositories.UsersRepository;
import com.nvt.service.services.UsersService;
import lombok.RequiredArgsConstructor;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UserDetailsService, UsersService {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // get password from request
        Users user = usersRepository.findByUserName(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<Roles> roles = rolesRepository.Authentications(username);
        for (Roles role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
        }
        return new User(user.getUserName(), user.getPassword(), authorities);
    }
}
