package org.example.repository;

import org.example.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    boolean existsBookingByUserId(Integer id);

    Optional<Booking> findBookingByUserId(int userId);
}
