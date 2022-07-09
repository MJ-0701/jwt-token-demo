package com.example.jwtdemo.domain.user.web.controller;

import com.example.jwtdemo.domain.user.domain.UserAddress;
import com.example.jwtdemo.domain.user.service.UserAddressService;
import com.example.jwtdemo.domain.user.web.dto.req.UserAddressRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-address")
public class UserAddressApiController {

    private final UserAddressService addressService;

    @PostMapping("register")
    public ResponseEntity<UserAddress> registerAddress(
            @RequestBody UserAddressRegisterDto registerDto){

        return ResponseEntity.ok(addressService.addressRegister(registerDto));
    }
}
