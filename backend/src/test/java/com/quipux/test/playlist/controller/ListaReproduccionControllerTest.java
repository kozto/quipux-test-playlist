package com.quipux.test.playlist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quipux.test.playlist.dto.CancionDto;
import com.quipux.test.playlist.dto.ListaReproduccionDto;
import com.quipux.test.playlist.exception.PlaylistAlreadyExistsException;
import com.quipux.test.playlist.exception.PlaylistNotFoundException;
import com.quipux.test.playlist.service.ListaReproduccionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Oscar Chamorro
 */
@WebMvcTest(ListaReproduccionController.class)
class ListaReproduccionControllerTest {

    @Autowired
    private MockMvc mockMvc; // Para simular peticiones HTTP

    @MockBean 
    private ListaReproduccionService listaReproduccionService;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON y viceversa

    private ListaReproduccionDto listaDto1;
    private ListaReproduccionDto listaDto2;
    private CancionDto cancionDto1;

    @BeforeEach
    void setUp() {
        
        cancionDto1 = new CancionDto(1L, "Cancion Test", "Artista Test", "Album Test", 2023, "Rock");

        listaDto1 = new ListaReproduccionDto();
        listaDto1.setId(1L);
        listaDto1.setNombre("Playlist Test 1");
        listaDto1.setDescripcion("Descripcion Test 1");
        listaDto1.setCanciones(Arrays.asList(cancionDto1));

        listaDto2 = new ListaReproduccionDto();
        listaDto2.setId(2L);
        listaDto2.setNombre("Playlist Test 2");
        listaDto2.setDescripcion("Descripcion Test 2");
        listaDto2.setCanciones(new ArrayList<>());
    }

    @DisplayName("GET /lists - obtener todas las listas")
    @Test
    void givenListOfListas_whenGetAllListas_thenReturnListasDtoList() throws Exception {
        
        // given
        List<ListaReproduccionDto> listas = Arrays.asList(listaDto1, listaDto2);
        given(listaReproduccionService.getAllListasReproduccion()).willReturn(listas);

        // when
        ResultActions response = mockMvc.perform(get("/lists"));

        // then
        response.andExpect(status().isOk())
                .andDo(print()) // Imprime la request/response para debug
                .andExpect(jsonPath("$.size()", is(listas.size())))
                .andExpect(jsonPath("$[0].nombre", is(listaDto1.getNombre())));
    }
    
    @DisplayName("GET /lists/id/{id} - obtener lista por ID exitoso")
    @Test
    void givenExistingId_whenGetListaById_thenReturnListaDto() throws Exception {
        
        // given
        long listaId = 1L;
        given(listaReproduccionService.getListaReproduccionById(listaId)).willReturn(listaDto1);

        // when
        ResultActions response = mockMvc.perform(get("/lists/id/{id}", listaId));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(listaDto1.getNombre())))
                .andExpect(jsonPath("$.descripcion", is(listaDto1.getDescripcion())));
    }

    @DisplayName("GET /lists/id/{id} - obtener lista por ID no encontrado")
    @Test
    void givenNonExistingId_whenGetListaById_thenReturnNotFound() throws Exception {
        
        // given
        long listaId = 99L;
        given(listaReproduccionService.getListaReproduccionById(listaId))
                .willThrow(new PlaylistNotFoundException("Lista no encontrada con el id: " + listaId));

        // when
        ResultActions response = mockMvc.perform(get("/lists/id/{id}", listaId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("GET /lists/{listName} - obtener lista por nombre exitoso")
    @Test
    void givenExistingNombre_whenGetListaByNombre_thenReturnListaDto() throws Exception {
        
        // given
        String listName = "Playlist Test 1";
        given(listaReproduccionService.getListaReproduccionByNombre(listName)).willReturn(listaDto1);
    
        // when
        ResultActions response = mockMvc.perform(get("/lists/{listName}", listName));
    
        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(listaDto1.getNombre())));
    }

    @DisplayName("GET /lists/{listName}/descripcion - obtener descripcion por nombre")
    @Test
    void givenExistingNombre_whenGetDescripcionByNombre_thenReturnStringDescripcion() throws Exception {
        
        // given
        String listName = "Playlist Test 1";
        String descripcion = "Descripcion Test 1";
        given(listaReproduccionService.getDescripcionByNombre(listName)).willReturn(descripcion);

        // when
        ResultActions response = mockMvc.perform(get("/lists/{listName}/descripcion", listName));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(descripcion));
    }

    @DisplayName("POST /lists - crear nueva lista exitoso")
    @Test
    void givenListaDto_whenSaveLista_thenReturnSavedListaDtoAndCreatedStatus() throws Exception {
        
        // given
        // El DTO de entrada no tendra ID
        ListaReproduccionDto inputDto = new ListaReproduccionDto(null, "Nueva Playlist", "Desc Nueva", Collections.emptyList());
        // El DTO devuelto por el servicio si tendra ID
        ListaReproduccionDto savedDto = new ListaReproduccionDto(3L, "Nueva Playlist", "Desc Nueva", Collections.emptyList());
        
        given(listaReproduccionService.saveListaReproduccion(any(ListaReproduccionDto.class)))
                .willReturn(savedDto);

        // when
        ResultActions response = mockMvc.perform(post("/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)));

        // then
        response.andExpect(status().isCreated()) // 201 Created
                .andDo(print())
                .andExpect(jsonPath("$.id", is(savedDto.getId().intValue())))
                .andExpect(jsonPath("$.nombre", is(savedDto.getNombre())))
                .andExpect(header().exists("Location")); // Verifica que el header Location exista
    }

    @DisplayName("POST /lists - crear lista con nombre existente")
    @Test
    void givenExistingNombre_whenSaveLista_thenReturnBadRequest() throws Exception {
        
        // given
        ListaReproduccionDto inputDto = new ListaReproduccionDto(null, "Playlist Existente", "Desc", Collections.emptyList());
        given(listaReproduccionService.saveListaReproduccion(any(ListaReproduccionDto.class)))
                .willThrow(new PlaylistAlreadyExistsException("Ya existe una lista con el nombre: " + inputDto.getNombre()));

        // when
        ResultActions response = mockMvc.perform(post("/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)));

        // then
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }
    
    @DisplayName("POST /lists - crear lista con nombre invalido (vacio)")
    @Test
    void givenInvalidNombre_whenSaveLista_thenReturnBadRequestFromValidation() throws Exception {
        
        // given
        // El nombre es obligatorio segun @NotBlank en ListaReproduccionDto
        ListaReproduccionDto inputDtoConNombreVacio = new ListaReproduccionDto(null, "", "Desc", Collections.emptyList());

        // NOTA: No mockeamos el servicio aqui porque la validacion @Valid deberia actuar ANTES de llamar al servicio.
        // Spring se encarga de esto.

        // when
        ResultActions response = mockMvc.perform(post("/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDtoConNombreVacio)));

        // then
        response.andExpect(status().isBadRequest()) // Bean Validation deberia resultar en 400
                .andDo(print());
                // Podrias verificar el mensaje de error si tu GlobalExceptionHandler lo formatea de una manera especifica
    }


    @DisplayName("DELETE /lists/{listName} - eliminar lista por nombre exitoso")
    @Test
    void givenExistingNombre_whenDeleteListaByNombre_thenReturnNoContent() throws Exception {

        // given
        String listName = "Playlist a Borrar";
        willDoNothing().given(listaReproduccionService).deleteListaReproduccionByNombre(listName);

        // when
        ResultActions response = mockMvc.perform(delete("/lists/{listName}", listName));

        // then
        response.andExpect(status().isNoContent()) // 204 No Content
                .andDo(print());
    }

    @DisplayName("DELETE /lists/{listName} - eliminar lista por nombre no encontrado")
    @Test
    void givenNonExistingNombre_whenDeleteListaByNombre_thenReturnNotFound() throws Exception {
        
        // given
        String listName = "Playlist Inexistente";
        doThrow(new PlaylistNotFoundException("No se puede eliminar. Lista no encontrada con el nombre: " + listName))
                .when(listaReproduccionService).deleteListaReproduccionByNombre(listName);

        // when
        ResultActions response = mockMvc.perform(delete("/lists/{listName}", listName));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
    
    @DisplayName("DELETE /lists/id/{id} - eliminar lista por ID exitoso")
    @Test
    void givenExistingId_whenDeleteListaById_thenReturnNoContent() throws Exception {
        
        // given
        long listaId = 1L;
        willDoNothing().given(listaReproduccionService).deleteListaReproduccionById(listaId);

        // when
        ResultActions response = mockMvc.perform(delete("/lists/id/{id}", listaId));

        // then
        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}
