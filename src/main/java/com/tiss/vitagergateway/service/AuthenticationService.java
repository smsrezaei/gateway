package com.tiss.vitagergateway.service;

import com.tiss.vitagergateway.dto.LoginUserDto;
import com.tiss.vitagergateway.dto.RegisterUserDto;
import com.tiss.vitagergateway.entity.User;
import com.tiss.vitagergateway.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    //private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager
            //,PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(input.getPassword());//passwordEncoder.encode(input.getPassword())

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        input.getEmail(),
//                        input.getPassword()
//                )
//        );

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow();
        if (user.getPassword().equals(input.getPassword())) {return user;}
        else throw new BadCredentialsException("UnAuthorized");
    }
}