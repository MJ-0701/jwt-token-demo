package com.example.jwtdemo.domain.user.service;

import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserAddress;
import com.example.jwtdemo.domain.user.domain.UserAuthority;
import com.example.jwtdemo.domain.user.domain.repository.UserAddressRepository;
import com.example.jwtdemo.domain.user.domain.repository.UserRepository;
import com.example.jwtdemo.domain.user.web.dto.req.UserAddressRegisterDto;
import com.example.jwtdemo.domain.user.web.dto.req.UserLoginRequestDto;
import com.example.jwtdemo.domain.user.web.dto.req.UserSaveReqDto;
import com.example.jwtdemo.domain.user.web.dto.res.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserAddressRepository userAddressRepository;

    @Transactional
    public User save(User user){
        return userRepository.save(user);
    }

    @Transactional
    public Long createUser(UserSaveReqDto reqDto, UserAddressRegisterDto registerDto){
        UserAddress address = userAddressRepository.findByIdx(registerDto.toEntity().getIdx());

        UserSaveReqDto saveReqDto = UserSaveReqDto
                .builder()
                .userName(reqDto.getUserName())
                .email(reqDto.getEmail())
                .password(reqDto.getPassword())
                .userAddress(address)
                .build();

        return userRepository.save(saveReqDto.toEntity()).getIdx();
    }

    @Transactional(readOnly = true)
    public User findByUserIdAndPassword(String id, String password){
        return userRepository.findByUserIdAndPassword(id, password).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }

    @Transactional(readOnly = true)
    public User findByUserId(String id){
        return userRepository.findByUserId(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUserInfo(){
        return userRepository.findAll().stream().map(UserResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void addAuthority(Long id, String authority){
        userRepository.findById(id).ifPresent(user -> {
            UserAuthority newRole = new UserAuthority(user.getIdx(), authority);
            if(user.getAuthorities() == null){
                HashSet<UserAuthority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }else if(!user.getAuthorities().contains(newRole)){
                HashSet<UserAuthority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }
        });
    }

    @Transactional
    public void removeAuthority(Long id, String authority){
        userRepository.findById(id).ifPresent(user -> {
            if(user.getAuthorities()==null) return;
            UserAuthority targetRole = new UserAuthority(user.getIdx(), authority);
            if(user.getAuthorities().contains(targetRole)){
                user.setAuthorities(
                        user.getAuthorities().stream().filter(auth->!auth.equals(targetRole))
                                .collect(Collectors.toSet())
                );
                save(user);
            }
        });
    }


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserId(username).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }
}
