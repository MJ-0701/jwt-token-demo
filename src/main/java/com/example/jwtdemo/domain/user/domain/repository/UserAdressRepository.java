package com.example.jwtdemo.domain.user.domain.repository;

import com.example.jwtdemo.domain.user.domain.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdressRepository extends JpaRepository<UserAddress, Long> {
}
