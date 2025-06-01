package com.quipux.test.playlist.security.repository;

import com.quipux.test.playlist.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @author Oscar Chamorro
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Boolean existsByUsername(String username);
    
}