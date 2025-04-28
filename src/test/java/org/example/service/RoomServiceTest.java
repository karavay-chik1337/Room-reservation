package org.example.service;

import org.example.mapper.RoomMapper;
import org.example.repository.BookingRepository;
import org.example.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void create() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAllAvailable() {
    }

    @Test
    void update() {
    }
}