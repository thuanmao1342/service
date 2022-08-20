package com.nvt.service.repositories;

import com.nvt.service.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
    @Query(
            value = "select r.id, r.role_code, r.role_name, r.create_date, r.description, r.status, r.create_user from roles r" +
                    " join role_user ru on r.id = ru.role_id" +
                    " join users u on ru.user_id = u.id" +
                    " where u.username =?1",
            nativeQuery = true)
    List<Roles> Authentications(String username);
}
