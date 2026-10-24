
package com.vtcampus.lostandfound.services;

import com.vtcampus.lostandfound.domain.entities.Users;

public interface AuthService {

    Users login(String email, String password);
}
