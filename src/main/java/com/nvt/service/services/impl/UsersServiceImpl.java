package com.nvt.service.services.impl;

import com.nvt.service.entities.Roles;
import com.nvt.service.entities.Users;
import com.nvt.service.repositories.RolesRepository;
import com.nvt.service.repositories.UsersRepository;
import com.nvt.service.services.UsersService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UserDetailsService, UsersService {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

    @Override
    public Users saveUser(Users user) {
        if  (user.getId() == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return usersRepository.save(user);
        } else {
            Users userObj = usersRepository.findById(user.getId()).get();
            return usersRepository.save(userObj);
        }
    }

    @Override
    public Page<Users> findAll(int page, int size) {
        // TODO Auto-generated method stub
        return null;
    }
}
