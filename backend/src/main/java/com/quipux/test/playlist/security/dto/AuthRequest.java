package com.quipux.test.playlist.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Chamorro
 */
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class AuthRequest {
    
    private String username;
    
    private String password;
    
}