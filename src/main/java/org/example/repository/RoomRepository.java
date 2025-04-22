package org.example.repository;

import org.example.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    boolean existsRoomByName(String name);
}
