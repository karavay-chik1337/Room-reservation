package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.RoomInnerDTO;
import org.example.dto.RoomOuterDTO;
import org.example.entity.Room;
import org.example.entity.Status;
import org.example.mapper.RoomMapper;
import org.example.repository.BookingRepository;
import org.example.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
        return mapper.toOuterDTO(roomRepository.saveAndFlush(newRoom));
    }

    public RoomOuterDTO findById(int id) {
        return mapper.toOuterDTO(roomRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Комнаты с id: %s не существует".formatted(id))));
    }

    public List<RoomOuterDTO> findAllAvailable(LocalDateTime desiredTime) {
        List<Room> availableRooms = roomRepository.findAll();

        if (desiredTime == null)
            return mapper.toOuterDTO(availableRooms);
        //Получаем id недоступных комнат
        Set<Integer> inaccessibleRoomIds = bookingRepository.findAll().stream()
                .filter(booking -> booking.getStatus() == Status.CONFIRMED)
                .filter(booking -> booking.getEndTime().isAfter(desiredTime))
                .map(booking -> booking.getRoom().getId())
                .collect(Collectors.toSet());

        availableRooms.removeIf(room -> inaccessibleRoomIds.contains(room.getId()));

        return mapper.toOuterDTO(availableRooms);
    }

    public RoomOuterDTO update(int id, RoomInnerDTO innerDTO) {
        Room updateRoom = roomRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Комнаты с id: %s не существует".formatted(id)));
        mapper.updateRoom(innerDTO, updateRoom);
        return mapper.toOuterDTO(roomRepository.saveAndFlush(updateRoom));
    }

    public void deleteById(int id) {
        roomRepository.deleteById(id);
    }

}
