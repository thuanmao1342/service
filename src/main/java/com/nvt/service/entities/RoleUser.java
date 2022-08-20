package com.nvt.service.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "role_user")
@Data
public class RoleUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "role_id")
    Roles role;

    @ManyToOne @JoinColumn(name = "user_id")
    Users user;

}
