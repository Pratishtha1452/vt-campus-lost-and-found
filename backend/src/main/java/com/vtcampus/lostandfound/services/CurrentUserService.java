
package com.vtcampus.lostandfound.services;

import com.vtcampus.lostandfound.domain.entities.Users;
import com.vtcampus.lostandfound.repositories.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UsersRepository usersRepository;

    public CurrentUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You must be logged in."
            );
        }

        String email = authentication.getName();

        return usersRepository.findByEmail(email)
                .orElseThrow(() ->
                        new org.springframework.security.access.AccessDeniedException(
                                "Authenticated user no longer exists."
                        )
                );
    }
}
