package com.quipux.test.playlist.mapper;

import com.quipux.test.playlist.dto.ListaReproduccionDto;
import com.quipux.test.playlist.model.ListaReproduccion;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author Oscar Chamorro
 */
@Mapper(componentModel = "spring", uses = {CancionMapper.class})
public interface ListaReproduccionMapper {
    
    ListaReproduccionDto toDto(ListaReproduccion entity);

    @Mapping(target = "id", ignore = true)
    ListaReproduccion toEntity(ListaReproduccionDto dto);

    List<ListaReproduccionDto> toDtoList(List<ListaReproduccion> entities);
    
    List<ListaReproduccion> toEntityList(List<ListaReproduccionDto> dtos);
    
    // Este metodo se asegura de que la relación bidireccional se establezca correctamente
    // despues de que MapStruct haya mapeado los campos basicos.  
    @AfterMapping
    default void establishBidirectionalRelationships(@MappingTarget ListaReproduccion listaReproduccion, ListaReproduccionDto dto) {
        
        if (listaReproduccion.getCanciones() != null && !listaReproduccion.getCanciones().isEmpty()) {
            listaReproduccion.getCanciones()
                .forEach(cancion -> cancion.setListaReproduccion(listaReproduccion));
        }
    }
    
}
