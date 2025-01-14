package com.anikeeva.traineeship.workplacebooking.mappers;

import com.anikeeva.traineeship.workplacebooking.entities.OfficeEntity;
import com.anikeeva.traineeship.workplacebooking.dto.OfficeForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.OfficeForUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OfficeMapper {
    @Mapping(target = "isDeleted", source = "isDeleted")
    OfficeForAdminDTO toOfficeForAdminDTO(OfficeEntity officeEntity);

    OfficeEntity toOfficeEntity(OfficeForAdminDTO officeForAdminDTO);

    void updateOfficeEntityFromOfficeForAdminDTO(OfficeForAdminDTO officeForAdminDTO,
                                                 @MappingTarget OfficeEntity toOfficeEntity);

    OfficeForUserDTO toOfficeForUserDTO(OfficeEntity officeEntity);
}
