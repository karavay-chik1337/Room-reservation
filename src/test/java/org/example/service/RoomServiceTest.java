package org.example.service;

import org.example.dto.RoomInnerDTO;
import org.example.dto.RoomOuterDTO;
import org.example.entity.Booking;
import org.example.entity.Room;
import org.example.entity.Status;
import org.example.entity.User;
import org.example.mapper.RoomMapper;
import org.example.repository.BookingRepository;
import org.example.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @InjectMocks
    RoomService roomService;
    @Mock
    RoomRepository roomRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    RoomMapper mapper;

    int roomId = 1;
    RoomInnerDTO innerDTO = new RoomInnerDTO("Переговорная", "Воронеж");
    Room room = new Room(roomId, innerDTO.name(), innerDTO.location(), Set.of());
    RoomOuterDTO outerDTO = new RoomOuterDTO(roomId, room.getName(), room.getLocation());

    @Test
    void create_success() {
        when(roomRepository.existsRoomByName(innerDTO.name())).thenReturn(false);
        when(mapper.toEntity(innerDTO)).thenReturn(room);
        when(roomRepository.saveAndFlush(any(Room.class))).thenReturn(room);
        when(mapper.toOuterDTO(room)).thenReturn(outerDTO);

        assertEquals(outerDTO, roomService.create(innerDTO));
        verify(roomRepository).existsRoomByName(innerDTO.name());
        verify(roomRepository).saveAndFlush(any(Room.class));
        verify(mapper).toEntity(innerDTO);
        verify(mapper).toOuterDTO(room);
        verifyNoMoreInteractions(roomRepository, mapper);
    }

    @Test
    void create_roomAlreadyExists_throwException() {
        when(roomRepository.existsRoomByName(innerDTO.name())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> roomService.create(innerDTO));
        verify(roomRepository).existsRoomByName(innerDTO.name());
        verifyNoMoreInteractions(roomRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void findById_success() {
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(mapper.toOuterDTO(room)).thenReturn(outerDTO);

        assertEquals(outerDTO, roomService.findById(roomId));
        verify(roomRepository).findById(roomId);
        verify(mapper).toOuterDTO(room);
        verifyNoMoreInteractions(mapper, roomRepository);
    }

    @Test
    void findById_roomNotFound_throwException() {
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> roomService.findById(roomId));
        verify(roomRepository).findById(roomId);
        verifyNoMoreInteractions(roomRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAllAvailable_desiredTimeIsNull_returnAll() {
        Room room1 = new Room(1, "какая-то1", "где-то", Set.of());
        Room room2 = new Room(2, "какая-то2", "где-то", Set.of());
        List<Room> rooms = List.of(room1, room2);

        RoomOuterDTO outerDTO1 = new RoomOuterDTO(room1.getId(), room1.getName(), room1.getLocation());
        RoomOuterDTO outerDTO2 = new RoomOuterDTO(room2.getId(), room2.getName(), room2.getLocation());
        List<RoomOuterDTO> roomOuterDTOS = List.of(outerDTO1, outerDTO2);

        when(roomRepository.findAll()).thenReturn(rooms);
        when(mapper.toOuterDTO(rooms)).thenReturn(roomOuterDTOS);

        assertEquals(roomOuterDTOS, roomService.findAllAvailable(null));
        verify(roomRepository).findAll();
        verify(mapper).toOuterDTO(rooms);
        verifyNoMoreInteractions(mapper, roomRepository);
    }

    @Test
    void findAllAvailable_desiredTimeIsNotNull_returnAvailable() {
        Room room1 = new Room(1, "какая-то1", "где-то", new HashSet<>());
        Room room2 = new Room(2, "какая-то2", "где-то", new HashSet<>());
        User user1 = new User(1, "dima", "karavaev", "dima@mail.ru", "финансовый");
        User user2 = new User(2, "sanya", "karavaev", "sanya@mail.ru", "финансовый");
        Booking booking1 = new Booking(1, room1, user1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Status.CONFIRMED);
        Booking booking2 = new Booking(2, room2, user2, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), Status.CONFIRMED);
        List<Booking> bookings = List.of(booking1, booking2);
        room1.getBookings().add(booking1);
        room2.getBookings().add(booking2);
        List<Room> rooms = new ArrayList<>(List.of(room1, room2));

        //RoomOuterDTO outerDTO1 = new RoomOuterDTO(room1.getId(), room1.getName(), room1.getLocation());
        RoomOuterDTO outerDTO2 = new RoomOuterDTO(room2.getId(), room2.getName(), room2.getLocation());
        List<RoomOuterDTO> roomOuterDTOS = List.of(outerDTO2);

        when(roomRepository.findAll()).thenReturn(rooms);
        when(bookingRepository.findAll()).thenReturn(bookings);
        when(mapper.toOuterDTO(rooms)).thenReturn(roomOuterDTOS);

        assertEquals(roomOuterDTOS, roomService.findAllAvailable(LocalDateTime.now().plusMinutes(30)));
        verify(roomRepository).findAll();
        verify(mapper).toOuterDTO(rooms);
        verifyNoMoreInteractions(mapper, roomRepository);
    }

    @Test
    void updateName_success() {
        RoomInnerDTO innerUpdateDTO = new RoomInnerDTO("какая-то", null);
        Room roomUpdate = new Room(roomId, innerUpdateDTO.name(), room.getLocation(), room.getBookings());
        RoomOuterDTO outerUpdateDTO = new RoomOuterDTO(roomUpdate.getId(), roomUpdate.getName(), roomUpdate.getLocation());

        when(roomRepository.existsRoomByName(innerUpdateDTO.name())).thenReturn(false);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(mapper.updateRoom(innerUpdateDTO, room)).thenReturn(roomUpdate);
        when(roomRepository.saveAndFlush(roomUpdate)).thenReturn(roomUpdate);
        when(mapper.toOuterDTO(roomUpdate)).thenReturn(outerUpdateDTO);

        assertEquals(outerUpdateDTO, roomService.update(roomId, innerUpdateDTO));
        verify(roomRepository).existsRoomByName(innerUpdateDTO.name());
        verify(roomRepository).findById(roomId);
        verify(mapper).updateRoom(innerUpdateDTO, room);
        verify(roomRepository).saveAndFlush(roomUpdate);
        verify(mapper).toOuterDTO(roomUpdate);
        verifyNoMoreInteractions(roomRepository, mapper);
    }

    @Test
    void updateName_roomNotFound_throwException() {
        RoomInnerDTO innerUpdateDTO = new RoomInnerDTO("какая-то", null);
        when(roomRepository.existsRoomByName(innerUpdateDTO.name())).thenReturn(false);
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> roomService.update(roomId, innerUpdateDTO));
        verify(roomRepository).existsRoomByName(innerUpdateDTO.name());
        verify(roomRepository).findById(roomId);
        verifyNoMoreInteractions(roomRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void updateName_roomAlreadyExists_throwException() {
        RoomInnerDTO innerUpdateDTO = new RoomInnerDTO("какая-то", null);
        when(roomRepository.existsRoomByName(innerUpdateDTO.name())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> roomService.update(roomId, innerUpdateDTO));
        verify(roomRepository).existsRoomByName(innerUpdateDTO.name());
        verifyNoMoreInteractions(roomRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void deleteById() {
        doNothing().when(roomRepository).deleteById(roomId);

        roomService.deleteById(roomId);

        verify(roomRepository).deleteById(roomId);
    }
}