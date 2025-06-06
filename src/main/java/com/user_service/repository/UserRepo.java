package com.user_service.repository;

import com.user_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
