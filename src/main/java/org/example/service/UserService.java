package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(UserDTO userDTO){
        if(userRepository.existsUserByEmail(userDTO.email()))
            throw new RuntimeException("Пользователь с почтой: \"%s\" уже существует".formatted(userDTO.email()));
        User newUser = new User();
        newUser.setName(userDTO.name());
        newUser.setSurname(userDTO.surname());
        newUser.setEmail(userDTO.email());
        newUser.setDepartment(userDTO.department());
        return userRepository.save(newUser);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

//    public User findById(Integer id){
//        return userRepository.findById(id).
//                orElseThrow(() -> new RuntimeException("Пользователя с id: %s не существует".formatted(id)));
//    }
//
//    pu
}
