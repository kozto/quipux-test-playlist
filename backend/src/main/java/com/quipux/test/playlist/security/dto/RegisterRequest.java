package com.quipux.test.playlist.security.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Chamorro
 */
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class RegisterRequest {
    
    private String username;
    
    private String password;
    
    private Set<String> roles;
    
}