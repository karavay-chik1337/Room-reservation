package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    @Fetch(value = FetchMode.JOIN)
    private Set<Booking> bookings = new HashSet<>();

    public Room(Integer id, String name, String location, Set<Booking> bookings) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.bookings = bookings;
    }
}
