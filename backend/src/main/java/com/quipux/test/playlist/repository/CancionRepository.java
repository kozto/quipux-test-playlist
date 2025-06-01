package com.quipux.test.playlist.repository;

import com.quipux.test.playlist.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Oscar Chamorro
 */
@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

}