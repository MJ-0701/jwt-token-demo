package com.example.jwtdemo.domain.user.domain.repository;

import com.example.jwtdemo.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserIdAndPassword(String id, String password);

    Optional<User> findByUserId(String id);

}
