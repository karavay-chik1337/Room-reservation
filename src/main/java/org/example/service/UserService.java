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

    public UserOuterDTO create(UserInnerDTO innerDTO){
        if(userRepository.existsUserByEmail(innerDTO.email()))
            throw new RuntimeException("Пользователь с почтой: \"%s\" уже существует".formatted(innerDTO.email()));
        User newUser = mapper.toEntity(innerDTO);
        return mapper.toOuterDTO(userRepository.save(newUser));
    }

    public UserOuterDTO findById(int id){
        return mapper.toOuterDTO(userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Пользователя с id: %s не существует".formatted(id))));
    }

    public List<UserOuterDTO> findAll() {
        return mapper.toOuterDTO(userRepository.findAll());
    }

    public UserOuterDTO update(int id, UserInnerDTO innerDTO){
        if(userRepository.existsUserByEmail(innerDTO.email()))
            throw new RuntimeException("Пользователь с почтой: \"%s\" уже существует".formatted(innerDTO.email()));
        User updateUser = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Пользователя с id: %s не существует".formatted(id)));
        mapper.updateUser(innerDTO, updateUser);
        return mapper.toOuterDTO(userRepository.save(updateUser));
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

}
