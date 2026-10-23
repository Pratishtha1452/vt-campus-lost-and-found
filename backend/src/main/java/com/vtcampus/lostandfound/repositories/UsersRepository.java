package com.vtcampus.lostandfound.repositories;

import com.vtcampus.lostandfound.domain.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
