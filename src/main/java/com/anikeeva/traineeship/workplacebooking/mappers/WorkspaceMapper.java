package com.anikeeva.traineeship.workplacebooking.mappers;

import com.anikeeva.traineeship.workplacebooking.entities.WorkspaceEntity;
import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceForUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {
    WorkspaceForAdminDTO toWorkspaceForAdminDTO(WorkspaceEntity workspaceEntity);

    WorkspaceEntity toWorkspaceEntity(WorkspaceForAdminDTO workspaceForAdminDTO);

    void updateWorkspaceEntityFromWorkspaceForAdminDTO(WorkspaceForAdminDTO workspaceForAdminDTO,
                                                       @MappingTarget WorkspaceEntity toWorkspaceEntity);

    WorkspaceForUserDTO toWorkspaceForUserDTO(WorkspaceEntity workspaceEntity);

}
