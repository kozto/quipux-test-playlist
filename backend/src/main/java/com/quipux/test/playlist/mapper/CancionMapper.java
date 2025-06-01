package com.quipux.test.playlist.mapper;

import com.quipux.test.playlist.dto.CancionDto;
import com.quipux.test.playlist.model.Cancion;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Oscar Chamorro
 */
@Mapper(componentModel = "spring")
public interface CancionMapper {

    CancionDto toDTO(Cancion entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "listaReproduccion", ignore = true)
    Cancion toEntity(CancionDto dto);

    List<CancionDto> toDTOList(List<Cancion> entities);
    
    List<Cancion> toEntityList(List<CancionDto> dtos);
    
}

