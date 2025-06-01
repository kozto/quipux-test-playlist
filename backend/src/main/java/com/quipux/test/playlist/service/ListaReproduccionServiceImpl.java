package com.quipux.test.playlist.service;

import com.quipux.test.playlist.mapper.ListaReproduccionMapper;
import com.quipux.test.playlist.dto.ListaReproduccionDto;
import com.quipux.test.playlist.exception.PlaylistAlreadyExistsException;
import com.quipux.test.playlist.exception.PlaylistNotFoundException;
import com.quipux.test.playlist.model.ListaReproduccion;
import com.quipux.test.playlist.repository.ListaReproduccionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oscar Chamorro
 */
@Service
public class ListaReproduccionServiceImpl implements ListaReproduccionService {

    @Autowired
    private ListaReproduccionRepository listaReproduccionRepository;
    
    @Autowired
    private ListaReproduccionMapper listaReproduccionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ListaReproduccionDto> getAllListasReproduccion() {
        
        List<ListaReproduccion> listas = listaReproduccionRepository.findAll();
        
        return listaReproduccionMapper.toDtoList(listas);
    }

    @Override
    @Transactional(readOnly = true)
    public ListaReproduccionDto getListaReproduccionById(Long id) {
        
        ListaReproduccion lista = listaReproduccionRepository.findById(id)
                .orElseThrow(() -> new PlaylistNotFoundException("Lista no encontrada con el id: " + id));
        
        return listaReproduccionMapper.toDto(lista);
    }
    
    @Override
    @Transactional
    public ListaReproduccionDto saveListaReproduccion(ListaReproduccionDto listaDto) {
        
        //Dos listas no pueden tener el mismo nombre
        if (listaReproduccionRepository.existsByNombre(listaDto.getNombre())) {
            throw new PlaylistAlreadyExistsException("Ya existe una lista con el nombre: " + listaDto.getNombre());
        }

        ListaReproduccion lista = listaReproduccionMapper.toEntity(listaDto);
        ListaReproduccion savedLista = listaReproduccionRepository.save(lista);
        
        return listaReproduccionMapper.toDto(savedLista);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ListaReproduccionDto getListaReproduccionByNombre(String nombre) {
        
        ListaReproduccion lista = listaReproduccionRepository.findByNombre(nombre)
                .orElseThrow(() -> new PlaylistNotFoundException("Lista no encontrada con el nombre: " + nombre));
        
        return listaReproduccionMapper.toDto(lista);
    }
    
    @Override
    @Transactional(readOnly = true)
    public String getDescripcionByNombre(String nombre) {
        
        ListaReproduccion lista = listaReproduccionRepository.findByNombre(nombre)
                .orElseThrow(() -> new PlaylistNotFoundException("Lista no encontrada con el nombre: " + nombre));
        
        return (lista.getDescripcion() == null) ? "" : lista.getDescripcion();
    }
    
    @Override
    @Transactional
    public void deleteListaReproduccionByNombre(String nombre) {
        
        ListaReproduccion lista = listaReproduccionRepository.findByNombre(nombre)
                .orElseThrow(() -> new PlaylistNotFoundException("Lista no encontrada con el nombre: " + nombre));
        
        listaReproduccionRepository.deleteById(lista.getId()); // Es mas seguro eliminar por ID una vez encontrada la lista
    }
    
    //Metodo extra, las eliminaciones deberian procesarse con ID
    @Override
    public void deleteListaReproduccionById(Long id) {
        
        if (!listaReproduccionRepository.existsById(id)) {
            throw new PlaylistNotFoundException("No se puede eliminar. Lista no encontrada con el id: " + id);
        }
        
        listaReproduccionRepository.deleteById(id);
    }

}
