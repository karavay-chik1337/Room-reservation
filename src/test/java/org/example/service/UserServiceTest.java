package org.example.service;

import org.example.dto.UserInnerDTO;
import org.example.dto.UserOuterDTO;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper mapper;
    @Mock
    UserRepository userRepository;

    UserInnerDTO innerDTO;
    User user;
    UserOuterDTO outerDTO;

    @BeforeEach
    void setUp() {
        innerDTO = new UserInnerDTO("dima", "karavaev", "dima@mail.ru", "финансовый");
        user = new User(1, innerDTO.name(), innerDTO.surname(), innerDTO.email(), innerDTO.department());
        outerDTO = new UserOuterDTO(user.getId(), innerDTO.name(), innerDTO.surname(), innerDTO.email(), innerDTO.department());
    }

    @Test
    void create_success() {
        when(userRepository.existsUserByEmail(innerDTO.email())).thenReturn(false);
        when(mapper.toEntity(innerDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapper.toOuterDTO(user)).thenReturn(outerDTO);

        assertEquals(outerDTO, userService.create(innerDTO));
        verifyNoMoreInteractions(userRepository, mapper);
    }

    @Test
    void create_UserWithEmailAlreadyExists_throwException() {
        when(userRepository.existsUserByEmail(innerDTO.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.create(innerDTO));
        verify(userRepository).existsUserByEmail(innerDTO.email());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void findById_success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(mapper.toOuterDTO(user)).thenReturn(outerDTO);

        assertEquals(outerDTO, userService.findById(1));
        verify(userRepository).findById(1);
        verifyNoMoreInteractions(mapper, userRepository);
    }

    @Test
    void findById_throwsException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.findById(1));
        verify(userRepository).findById(1);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll() {
        List<User> users = List.of(new User(1, "dima", "karavaev", "dima@mail.ru", "финансовый"),
                new User(2, "anna", "karavaeva", "anna@mail.ru", "финансовый"));
        List<UserOuterDTO> userOuterDTOS = List.of(new UserOuterDTO(1, "dima", "karavaev", "dima@mail.ru", "финансовый"),
                new UserOuterDTO(2, "anna", "karavaev", "anna@mail.ru", "финансовый"));
        when(userRepository.findAll()).thenReturn(users);
        when(mapper.toOuterDTO(users)).thenReturn(userOuterDTOS);

        assertEquals(userOuterDTOS, userService.findAll());
        verifyNoMoreInteractions(mapper, userRepository);
    }

    @Test
    void update_success() {
        int id = 1;
        UserInnerDTO innerDTO = new UserInnerDTO("DIMA", null, null, null);
        UserOuterDTO outerDTO = new UserOuterDTO(user.getId(), innerDTO.name(), user.getSurname(), user.getEmail(), user.getDepartment());
        when(userRepository.existsUserByEmail(innerDTO.email())).thenReturn(false);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        user.setName(innerDTO.name());
        when(mapper.updateUser(innerDTO, user)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapper.toOuterDTO(user)).thenReturn(outerDTO);

        assertEquals(outerDTO, userService.update(id, innerDTO));
        verifyNoMoreInteractions(userRepository, mapper);
        //verify(mapper, )
    }

    @Test
    void update_EmailAlreadyExists_throwException(){
        when(userRepository.existsUserByEmail(innerDTO.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.update(1, innerDTO));
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void update_userNotFound_throwException(){
        when(userRepository.existsUserByEmail(innerDTO.email())).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.update(1, innerDTO));
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteById(){
        doNothing().when(userRepository).deleteById(user.getId());

        userService.deleteById(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
    }
}