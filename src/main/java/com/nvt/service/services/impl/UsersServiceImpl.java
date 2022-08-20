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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UserDetailsService, UsersService {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUserName(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (user == null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }else {
            log.info("User found in the database");
            List<Roles> roles = rolesRepository.Authentications(username);
            for (Roles role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
            }
        }
        return new User(user.getUserName(), user.getPassword(), authorities);
    }
}
