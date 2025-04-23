package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserInnerDTO;
import org.example.dto.UserOuterDTO;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserOuterDTO createUser(UserInnerDTO userInnerDTO){
        if(userRepository.existsUserByEmail(userInnerDTO.email()))
            throw new RuntimeException("Пользователь с почтой: \"%s\" уже существует".formatted(userInnerDTO.email()));
        User newUser = mapper.toEntity(userInnerDTO);
        return mapper.toOuterDTO(userRepository.save(newUser));
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public UserOuterDTO findById(int id){
        return mapper.toOuterDTO(userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Пользователя с id: %s не существует".formatted(id))));
    }

    public List<UserOuterDTO> findAll() {
        return mapper.toOuterDTO(userRepository.findAll());
    }

}
