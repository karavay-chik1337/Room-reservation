package org.example.mapper;

import org.example.dto.UserInnerDTO;
import org.example.dto.UserOuterDTO;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserOuterDTO toOuterDTO(User user);

    List<UserOuterDTO> toOuterDTO(List<User> users);

    User toEntity(UserInnerDTO innerDTO);
}
