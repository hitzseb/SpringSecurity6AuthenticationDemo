package com.hitzseb.jwtSpringSecurity6demo.repository;

import com.hitzseb.jwtSpringSecurity6demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
