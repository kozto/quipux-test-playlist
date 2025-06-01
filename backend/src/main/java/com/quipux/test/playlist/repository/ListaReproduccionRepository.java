package com.quipux.test.playlist.repository;

import com.quipux.test.playlist.model.ListaReproduccion;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Oscar Chamorro
 */
@Repository
public interface ListaReproduccionRepository extends JpaRepository<ListaReproduccion, Long> {

    Optional<ListaReproduccion> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    void deleteByNombre(String nombre);

}
