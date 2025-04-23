package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.RoomInnerDTO;
import org.example.dto.RoomOuterDTO;
import org.example.entity.Booking;
import org.example.entity.Room;
import org.example.entity.Status;
import org.example.mapper.RoomMapper;
import org.example.repository.BookingRepository;
import org.example.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final RoomMapper mapper;

    public RoomOuterDTO create(RoomInnerDTO roomInnerDTO) {
        if (roomRepository.existsRoomByName(roomInnerDTO.name())){
            throw new RuntimeException("Комната с название: \"%s\" уже существует"
                    .formatted(roomInnerDTO.name()));
        }
        Room newRoom = mapper.toEntity(roomInnerDTO);
        return mapper.toOuterDTO(roomRepository.save(newRoom));
    }

    public RoomOuterDTO update(int id, String name, String location) {
        Room updateRoom = roomRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Комнаты с id: %s не существует".formatted(id)));
        updateRoom.setName(name.isEmpty() ? updateRoom.getName() : name);
        updateRoom.setLocation(location.isEmpty() ? updateRoom.getLocation() : location);
        return mapper.toOuterDTO(roomRepository.save(updateRoom));
    }

    public List<RoomOuterDTO> findAllAvailable(LocalDateTime desiredTime){
        if(desiredTime == null)
            return mapper.toOuterDTO(roomRepository.findAll());
        List<Room> availableRooms = bookingRepository.findAll().stream()
                .filter(booking -> booking.getStatus() != Status.CONFIRMED
                        || booking.getEndTime().isBefore(desiredTime))
                .map(Booking::getRoom)
                .collect(Collectors.toList());
        return mapper.toOuterDTO(availableRooms);
    }

    public void deleteById(int id) {
        roomRepository.deleteById(id);
    }

    public RoomOuterDTO findById(int id) {
        return mapper.toOuterDTO(roomRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Комнаты с id: %s не существует".formatted(id))));
    }
}
