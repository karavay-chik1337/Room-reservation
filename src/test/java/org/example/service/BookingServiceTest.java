package org.example.service;

import org.example.dto.BookingInnerDTO;
import org.example.dto.BookingOuterDTO;
import org.example.entity.Booking;
import org.example.entity.Room;
import org.example.entity.Status;
import org.example.entity.User;
import org.example.mapper.BookingMapper;
import org.example.repository.BookingRepository;
import org.example.repository.RoomRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @InjectMocks
    BookingService bookingService;

    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RoomRepository roomRepository;
    @Mock
    BookingMapper mapper;

    int bookingId = 1;
    int userId = 1;
    int roomId = 1;

    User user = new User(userId, "dima", "karavaev", "dima@mail.ru", "финансовый");
    Room room = new Room(roomId, "Переговорная", "Воронеж", Set.of());

    BookingInnerDTO innerDTO = new BookingInnerDTO(userId, roomId,
            LocalDateTime.now(), LocalDateTime.now().plusHours(2));
    Booking booking = new Booking(bookingId, room, user, innerDTO.startTime(), innerDTO.endTime(), Status.CONFIRMED);
    BookingOuterDTO outerDTO = new BookingOuterDTO(booking.getId(), userId, roomId,
            innerDTO.startTime(), innerDTO.endTime(), Status.CONFIRMED.name());


    @Test
    void create_success() {
        // given
        when(bookingRepository.existsBookingByUserId(innerDTO.userId())).thenReturn(false);
        when(userRepository.findById(innerDTO.userId())).thenReturn(Optional.of(user));
        when(roomRepository.findById(innerDTO.roomId())).thenReturn(Optional.of(room));
        when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(booking);
        when(mapper.toOuterDTO(booking)).thenReturn(outerDTO);

        assertEquals(outerDTO, bookingService.create(innerDTO));
        verifyNoMoreInteractions(mapper, bookingRepository, userRepository, roomRepository);
    }

    @Test
    void create_userAlreadyHasBooking_throwsException() {
        when(bookingRepository.existsBookingByUserId(innerDTO.userId())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> bookingService.create(innerDTO));
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(mapper, userRepository, roomRepository);
    }

    @Test
    void create_userNotFound_throwsException() {
        when(bookingRepository.existsBookingByUserId(innerDTO.userId())).thenReturn(false);
        when(userRepository.findById(innerDTO.userId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookingService.create(innerDTO));
        verifyNoMoreInteractions(bookingRepository, userRepository);
        verifyNoInteractions(roomRepository, mapper);
    }

    @Test
    void create_roomNotFound_throwsException() {
        when(bookingRepository.existsBookingByUserId(innerDTO.userId())).thenReturn(false);
        when(userRepository.findById(innerDTO.userId())).thenReturn(Optional.of(user));
        when(roomRepository.findById(innerDTO.roomId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookingService.create(innerDTO));
        verifyNoMoreInteractions(bookingRepository, userRepository, roomRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void create_roomAlreadyBooked_throwsException() {
        Booking booking = new Booking(1, this.room, user, innerDTO.startTime().plusHours(1), innerDTO.endTime(), Status.CONFIRMED);
        Room room = new Room(1, "Переговорная", "Воронеж", Set.of(booking));
        when(bookingRepository.existsBookingByUserId(1)).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(roomRepository.findById(1)).thenReturn(Optional.of(room));

        assertThrows(IllegalArgumentException.class, () -> bookingService.create(innerDTO));
        verifyNoMoreInteractions(bookingRepository, userRepository, roomRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void findById_success() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(mapper.toOuterDTO(booking)).thenReturn(outerDTO);

        BookingOuterDTO result = bookingService.findById(booking.getId());

        assertNotNull(result);
        assertEquals(outerDTO, bookingService.findById(booking.getId()));
        verifyNoMoreInteractions(mapper, bookingRepository);

    }

    @Test
    void findById_throwsException() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookingService.findById(1));
        verify(bookingRepository).findById(booking.getId());
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll() {
        User user1 = new User(1, "dima", "karavaev", "dima@mail.ru", "финансовый");
        User user2 = new User(2, "sanya", "karavaev", "sanya@mail.ru", "финансовый");
        Booking booking1 = new Booking(1, room, user1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Status.CONFIRMED);
        Booking booking2 = new Booking(2, room, user2, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), Status.CONFIRMED);
        List<Booking> bookings = List.of(booking1, booking2);

        BookingOuterDTO outerDTO1 = new BookingOuterDTO(booking1.getId(), user1.getId(),
                room.getId(), booking1.getStartTime(), booking1.getEndTime(), booking1.getStatus().name());
        BookingOuterDTO outerDTO2 = new BookingOuterDTO(booking2.getId(), user2.getId(),
                room.getId(), booking2.getStartTime(), booking2.getEndTime(), booking2.getStatus().name());
        List<BookingOuterDTO> outerDTOS = List.of(outerDTO1, outerDTO2);

        when(bookingRepository.findAll()).thenReturn(bookings);
        when(mapper.toOuterDTO(bookings)).thenReturn(outerDTOS);

        assertEquals(outerDTOS, bookingService.findAll());
        verifyNoMoreInteractions(mapper, bookingRepository);
    }

    @Test
    void findByUserId_success() {
        when(bookingRepository.findBookingByUserId(user.getId())).thenReturn(Optional.of(booking));
        when(mapper.toOuterDTO(booking)).thenReturn(outerDTO);

        assertEquals(outerDTO, bookingService.findByUserId(user.getId()));
        verifyNoMoreInteractions(mapper, bookingRepository);
    }

    @Test
    void findByUserId_userNotFound_throwsException() {
        when(bookingRepository.findBookingByUserId(user.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookingService.findByUserId(user.getId()));
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void cancelBooking_success() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        booking.setStatus(Status.CANCELLED);
        when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(booking);
        BookingOuterDTO cancelOuterDTO = new BookingOuterDTO(booking.getId(), userId, roomId,
                booking.getStartTime(), booking.getEndTime(), Status.CANCELLED.name());
        when(mapper.toOuterDTO(booking)).thenReturn(cancelOuterDTO);

        assertEquals(cancelOuterDTO, bookingService.cancelBooking(booking.getId()));
    }

    @Test
    void deleteById(){
        doNothing().when(bookingRepository).deleteById(bookingId);

        bookingService.deleteById(bookingId);

        verify(bookingRepository).deleteById(bookingId);
    }

}