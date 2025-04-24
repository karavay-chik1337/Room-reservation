package org.example.mapper;

import org.example.dto.BookingOuterDTO;
import org.example.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    @Mapping(target = "userId", expression = "java(booking.getUser().getId())")
    @Mapping(target = "roomId", expression = "java(booking.getRoom().getId())")
    BookingOuterDTO toOuterDTO(Booking booking);

    List<BookingOuterDTO> toOuterDTO(List<Booking> bookings);

}
