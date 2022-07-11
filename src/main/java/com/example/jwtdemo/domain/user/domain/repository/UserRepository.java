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


//    @Query("SELECT u FROM User u WHERE u.userAddress.address LIKE %:userAddress%")
//    List<User> findByUserAddressLike(@Param("userAddress") String userAddress);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:email% ")
    List<User>findByEmailLike(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.userAddress.address LIKE %:address%")
    List<User> findByUserAddressLike(@Param("address")String address);


}
