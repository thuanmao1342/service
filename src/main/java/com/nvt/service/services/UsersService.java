package com.nvt.service.services;

import org.springframework.data.domain.Page;

import com.nvt.service.entities.Users;

public interface UsersService {

    public Users saveUser(Users user);

    public Page<Users> findAll(int page, int size);
}
