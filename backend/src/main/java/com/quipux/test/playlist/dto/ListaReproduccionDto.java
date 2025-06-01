package com.quipux.test.playlist.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Chamorro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaReproduccionDto {
    
    private Long id;
    
    @NotBlank(message = "El nombre de la lista es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    private List<CancionDto> canciones;
    
}
