package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id", nullable = false)
    private Room room;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "status")
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (!id.equals(booking.id)) return false;
        if (!room.equals(booking.room)) return false;
        if (!user.equals(booking.user)) return false;
        if (!startTime.equals(booking.startTime)) return false;
        if (!endTime.equals(booking.endTime)) return false;
        return status == booking.status;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + room.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
