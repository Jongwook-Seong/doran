package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//@Component("userDetailsService")
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByIdentity(username)
//                .map(user -> createUser(username, user))
//                .orElseThrow(() -> new UsernameNotFoundException(username + ": 데이터베이스에 존재하지 않습니다."));
        return null;
    }
}
