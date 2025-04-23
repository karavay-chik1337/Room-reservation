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

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper mapper;

    //переделать
    public BookingOuterDTO create(BookingInnerDTO bookingInnerDTO) {
        if (bookingRepository.existsBookingByUserId(bookingInnerDTO.userId())) {
            throw new RuntimeException("У пользователя с id: %s уже есть бронь".formatted(bookingInnerDTO.userId()));
        }
        User user = userRepository.findById(bookingInnerDTO.userId())
                .orElseThrow(() -> new RuntimeException("Пользователя с id: %s не существует".formatted(bookingInnerDTO.userId())));
        Room room = roomRepository.findById(bookingInnerDTO.roomId())
                .orElseThrow(() -> new RuntimeException("Комнаты с id: %s не существует".formatted(bookingInnerDTO.roomId())));
        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setRoom(room);
        newBooking.setStatus(Status.CONFIRMED);
        newBooking.setStartTime(bookingInnerDTO.startTime());
        newBooking.setEndTime(bookingInnerDTO.endTime());
        return mapper.toOuterDTO(bookingRepository.save(newBooking));
    }

    public BookingOuterDTO cancelBooking(int id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Такой брони не существует"));
        booking.setStatus(Status.CANCELLED);
        return mapper.toOuterDTO(bookingRepository.save(booking));
    }

    public List<BookingOuterDTO> findAll() {
        return mapper.toOuterDTO(bookingRepository.findAll());
    }

    public BookingOuterDTO findByUserId(int userId) {
        return mapper.toOuterDTO(bookingRepository.findBookingByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Пользователя с id: %s нет в списке бронирования")));
    }

    public void deleteById(int id) {
        bookingRepository.deleteById(id);
    }

    public BookingOuterDTO findById(int id) {
        return mapper.toOuterDTO(bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Брони с id: %s нет")));
    }
}
