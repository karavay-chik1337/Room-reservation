package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.BookingDTO;
import org.example.entity.Booking;
import org.example.entity.Room;
import org.example.entity.Status;
import org.example.entity.User;
import org.example.repository.BookingRepository;
import org.example.repository.RoomRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public Booking createBooking(BookingDTO bookingDTO) {
        if (bookingRepository.existsBookingByUserId(bookingDTO.userId())) {
            throw new RuntimeException("У данного юзера уже есть бронь");
        }
        User user = userRepository.findById(bookingDTO.userId())
                .orElseThrow(() -> new RuntimeException("Пользователя с id: %s не существует".formatted(bookingDTO.userId())));
        Room room = roomRepository.findById(bookingDTO.roomId())
                .orElseThrow(() -> new RuntimeException("Комнаты с id: %s не существует".formatted(bookingDTO.roomId())));
        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setRoom(room);
        newBooking.setStatus(Status.CONFIRMED);
        newBooking.setStartTime(bookingDTO.startTime());
        newBooking.setEndTime(bookingDTO.endTime());
        return bookingRepository.save(newBooking);
    }

    public Booking cancelBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Такой брони не существует"));
        booking.setStatus(Status.CANCELLED);
        return bookingRepository.save(booking);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking findByUserId(int userId) {
        return bookingRepository.findBookingByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Пользователя с id: %s нет в списке бронирования"));
    }
}
