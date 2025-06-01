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
public class AuthResponse {
    
    private String token;
    
    private String type = "Bearer";
    
     public AuthResponse(String token) {
        this.token = token;
    }
    
}