package com.quipux.test.playlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Oscar Chamorro
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlaylistNotFoundException extends RuntimeException {
    
    public PlaylistNotFoundException(String message) {
        super(message);
    }
    
}
