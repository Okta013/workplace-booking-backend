package com.anikeeva.traineeship.workplacebooking.mappers;

import com.anikeeva.traineeship.workplacebooking.dto.WorkplaceForSelectDTO;
import com.anikeeva.traineeship.workplacebooking.entities.WorkplaceEntity;
import com.anikeeva.traineeship.workplacebooking.dto.WorkplaceForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkplaceForUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkplaceMapper {
    WorkplaceForAdminDTO toWorkplaceForAdminDTO(WorkplaceEntity workplaceEntity);

    WorkplaceEntity toWorkplaceEntity(WorkplaceForAdminDTO workplaceDTO);

    void updateWorkplaceEntityFromWorkplaceForAdminDTO(WorkplaceForAdminDTO workplaceForAdminDTO,
                                                       @MappingTarget WorkplaceEntity toWorkspaceEntity);


    WorkplaceForUserDTO toWorkplaceForUserDTO(WorkplaceEntity workplaceEntity);

    WorkplaceForSelectDTO toWorkplaceForSelectDTO(WorkplaceEntity workplaceEntity);
}
