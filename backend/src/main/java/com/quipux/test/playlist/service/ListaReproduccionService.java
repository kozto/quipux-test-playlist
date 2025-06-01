package com.quipux.test.playlist.service;

import com.quipux.test.playlist.dto.ListaReproduccionDto;
import com.quipux.test.playlist.model.ListaReproduccion;
import java.util.List;

/**
 * @author Oscar Chamorro
 */
public interface ListaReproduccionService {
    
    public List<ListaReproduccionDto> getAllListasReproduccion();

    public ListaReproduccionDto getListaReproduccionById(Long id);
    
    public ListaReproduccionDto saveListaReproduccion(ListaReproduccionDto listaReproduccionDto);
    
    public ListaReproduccionDto getListaReproduccionByNombre(String nombre);
    
    public String getDescripcionByNombre(String nombre);
    
    public void deleteListaReproduccionByNombre(String nombre);
    
    public void deleteListaReproduccionById(Long id);
 
}
