package com.example.banksecurity.Security.UserDetails;

import com.example.banksecurity.Storage.User.User;
import com.example.banksecurity.Storage.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();

        try {
            if (userRepository.findByUsername(username).isPresent()){
                user = userRepository.findByUsername(username).get();
            }
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User  not found with username " + username);
        }
        return UserDetailsImpl.build(user);
    }
}
