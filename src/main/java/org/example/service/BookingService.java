package org.example.service;

import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper mapper;

    public BookingOuterDTO create(BookingInnerDTO bookingInnerDTO) {
        if (bookingRepository.existsBookingByUserId(bookingInnerDTO.userId())) {
            throw new IllegalArgumentException("У пользователя с id: %s уже есть бронь".formatted(bookingInnerDTO.userId()));
        }
        User user = userRepository.findById(bookingInnerDTO.userId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователя с id: %s не существует".formatted(bookingInnerDTO.userId())));
        Room room = roomRepository.findById(bookingInnerDTO.roomId())
                .orElseThrow(() -> new IllegalArgumentException("Комнаты с id: %s не существует".formatted(bookingInnerDTO.roomId())));

        LocalDateTime newStart = bookingInnerDTO.startTime();
        LocalDateTime newEnd = bookingInnerDTO.endTime();

        Set<Booking> bookings = room.getBookings();

        boolean isBusy = bookings.stream().anyMatch(existing ->
                newStart.isBefore(existing.getEndTime()) && newEnd.isAfter(existing.getStartTime())
        );

        if (isBusy) {
            throw new IllegalArgumentException("На указанный период [%s - %s] уже есть бронь для комнаты с id: %s"
                    .formatted(newStart, newEnd, room.getId()));
        }

        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setRoom(room);
        newBooking.setStatus(Status.CONFIRMED);
        newBooking.setStartTime(bookingInnerDTO.startTime());
        newBooking.setEndTime(bookingInnerDTO.endTime());
        return mapper.toOuterDTO(bookingRepository.saveAndFlush(newBooking));
    }

    public BookingOuterDTO findById(int id) {
        return mapper.toOuterDTO(bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Брони с id: %s нет")));
    }

    public List<BookingOuterDTO> findAll() {
        return mapper.toOuterDTO(bookingRepository.findAll());
    }

    public void deleteById(int id) {
        bookingRepository.deleteById(id);
    }

    public BookingOuterDTO findByUserId(int userId) {
        return mapper.toOuterDTO(bookingRepository.findBookingByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователя с id: %s нет в списке бронирования")));
    }

    public BookingOuterDTO cancelBooking(int id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Такой брони не существует"));
        booking.setStatus(Status.CANCELLED);
        return mapper.toOuterDTO(bookingRepository.saveAndFlush(booking));
    }
}
