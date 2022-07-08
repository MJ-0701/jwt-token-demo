package com.example.jwtdemo.domain.user.domain.repository;

import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserIdAndPassword(String id, String password);

    Optional<User> findByUserId(String id);

    User findByIdx(Long idx);


    List<User> findByUserAddressLike(String userAddress);


}
