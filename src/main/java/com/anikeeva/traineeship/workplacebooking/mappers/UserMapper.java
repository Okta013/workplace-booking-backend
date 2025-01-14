package com.anikeeva.traineeship.workplacebooking.mappers;

import com.anikeeva.traineeship.workplacebooking.dto.UserForSelectDTO;
import com.anikeeva.traineeship.workplacebooking.dto.UserForUserDTO;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import com.anikeeva.traineeship.workplacebooking.dto.UserForAdminDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserForUserDTO toUserForUserDTO(UserEntity userEntity);

    UserForAdminDTO toUserForAdminDTO(UserEntity userEntity);

    UserEntity toUserEntity(UserForAdminDTO userForAdminDTO);

    @Mapping(target = "id", ignore = true)
    void updateUserEntityFromUserForAdminDTO(UserForAdminDTO userForAdminDTO, @MappingTarget UserEntity toUserEntity);

    UserForSelectDTO toUserForSelectDTO(UserEntity userEntity);
}