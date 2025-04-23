package org.example.repository;

import org.example.entity.Booking;
import org.example.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    boolean existsBookingByUserId(Integer id);
    Optional<Booking> findBookingByUserId(int userId);
    List<Booking> findAllByStatusAndEndTimeLessThanEqual(Status status, LocalDateTime endTime);
}
