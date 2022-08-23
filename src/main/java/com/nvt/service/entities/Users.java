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

    @Column(name = "username")
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

    @Column(name = "login_fail_date")
    private Date loginFailDate;

    @Column(name = "login_fail_count")
    private Integer loginFailCount;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<RoleUser> roleUserEntityList;

}
