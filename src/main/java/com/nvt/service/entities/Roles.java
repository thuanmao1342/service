package com.nvt.service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "role_code")
    private String RoleCode;

    @Column(name = "role_name")
    private String RoleName;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "create_user")
    private Integer createUser;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    List<RoleUser> roleUserEntityList;
}
