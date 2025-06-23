package com.surfer.codes.url_shortener.domain.repositories;

import com.surfer.codes.url_shortener.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
