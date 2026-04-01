package com.gokul.librarymanagement.service;

import com.gokul.librarymanagement.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User with the given name not found"));
    }
}
