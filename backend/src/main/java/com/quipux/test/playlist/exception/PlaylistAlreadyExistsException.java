package com.quipux.test.playlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Oscar Chamorro
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // CONFLICT (409)
public class PlaylistAlreadyExistsException extends RuntimeException {
    
    public PlaylistAlreadyExistsException(String message) {
        super(message);
    }
    
}
