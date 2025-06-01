package com.quipux.test.playlist.controller;

import com.quipux.test.playlist.dto.ListaReproduccionDto;
import com.quipux.test.playlist.service.ListaReproduccionService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author Oscar Chamorro
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/lists")
public class ListaReproduccionController {

    @Autowired
    private ListaReproduccionService listaReproduccionService;

//    @GetMapping("/autenticacion")
//    public AutenticacionBean autenticacion() {
//        return new AutenticacionBean("Autenticación exitosa");
//    }

    
    @GetMapping
    public ResponseEntity<List<ListaReproduccionDto>> getAllListasReproduccion() {
        
        List<ListaReproduccionDto> listasDto = listaReproduccionService.getAllListasReproduccion();
        
        return ResponseEntity.ok(listasDto);
    }
    
    @GetMapping("/id/{id}")
    public ResponseEntity<ListaReproduccionDto> getListaReproduccionById(@PathVariable Long id) {
        
        ListaReproduccionDto listaDto = listaReproduccionService.getListaReproduccionById(id);
        
        return ResponseEntity.ok(listaDto);
    }
    
    @GetMapping("/{listName}")
    public ResponseEntity<ListaReproduccionDto> getListaReproduccionByNombre(@PathVariable String listName) {
        
        ListaReproduccionDto listaDto = listaReproduccionService.getListaReproduccionByNombre(listName);
        
        return ResponseEntity.ok(listaDto);
    }
    
    // Trae unicamente la descripcion de una lista de reproduccion segun su nombre
    @GetMapping("/{listName}/descripcion")
    public ResponseEntity<String> getDescripcionByNombre(@PathVariable String listName) {
        
        String nombreLista = listaReproduccionService.getDescripcionByNombre(listName);
        
        return ResponseEntity.ok(nombreLista);
    }    
    
    @PostMapping
    public ResponseEntity<ListaReproduccionDto> saveListaReproduccion(@Valid @RequestBody ListaReproduccionDto listaDto) {

        ListaReproduccionDto savedListaDto = listaReproduccionService.saveListaReproduccion(listaDto);

        // URI del recurso creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/lists")
                .buildAndExpand(savedListaDto.getNombre())
                .toUri();

        return ResponseEntity.created(location).body(savedListaDto);
    }
    
    @DeleteMapping("/{listName}")
    public ResponseEntity<Void> deleteListaByName(@PathVariable String listName) {
        
        listaReproduccionService.deleteListaReproduccionByNombre(listName);
        
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    
    // Se aconseja eliminar por ID
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteListaReproduccionById(@PathVariable Long id) {
        
        listaReproduccionService.deleteListaReproduccionById(id);
        
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
