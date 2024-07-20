package com.example.demo.Dao;

import com.example.demo.Model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<AppUser, String> {
    Optional<AppUser> findByUsername(String username);
}
