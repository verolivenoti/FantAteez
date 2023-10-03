package com.capitaneriadiporto.fantateez.config;

import com.capitaneriadiporto.fantateez.entity.Users;
import com.capitaneriadiporto.fantateez.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Optional<Users> user = userService.findUserByName(username);
        return user.map(UserInfoDetails::new).orElseThrow(()->new UsernameNotFoundException("HEY username not found"));
    }
}
