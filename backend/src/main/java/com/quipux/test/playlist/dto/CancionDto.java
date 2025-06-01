package com.quipux.test.playlist.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * @author Oscar Chamorro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancionDto {
    
    private Long id;
    private String titulo;
    private String artista;
    private String album;
    private String anno;
    private String genero;
    
}
