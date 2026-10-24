
package com.vtcampus.lostandfound.services;

import com.vtcampus.lostandfound.domain.entities.Users;

import java.util.Optional;

public interface UserService {

    Users createUser(Users user);

    Optional<Users> viewUserById(Long id);

    Users updateUser(Long id, Users user);

    void deleteUser(Long id);
}
