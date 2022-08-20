package com.nvt.service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private Integer status;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "last_change_password")
    private Date lastChangePassword;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "login_fall_date")
    private Date loginFallDate;

    @Column(name = "login_fall_count")
    private Integer loginFallCount;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<RoleUser> roleUserEntityList;

}
