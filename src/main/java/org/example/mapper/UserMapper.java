package org.example.mapper;

import org.example.dto.UserInnerDTO;
import org.example.dto.UserOuterDTO;
import org.example.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserOuterDTO toOuterDTO(User user);

    List<UserOuterDTO> toOuterDTO(List<User> users);

    User toEntity(UserInnerDTO innerDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserInnerDTO innerDTO, @MappingTarget User user);
}
