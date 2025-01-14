package com.anikeeva.traineeship.workplacebooking.mappers;

import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceForUserDTO;
import com.anikeeva.traineeship.workplacebooking.entities.WorkspaceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class WorkspaceMapperTest {
    WorkspaceMapper workspaceMapper = new WorkspaceMapperImpl();

    @Test
    public void toWorkspaceForAdminDTO() {
        UUID workspaceId = UUID.randomUUID();
        UUID officeId = UUID.randomUUID();
        WorkspaceEntity workspaceEntity = new WorkspaceEntity(
                workspaceId,
                "Name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        WorkspaceForAdminDTO expectedWorkspaceForAdminDTO = new WorkspaceForAdminDTO(
                workspaceId,
                "Name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        WorkspaceForAdminDTO actualWorkspaceForAdminDTO = workspaceMapper.toWorkspaceForAdminDTO(workspaceEntity);

        Assertions.assertEquals(expectedWorkspaceForAdminDTO, actualWorkspaceForAdminDTO);
    }

    @Test
    public void toWorkspaceForAdminDTOWithNull() {
        WorkspaceForAdminDTO actualWorkspaceForAdminDTO = workspaceMapper.toWorkspaceForAdminDTO(null);

        Assertions.assertNull(actualWorkspaceForAdminDTO);
    }

    @Test
    public void toWorkspaceEntity() {
        UUID officeId = UUID.randomUUID();
        UUID workspaceId = UUID.randomUUID();
        WorkspaceForAdminDTO workspaceForAdminDTO = new WorkspaceForAdminDTO(
                workspaceId,
                "Name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        WorkspaceEntity expectedWorkspaceEntity = new WorkspaceEntity(
                workspaceId,
                "Name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        WorkspaceEntity actualWorkspaceEntity = workspaceMapper.toWorkspaceEntity(workspaceForAdminDTO);

        Assertions.assertEquals(expectedWorkspaceEntity, actualWorkspaceEntity);
    }

    @Test
    public void toWorkspaceEntityWithNull() {
        WorkspaceEntity actualWorkspaceEntity = workspaceMapper.toWorkspaceEntity(null);

        Assertions.assertNull(actualWorkspaceEntity);
    }
    @Test
    public void updateWorkspaceEntityFromWorkspaceForAdminDTO() {
        UUID workspaceId = UUID.randomUUID();
        UUID officeId = UUID.randomUUID();
        WorkspaceEntity workspaceEntity = new WorkspaceEntity(
                workspaceId,
                "Name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        WorkspaceForAdminDTO workspaceForAdminDTO = new WorkspaceForAdminDTO(
                workspaceId,
                "New name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        WorkspaceEntity updatedWorkspaceEntity = new WorkspaceEntity(
                workspaceId,
                "New name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        workspaceMapper.updateWorkspaceEntityFromWorkspaceForAdminDTO(workspaceForAdminDTO, workspaceEntity);

        Assertions.assertEquals(workspaceEntity, updatedWorkspaceEntity);
    }

    @Test
    public void updateWorkspaceEntityFromWorkspaceForAdminDTOWithNull() {
        UUID workspaceId = UUID.randomUUID();
        UUID officeId = UUID.randomUUID();
        WorkspaceEntity workspaceEntity = new WorkspaceEntity(
                workspaceId,
                "Name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        WorkspaceEntity newWorkspaceEntity = new WorkspaceEntity(
                workspaceId,
                "Name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        workspaceMapper.updateWorkspaceEntityFromWorkspaceForAdminDTO(null, newWorkspaceEntity);

        Assertions.assertEquals(workspaceEntity, newWorkspaceEntity);
    }

    @Test
    public void toWorkspaceForUserDTO() {
        UUID workspaceId = UUID.randomUUID();
        UUID officeId = UUID.randomUUID();
        WorkspaceEntity workspaceEntity = new WorkspaceEntity(
                workspaceId,
                "Name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        WorkspaceForUserDTO expectedWorkspaceForUserDTO = new WorkspaceForUserDTO(
                workspaceId,
                "Name" ,
                (short) 1,
                (short) 2,
                officeId
        );

        WorkspaceForUserDTO actualWorkspaceForUserDTO = workspaceMapper.toWorkspaceForUserDTO(workspaceEntity);

        Assertions.assertEquals(expectedWorkspaceForUserDTO, actualWorkspaceForUserDTO);
    }

    @Test
    public void toWorkspaceForUserDTOWithNull() {
        WorkspaceEntity workspaceEntity = null;

        WorkspaceForUserDTO actualWorkspaceForUserDTO = workspaceMapper.toWorkspaceForUserDTO(workspaceEntity);

        Assertions.assertNull(actualWorkspaceForUserDTO);
    }
}
