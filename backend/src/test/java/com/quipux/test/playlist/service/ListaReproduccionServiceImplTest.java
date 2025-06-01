package com.quipux.test.playlist.service;

import com.quipux.test.playlist.dto.CancionDto;
import com.quipux.test.playlist.dto.ListaReproduccionDto;
import com.quipux.test.playlist.exception.PlaylistAlreadyExistsException;
import com.quipux.test.playlist.exception.PlaylistNotFoundException;
import com.quipux.test.playlist.mapper.ListaReproduccionMapper;
import com.quipux.test.playlist.model.Cancion;
import com.quipux.test.playlist.model.ListaReproduccion;
import com.quipux.test.playlist.repository.ListaReproduccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given; // BDDMockito para un estilo Given-When-Then
import static org.mockito.BDDMockito.willDoNothing; // Para métodos void
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Oscar Chamorro
 */
@ExtendWith(MockitoExtension.class) // Se habilita el uso de anotaciones Mockito
class ListaReproduccionServiceImplTest {

    @Mock
    private ListaReproduccionRepository listaReproduccionRepository;

    @Mock
    private ListaReproduccionMapper listaReproduccionMapper;

    @InjectMocks // Se crea una instancia de ListaReproduccionServiceImpl y se inyectan los mocks anteriores
    private ListaReproduccionServiceImpl listaReproduccionService;

    private ListaReproduccion listaReproduccion1;
    private ListaReproduccionDto listaReproduccionDto1;
    private ListaReproduccion listaReproduccion2;
    private ListaReproduccionDto listaReproduccionDto2;

    @BeforeEach
    void setUp() {
        
        // Configuración inicial de objetos de prueba

        // Lista 1
        listaReproduccion1 = new ListaReproduccion();
        listaReproduccion1.setId(1L);
        listaReproduccion1.setNombre("Rock en Español");
        listaReproduccion1.setDescripcion("Grandes exitos del rock en tu idioma");
        Cancion cancion1 = new Cancion(101L, "De Musica Ligera", "Soda Stereo", "Cancion Animal", 1990, "Rock", listaReproduccion1);
        Cancion cancion2 = new Cancion(102L, "Lamento Boliviano", "Enanitos Verdes", "Big Bang", 1994, "Rock", listaReproduccion1);
        listaReproduccion1.setCanciones(Arrays.asList(cancion1, cancion2));

        // Lista DTO 1
        listaReproduccionDto1 = new ListaReproduccionDto();
        listaReproduccionDto1.setId(1L);
        listaReproduccionDto1.setNombre("Rock en Español");
        listaReproduccionDto1.setDescripcion("Grandes exitos del rock en tu idioma");
        CancionDto cancionDto1 = new CancionDto(1L, "De Musica Ligera", "Soda Stereo", "Cancion Animal", 1990, "Rock");
        CancionDto cancionDto2 = new CancionDto(2L, "Lamento Boliviano", "Enanitos Verdes", "Big Bang", 1994, "Rock");
        listaReproduccionDto1.setCanciones(Arrays.asList(cancionDto1, cancionDto2));

        // Lista 2 (para pruebas de múltiples listas)
        listaReproduccion2 = new ListaReproduccion();
        listaReproduccion2.setId(2L);
        listaReproduccion2.setNombre("Pop Hits");
        listaReproduccion2.setDescripcion("Lo mejor del Pop actual");
        listaReproduccion2.setCanciones(new ArrayList<>());

        // Lista DTO 2
        listaReproduccionDto2 = new ListaReproduccionDto();
        listaReproduccionDto2.setId(2L);
        listaReproduccionDto2.setNombre("Pop Hits");
        listaReproduccionDto2.setDescripcion("Lo mejor del Pop actual");
        listaReproduccionDto2.setCanciones(new ArrayList<>());
    }

    // Pruebas para getAllListasReproduccion
    @DisplayName("JUnit test para getAllListasReproduccion cuando existen listas")
    @Test
    void givenListasExist_whenGetAllListasReproduccion_thenReturnListOfListaReproduccionDto() {
        
        // Given
        given(listaReproduccionRepository.findAll()).willReturn(Arrays.asList(listaReproduccion1, listaReproduccion2));
        given(listaReproduccionMapper.toDtoList(Arrays.asList(listaReproduccion1, listaReproduccion2)))
                .willReturn(Arrays.asList(listaReproduccionDto1, listaReproduccionDto2));

        // When
        List<ListaReproduccionDto> result = listaReproduccionService.getAllListasReproduccion();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Rock en Español", result.get(0).getNombre());
        assertEquals("Pop Hits", result.get(1).getNombre());
        verify(listaReproduccionRepository, times(1)).findAll();
        verify(listaReproduccionMapper, times(1)).toDtoList(any());
    }

    @DisplayName("JUnit test para getAllListasReproduccion cuando no existen listas")
    @Test
    void givenNoListasExist_whenGetAllListasReproduccion_thenReturnEmptyList() {
        
        // Given
        given(listaReproduccionRepository.findAll()).willReturn(Collections.emptyList());
        given(listaReproduccionMapper.toDtoList(Collections.emptyList())).willReturn(Collections.emptyList());

        // When
        List<ListaReproduccionDto> result = listaReproduccionService.getAllListasReproduccion();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(listaReproduccionRepository, times(1)).findAll();
        verify(listaReproduccionMapper, times(1)).toDtoList(Collections.emptyList());
    }


    // Pruebas para getListaReproduccionById
    @DisplayName("JUnit test para getListaReproduccionById cuando la lista existe")
    @Test
    void givenExistingId_whenGetListaReproduccionById_thenReturnListaReproduccionDto() {
        
        // Given
        given(listaReproduccionRepository.findById(1L)).willReturn(Optional.of(listaReproduccion1));
        given(listaReproduccionMapper.toDto(listaReproduccion1)).willReturn(listaReproduccionDto1);

        // When
        ListaReproduccionDto resultDto = listaReproduccionService.getListaReproduccionById(1L);

        // Then
        assertNotNull(resultDto);
        assertEquals(1L, resultDto.getId());
        assertEquals("Rock en Español", resultDto.getNombre());
        verify(listaReproduccionRepository, times(1)).findById(1L);
        verify(listaReproduccionMapper, times(1)).toDto(listaReproduccion1);
    }

    @DisplayName("JUnit test para getListaReproduccionById cuando la lista NO existe")
    @Test
    void givenNonExistingId_whenGetListaReproduccionById_thenThrowPlaylistNotFoundException() {
        
        // Given
        long nonExistingId = 99L;
        given(listaReproduccionRepository.findById(nonExistingId)).willReturn(Optional.empty());

        // When & Then
        PlaylistNotFoundException exception = assertThrows(PlaylistNotFoundException.class, () -> {
            listaReproduccionService.getListaReproduccionById(nonExistingId);
        });
        assertEquals("Lista no encontrada con el id: " + nonExistingId, exception.getMessage());
        verify(listaReproduccionRepository, times(1)).findById(nonExistingId);
        verify(listaReproduccionMapper, never()).toDto(any(ListaReproduccion.class));
    }

    // Pruebas para saveListaReproduccion
    @DisplayName("JUnit test para saveListaReproduccion caso exitoso")
    @Test
    void givenValidListaDto_whenSaveListaReproduccion_thenReturnSavedListaDto() {
        
        // Given
        ListaReproduccionDto newListaDto = new ListaReproduccionDto();
        newListaDto.setNombre("Nueva Playlist");
        newListaDto.setDescripcion("Descripcion de la nueva playlist");
        newListaDto.setCanciones(new ArrayList<>());

        ListaReproduccion newListaEntity = new ListaReproduccion();
        newListaEntity.setNombre("Nueva Playlist");
        newListaEntity.setDescripcion("Descripcion de la nueva playlist");

        ListaReproduccion savedListaEntity = new ListaReproduccion();
        savedListaEntity.setId(3L); // ID generado por la BD
        savedListaEntity.setNombre("Nueva Playlist");
        savedListaEntity.setDescripcion("Descripcion de la nueva playlist");
        
        ListaReproduccionDto savedListaDtoResult = new ListaReproduccionDto();
        savedListaDtoResult.setId(3L);
        savedListaDtoResult.setNombre("Nueva Playlist");
        savedListaDtoResult.setDescripcion("Descripcion de la nueva playlist");

        given(listaReproduccionRepository.existsByNombre(newListaDto.getNombre())).willReturn(false);
        given(listaReproduccionMapper.toEntity(newListaDto)).willReturn(newListaEntity);
        given(listaReproduccionRepository.save(newListaEntity)).willReturn(savedListaEntity);
        given(listaReproduccionMapper.toDto(savedListaEntity)).willReturn(savedListaDtoResult);

        // When
        ListaReproduccionDto result = listaReproduccionService.saveListaReproduccion(newListaDto);

        // Then
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Nueva Playlist", result.getNombre());
        verify(listaReproduccionRepository, times(1)).existsByNombre("Nueva Playlist");
        verify(listaReproduccionMapper, times(1)).toEntity(newListaDto);
        verify(listaReproduccionRepository, times(1)).save(newListaEntity);
        verify(listaReproduccionMapper, times(1)).toDto(savedListaEntity);
    }

    @DisplayName("JUnit test para saveListaReproduccion cuando el nombre ya existe")
    @Test
    void givenExistingName_whenSaveListaReproduccion_thenThrowPlaylistAlreadyExistsException() {
        
        // Given
        given(listaReproduccionRepository.existsByNombre(listaReproduccionDto1.getNombre())).willReturn(true);

        // When & Then
        PlaylistAlreadyExistsException exception = assertThrows(PlaylistAlreadyExistsException.class, () -> {
            listaReproduccionService.saveListaReproduccion(listaReproduccionDto1);
        });

        assertEquals("Ya existe una lista con el nombre: " + listaReproduccionDto1.getNombre(), exception.getMessage());
        verify(listaReproduccionRepository, times(1)).existsByNombre(listaReproduccionDto1.getNombre());
        verify(listaReproduccionMapper, never()).toEntity(any());
        verify(listaReproduccionRepository, never()).save(any());
        verify(listaReproduccionMapper, never()).toDto(any());
    }

    // Pruebas para getListaReproduccionByNombre 
    @DisplayName("JUnit test para getListaReproduccionByNombre cuando la lista existe")
    @Test
    void givenExistingNombre_whenGetListaReproduccionByNombre_thenReturnListaDto() {
        
        // Given
        given(listaReproduccionRepository.findByNombre("Rock en Español")).willReturn(Optional.of(listaReproduccion1));
        given(listaReproduccionMapper.toDto(listaReproduccion1)).willReturn(listaReproduccionDto1);

        // When
        ListaReproduccionDto result = listaReproduccionService.getListaReproduccionByNombre("Rock en Español");

        // Then
        assertNotNull(result);
        assertEquals("Rock en Español", result.getNombre());
        verify(listaReproduccionRepository).findByNombre("Rock en Español");
        verify(listaReproduccionMapper).toDto(listaReproduccion1);
    }
    
    @DisplayName("JUnit test para getListaReproduccionByNombre cuando la lista NO existe")
    @Test
    void givenNonExistingNombre_whenGetListaReproduccionByNombre_thenThrowPlaylistNotFoundException() {
        
        // Given
        String nombreInexistente = "Jazz Fusion";
        given(listaReproduccionRepository.findByNombre(nombreInexistente)).willReturn(Optional.empty());

        // When & Then
        assertThrows(PlaylistNotFoundException.class, () -> {
            listaReproduccionService.getListaReproduccionByNombre(nombreInexistente);
        });
        verify(listaReproduccionRepository).findByNombre(nombreInexistente);
        verify(listaReproduccionMapper, never()).toDto(any());
    }

    // Pruebas para getDescripcionByNombre
    @DisplayName("JUnit test para getDescripcionByNombre cuando lista existe y descripcion NO es null")
    @Test
    void givenExistingListWithDescription_whenGetDescripcionByNombre_thenReturnDescriptionString() {
        
        // Given
        given(listaReproduccionRepository.findByNombre("Rock en Español")).willReturn(Optional.of(listaReproduccion1));
        // listaReproduccion1.getDescripcion() es "Grandes exitos del rock en tu idioma"

        // When
        String descripcion = listaReproduccionService.getDescripcionByNombre("Rock en Español");

        // Then
        assertEquals("Grandes exitos del rock en tu idioma", descripcion);
        verify(listaReproduccionRepository).findByNombre("Rock en Español");
    }

    @DisplayName("JUnit test para getDescripcionByNombre cuando lista existe y descripción ES null")
    @Test
    void givenExistingListWithNullDescription_whenGetDescripcionByNombre_thenReturnEmptyString() {
        
        // Given
        listaReproduccion1.setDescripcion(null); // Modificamos para este test
        given(listaReproduccionRepository.findByNombre("Rock en Español")).willReturn(Optional.of(listaReproduccion1));
        
        // When
        String descripcion = listaReproduccionService.getDescripcionByNombre("Rock en Español");

        // Then
        assertEquals("", descripcion); // Se espera una cadena vacía
        verify(listaReproduccionRepository).findByNombre("Rock en Español");
    }

    @DisplayName("JUnit test para getDescripcionByNombre cuando lista NO existe")
    @Test
    void givenNonExistingList_whenGetDescripcionByNombre_thenThrowPlaylistNotFoundException() {
        
         // Given
        String nombreInexistente = "Jazz Fusion";
        given(listaReproduccionRepository.findByNombre(nombreInexistente)).willReturn(Optional.empty());

        // When & Then
        assertThrows(PlaylistNotFoundException.class, () -> {
            listaReproduccionService.getDescripcionByNombre(nombreInexistente);
        });
        verify(listaReproduccionRepository).findByNombre(nombreInexistente);
    }

    // Pruebas para deleteListaReproduccionByNombre
    @DisplayName("JUnit test para deleteListaReproduccionByNombre caso exitoso")
    @Test
    void givenExistingNombre_whenDeleteListaReproduccionByNombre_thenDeletesSuccessfully() {
        // Given
        given(listaReproduccionRepository.findByNombre("Rock en Español")).willReturn(Optional.of(listaReproduccion1));
        willDoNothing().given(listaReproduccionRepository).deleteById(listaReproduccion1.getId());

        // When
        assertDoesNotThrow(() -> listaReproduccionService.deleteListaReproduccionByNombre("Rock en Español"));

        // Then
        verify(listaReproduccionRepository).findByNombre("Rock en Español");
        verify(listaReproduccionRepository).deleteById(listaReproduccion1.getId());
    }

    @DisplayName("JUnit test para deleteListaReproduccionByNombre cuando lista NO existe")
    @Test
    void givenNonExistingNombre_whenDeleteListaReproduccionByNombre_thenThrowPlaylistNotFoundException() {
        
        // Given
        String nombreInexistente = "Jazz Fusion";
        given(listaReproduccionRepository.findByNombre(nombreInexistente)).willReturn(Optional.empty());

        // When & Then
        assertThrows(PlaylistNotFoundException.class, () -> {
            listaReproduccionService.deleteListaReproduccionByNombre(nombreInexistente);
        });
        verify(listaReproduccionRepository).findByNombre(nombreInexistente);
        verify(listaReproduccionRepository, never()).deleteById(anyLong());
    }


    // Pruebas para deleteListaReproduccionById
    @DisplayName("JUnit test para deleteListaReproduccionById caso exitoso")
    @Test
    void givenExistingId_whenDeleteListaReproduccionById_thenDeletesSuccessfully() {
        
        // Given
        long existingId = 1L;
        given(listaReproduccionRepository.existsById(existingId)).willReturn(true);
        willDoNothing().given(listaReproduccionRepository).deleteById(existingId);

        // When
        assertDoesNotThrow(() -> listaReproduccionService.deleteListaReproduccionById(existingId));

        // Then
        verify(listaReproduccionRepository).existsById(existingId);
        verify(listaReproduccionRepository).deleteById(existingId);
    }
    
    @DisplayName("JUnit test para deleteListaReproduccionById cuando lista NO existe")
    @Test
    void givenNonExistingId_whenDeleteListaReproduccionById_thenThrowPlaylistNotFoundException() {
        
        // Given
        long nonExistingId = 99L;
        given(listaReproduccionRepository.existsById(nonExistingId)).willReturn(false);

        // When & Then
        assertThrows(PlaylistNotFoundException.class, () -> {
            listaReproduccionService.deleteListaReproduccionById(nonExistingId);
        });
        verify(listaReproduccionRepository).existsById(nonExistingId);
        verify(listaReproduccionRepository, never()).deleteById(anyLong());
    }
    
}
