package org.example.mapper;

import org.example.dto.RoomInnerDTO;
import org.example.dto.RoomOuterDTO;
import org.example.entity.Room;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {
    RoomOuterDTO toOuterDTO(Room room);

    List<RoomOuterDTO> toOuterDTO(List<Room> rooms);

    Room toEntity(RoomInnerDTO innerDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoom(RoomInnerDTO innerDTO, @MappingTarget Room room);
}
