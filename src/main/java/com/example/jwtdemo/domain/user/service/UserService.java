package com.example.jwtdemo.domain.user.service;

import com.example.jwtdemo.domain.user.domain.User;
import com.example.jwtdemo.domain.user.domain.UserAuthority;
import com.example.jwtdemo.domain.user.domain.repository.UserRepository;
import com.example.jwtdemo.domain.user.web.dto.req.UserSaveReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    public Long createUser(UserSaveReqDto reqDto){
        return userRepository.save(reqDto.toEntity()).getIdx();
    }

    public User findByUserIdAndPassword(String id, String password){
        return userRepository.findByUserIdAndPassword(id, password).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }

    public User findByUserId(String id){
        return userRepository.findByUserId(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));

    }

    public void addAuthority(Long id, String authority){
        userRepository.findById(id).ifPresent(user -> {
            UserAuthority newRole = new UserAuthority(user.getIdx(), authority);
            if(user.getAuthorities() == null){
                HashSet<UserAuthority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                createUser(user.toDto());
            }else if(!user.getAuthorities().contains(newRole)){
                HashSet<UserAuthority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(newRole);
                user.setAuthorities(authorities);
                createUser(user.toDto());
            }
        });
    }

    public void removeAuthority(Long id, String authority){
        userRepository.findById(id).ifPresent(user -> {
            if(user.getAuthorities()==null) return;
            UserAuthority targetRole = new UserAuthority(user.getIdx(), authority);
            if(user.getAuthorities().contains(targetRole)){
                user.setAuthorities(
                        user.getAuthorities().stream().filter(auth->!auth.equals(targetRole))
                                .collect(Collectors.toSet())
                );
                createUser(user.toDto());
            }
        });
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserId(username).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }
}
