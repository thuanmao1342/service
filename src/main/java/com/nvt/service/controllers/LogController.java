package com.nvt.service.controllers;

import com.nvt.service.entities.Customer;
import com.nvt.service.entities.Users;
import com.nvt.service.repositories.CustomerRepository;
import com.nvt.service.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {

    private final UsersRepository usersRepository;
    private final CustomerRepository customerRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public List<Users> logAppRun(){
        List<Customer> users = customerRepository.findAll();
        List<Users> list = usersRepository.findAll();
        System.out.println(users);
        return list;
    }

    @GetMapping("/a")
    public String getError(@RequestParam("a") int a) throws Exception{
        if (a == 1){
            throw new RuntimeException("lay message o day hehehe");
        }
        return "thuan rat la dep trai";
    }
}
