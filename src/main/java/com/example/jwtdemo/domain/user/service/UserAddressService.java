package com.example.jwtdemo.domain.user.service;

import com.example.jwtdemo.domain.user.domain.UserAddress;
import com.example.jwtdemo.domain.user.domain.repository.UserAddressRepository;
import com.example.jwtdemo.domain.user.web.dto.req.UserAddressRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;

    private final UserService userService;

    @Transactional
    public UserAddress addressRegister(UserAddressRegisterDto userAddressRegisterDto){

        return userAddressRepository.save(userAddressRegisterDto.toEntity());
    }
}
