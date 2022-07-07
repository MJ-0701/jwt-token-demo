package com.example.jwtdemo.domain.user.service;

import com.example.jwtdemo.domain.user.domain.repository.UserAdressRepository;
import com.example.jwtdemo.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserAdressRepository userAdressRepository;

    private final UserRepository userRepository;

    @Transactional
    public String addressRegister(){


        return null;
    }
}
