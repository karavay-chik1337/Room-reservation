package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.RoomDTO;
import org.example.entity.Room;
import org.example.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room create(RoomDTO roomDTO) {
        if (roomRepository.existsRoomByName(roomDTO.name())){
            throw new RuntimeException("Комната с название: \"%s\" уже существует"
                    .formatted(roomDTO.name()));
        }
        Room newRoom = new Room();
        newRoom.setName(roomDTO.name());
        newRoom.setLocation(roomDTO.location());
        return roomRepository.save(newRoom);
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public void deleteById(int id) {
        roomRepository.deleteById(id);
    }

    public Room findById(int id) {
        return roomRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Комнаты с id: %s не существует".formatted(id)));
    }
}
