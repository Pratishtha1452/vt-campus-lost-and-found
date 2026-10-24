
package com.vtcampus.lostandfound.services.impl;

import com.vtcampus.lostandfound.domain.entities.Users;
import com.vtcampus.lostandfound.repositories.UsersRepository;
import com.vtcampus.lostandfound.services.AuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UsersRepository usersRepository,
            PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users login(String email, String password) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return user;
    }
}
