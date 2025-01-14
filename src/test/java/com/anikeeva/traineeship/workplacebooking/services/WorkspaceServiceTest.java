package com.anikeeva.traineeship.workplacebooking.services;

import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceForUserDTO;
import com.anikeeva.traineeship.workplacebooking.entities.WorkspaceEntity;
import com.anikeeva.traineeship.workplacebooking.exceptions.BlankFieldsException;
import com.anikeeva.traineeship.workplacebooking.exceptions.ResourceNotFoundException;
import com.anikeeva.traineeship.workplacebooking.mappers.WorkspaceMapper;
import com.anikeeva.traineeship.workplacebooking.repositories.WorkspaceRepository;
import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {
    @InjectMocks
    WorkspaceService workspaceService;

    @Mock
    WorkspaceRepository workspaceRepository;

    @Mock
    WorkspaceMapper workspaceMapper;

    @Mock
    UserService userService;

    private static class TestWorkspaceData {
        UUID workspaceId1 = UUID.randomUUID();
        UUID workspaceId2 = UUID.randomUUID();
        UUID workspaceId3 = UUID.randomUUID();
        UUID workspaceId4 = UUID.randomUUID();
        UUID officeId1 = UUID.randomUUID();
        UUID officeId2 = UUID.randomUUID();
        UUID officeId3 = UUID.randomUUID();
        UUID officeId4 = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        WorkspaceEntity workspaceEntity1 = new  WorkspaceEntity(
                workspaceId1,
                "Name1" ,
                (short) 1,
                (short) 2,
                false,
                officeId1
        );
        WorkspaceEntity  workspaceEntity2 = new  WorkspaceEntity(
                workspaceId2,
                "Name2" ,
                (short) 2,
                (short) 3,
                false,
                officeId2
        );
        WorkspaceEntity workspaceEntity3 = new WorkspaceEntity(
                workspaceId3,
                "Name3" ,
                (short) 3,
                (short) 4,
                true,
                officeId3
        );
        WorkspaceEntity workspaceEntity4 = new WorkspaceEntity(
                workspaceId4,
                "Name4" ,
                (short) 4,
                (short) 5,
                true,
                officeId4
        );
        WorkspaceForAdminDTO workspaceForAdminDTO1 = new WorkspaceForAdminDTO(
                workspaceId1,
                "Name1" ,
                (short) 1,
                (short) 2,
                false,
                officeId1
        );
        WorkspaceForAdminDTO workspaceForAdminDTO2 = new WorkspaceForAdminDTO(
                workspaceId2,
                "Name2" ,
                (short) 2,
                (short) 3,
                false,
                officeId2
        );
        WorkspaceForAdminDTO workspaceForAdminDTO3 = new WorkspaceForAdminDTO(
                workspaceId3,
                "Name3" ,
                (short) 3,
                (short) 4,
                true,
                officeId3
        );
        WorkspaceForAdminDTO workspaceForAdminDTO4 = new WorkspaceForAdminDTO(
                workspaceId4,
                "Name4" ,
                (short) 4,
                (short) 5,
                true,
                officeId4
        );
        WorkspaceForUserDTO workspaceForUserDTO1 = new WorkspaceForUserDTO(
                workspaceId1,
                "Name1" ,
                (short) 1,
                (short) 2,
                officeId1
        );
        WorkspaceForUserDTO workspaceForUserDTO2 = new WorkspaceForUserDTO(
                workspaceId2,
                "Name2" ,
                (short) 2,
                (short) 3,
                officeId2
        );
        WorkspaceForUserDTO workspaceForUserDTO3 = new WorkspaceForUserDTO(
                workspaceId3,
                "Name3" ,
                (short) 3,
                (short) 4,
                officeId3
        );
        WorkspaceForUserDTO workspaceForUserDTO4 = new WorkspaceForUserDTO(
                workspaceId4,
                "Name4" ,
                (short) 4,
                (short) 5,
                officeId4
        );
        UserDetailsImpl currentUser = new UserDetailsImpl(
                userId,
                "Full Name",
                "89009009090",
                "email@gmail.com",
                "password",
                false
        );
        List<WorkspaceEntity> allWorkspaces = Arrays.asList(workspaceEntity1, workspaceEntity2,
                workspaceEntity3, workspaceEntity4);
        List<WorkspaceForAdminDTO> allWorkspacesDTO = Arrays.asList(workspaceForAdminDTO1, workspaceForAdminDTO2,
                workspaceForAdminDTO3, workspaceForAdminDTO4);
        List<WorkspaceForUserDTO> allWorkspacesForUserDTO = Arrays.asList(workspaceForUserDTO1,workspaceForUserDTO2,
                workspaceForUserDTO3, workspaceForUserDTO4);
    }

    @Test
    public void createWorkspace() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        WorkspaceForAdminDTO expectedWorkspaceForAdminDTO = testWorkspaceData.workspaceForAdminDTO1;

        when(workspaceRepository.save(any(WorkspaceEntity.class))).thenReturn(testWorkspaceData.workspaceEntity1);
        when(workspaceMapper.toWorkspaceForAdminDTO(any(WorkspaceEntity.class)))
                .thenReturn(expectedWorkspaceForAdminDTO);
        when(workspaceMapper.toWorkspaceEntity(any(WorkspaceForAdminDTO.class)))
                .thenReturn(testWorkspaceData.workspaceEntity1);
        WorkspaceForAdminDTO actualWorkspaceForAdminDTO =
                workspaceService.createWorkspace(expectedWorkspaceForAdminDTO);

        Assertions.assertEquals(expectedWorkspaceForAdminDTO, actualWorkspaceForAdminDTO);
        verify(workspaceRepository, times(1)).save(any(WorkspaceEntity.class));
        verify(workspaceMapper, times(1)).toWorkspaceForAdminDTO(any(WorkspaceEntity.class));
        verify(workspaceMapper, times(1)).toWorkspaceEntity(any(WorkspaceForAdminDTO.class));
    }

    @Test
    public void createWorkspaceWithMissingParameters() {
        WorkspaceForAdminDTO expectedWorkspaceForAdminDTO = new WorkspaceForAdminDTO(
                UUID.randomUUID(),
                "Name" ,
                null,
                (short) 2,
                false,
                UUID.randomUUID()
        );

        BlankFieldsException thrown = Assertions.assertThrows(BlankFieldsException.class, () -> {
            workspaceService.createWorkspace(expectedWorkspaceForAdminDTO);
        });

        Assertions.assertEquals("Заполнены не все обязательные поля", thrown.getMessage());
    }

    @Test
    public void showWorkspaceForAdmin() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        WorkspaceForAdminDTO expectedWorkspaceForAdminDTO = testWorkspaceData.workspaceForAdminDTO1;

        when(workspaceMapper.toWorkspaceForAdminDTO(any(WorkspaceEntity.class)))
                .thenReturn(expectedWorkspaceForAdminDTO);
        when(workspaceRepository.findById(testWorkspaceData.workspaceId1))
                .thenReturn(Optional.of(testWorkspaceData.workspaceEntity1));
        when(userService.checkAdminRole(any())).thenReturn(true);
        WorkspaceDTO actualWorkspaceForAdminDTO = workspaceService
                .showWorkspace(testWorkspaceData.currentUser, testWorkspaceData.workspaceId1);

        Assertions.assertNotNull(actualWorkspaceForAdminDTO);
        Assertions.assertEquals(expectedWorkspaceForAdminDTO, actualWorkspaceForAdminDTO);
    }

    @Test
    public void showWorkspaceForUser() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        WorkspaceForUserDTO expectedWorkspaceForUserDTO = testWorkspaceData.workspaceForUserDTO1;

        when(workspaceMapper.toWorkspaceForUserDTO(any(WorkspaceEntity.class)))
                .thenReturn(expectedWorkspaceForUserDTO);
        when(workspaceRepository.findById(testWorkspaceData.workspaceId1))
                .thenReturn(Optional.of(testWorkspaceData.workspaceEntity1));
        when(userService.checkAdminRole(any())).thenReturn(false);
        WorkspaceDTO actualWorkspaceForUserDTO = workspaceService
                .showWorkspace(testWorkspaceData.currentUser, testWorkspaceData.workspaceId1);

        Assertions.assertNotNull(actualWorkspaceForUserDTO);
        Assertions.assertEquals(expectedWorkspaceForUserDTO, actualWorkspaceForUserDTO);
    }

    @Test
    public void showWorkspaceNotFound() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            UUID workspaceId = UUID.randomUUID();
            when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.empty());
            workspaceService.showWorkspace(testWorkspaceData.currentUser, workspaceId);
        });

        Assertions.assertEquals("Помещение не найдено", thrown.getMessage());
    }

    @Test
    public void showAllWorkspacesForAdmin() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = testWorkspaceData.allWorkspaces;
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = testWorkspaceData.allWorkspacesDTO;

        when(workspaceMapper.toWorkspaceForAdminDTO(testWorkspaceData.workspaceEntity1))
                .thenReturn(testWorkspaceData.workspaceForAdminDTO1);
        when(workspaceMapper.toWorkspaceForAdminDTO(testWorkspaceData.workspaceEntity2))
                .thenReturn(testWorkspaceData.workspaceForAdminDTO2);
        when(workspaceMapper.toWorkspaceForAdminDTO(testWorkspaceData.workspaceEntity3))
                .thenReturn(testWorkspaceData.workspaceForAdminDTO3);
        when(workspaceMapper.toWorkspaceForAdminDTO(testWorkspaceData.workspaceEntity4))
                .thenReturn(testWorkspaceData.workspaceForAdminDTO4);
        when(workspaceRepository.findByOfficeId(testWorkspaceData.officeId1))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<WorkspaceDTO> actualWorkspaces =
                workspaceService.showAllWorkspaces(testWorkspaceData.currentUser,
                        testWorkspaceData.officeId1,null);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void showAllWorkspacesForUser() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = testWorkspaceData.allWorkspaces;
        List<WorkspaceForUserDTO> expectedWorkspacesDTO = testWorkspaceData.allWorkspacesForUserDTO;

        when(workspaceMapper.toWorkspaceForUserDTO(testWorkspaceData.workspaceEntity1))
                .thenReturn(testWorkspaceData.workspaceForUserDTO1);
        when(workspaceMapper.toWorkspaceForUserDTO(testWorkspaceData.workspaceEntity2))
                .thenReturn(testWorkspaceData.workspaceForUserDTO2);
        when(workspaceMapper.toWorkspaceForUserDTO(testWorkspaceData.workspaceEntity3))
                .thenReturn(testWorkspaceData.workspaceForUserDTO3);
        when(workspaceMapper.toWorkspaceForUserDTO(testWorkspaceData.workspaceEntity4))
                .thenReturn(testWorkspaceData.workspaceForUserDTO4);
        when(workspaceRepository.findByOfficeId(testWorkspaceData.officeId1))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(false);
        List<WorkspaceDTO> actualWorkspaces =
                workspaceService.showAllWorkspaces(testWorkspaceData.currentUser,
                        testWorkspaceData.officeId1,null);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void showAllWorkspacesForAdminWithFalseStatus() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity1);
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForAdminDTO1);

        when(workspaceMapper.toWorkspaceForAdminDTO(testWorkspaceData.workspaceEntity1))
                .thenReturn(testWorkspaceData.workspaceForAdminDTO1);
        when(workspaceRepository.findByOfficeIdAndIsDeleted(testWorkspaceData.officeId1,false))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<WorkspaceDTO> actualWorkspaces =
                workspaceService.showAllWorkspaces(testWorkspaceData.currentUser,
                        testWorkspaceData.officeId1,false);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void showAllWorkspacesForUserWithFalseStatus() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity1);
        List<WorkspaceForUserDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForUserDTO1);

        when(workspaceMapper.toWorkspaceForUserDTO(testWorkspaceData.workspaceEntity1))
                .thenReturn(testWorkspaceData.workspaceForUserDTO1);
        when(workspaceRepository.findByOfficeIdAndIsDeleted(testWorkspaceData.officeId1,false))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(false);
        List<WorkspaceDTO> actualWorkspaces =
                workspaceService.showAllWorkspaces(testWorkspaceData.currentUser,
                        testWorkspaceData.officeId1,false);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void showAllWorkspacesForAdminWithTrueStatus() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity3);
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForAdminDTO3);

        when(workspaceMapper.toWorkspaceForAdminDTO(testWorkspaceData.workspaceEntity3))
                .thenReturn(testWorkspaceData.workspaceForAdminDTO3);
        when(workspaceRepository.findByOfficeIdAndIsDeleted(testWorkspaceData.officeId3,true))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<WorkspaceDTO> actualWorkspaces =
                workspaceService.showAllWorkspaces(testWorkspaceData.currentUser,
                        testWorkspaceData.officeId3,true);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void showAllWorkspacesForUserWithTrueStatus() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity3);
        List<WorkspaceForUserDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForUserDTO3);

        when(workspaceMapper.toWorkspaceForUserDTO(testWorkspaceData.workspaceEntity3))
                .thenReturn(testWorkspaceData.workspaceForUserDTO3);
        when(workspaceRepository.findByOfficeIdAndIsDeleted(testWorkspaceData.officeId3,true))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(false);
        List<WorkspaceDTO> actualWorkspaces =
                workspaceService.showAllWorkspaces(testWorkspaceData.currentUser,
                        testWorkspaceData.officeId3,true);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void showAllWorkspacesNotFound() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = new ArrayList<>(0);

        List<WorkspaceDTO> actualWorkspacesDTO =
                workspaceService.showAllWorkspaces(testWorkspaceData.currentUser, null,null);

        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspacesDTO);
    }

    @Test
    public void searchWorkspaceWithNameForAdmin() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity2);
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForAdminDTO2);

        when(workspaceMapper.toWorkspaceForAdminDTO(testWorkspaceData.workspaceEntity2))
                .thenReturn(testWorkspaceData.workspaceForAdminDTO2);
        when(workspaceRepository.findByOfficeIdAndNameOrFloorNumberOrRoomNumber(testWorkspaceData.officeId2,"Name2", null, null))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<WorkspaceDTO> actualWorkspaces = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser, testWorkspaceData.officeId2,"Name2", null, null);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void searchWorkspaceWithNameForUser() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity2);
        List<WorkspaceForUserDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForUserDTO2);

        when(workspaceMapper.toWorkspaceForUserDTO(testWorkspaceData.workspaceEntity2))
                .thenReturn(testWorkspaceData.workspaceForUserDTO2);
        when(workspaceRepository.findByOfficeIdAndNameOrFloorNumberOrRoomNumber(testWorkspaceData.officeId2,"Name2", null, null))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(false);
        List<WorkspaceDTO> actualWorkspaces = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser, testWorkspaceData.officeId2,"Name2", null, null);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void searchWorkspaceWithNameNotFound() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = new ArrayList<>(0);
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = new ArrayList<>(0);

        when(workspaceRepository.findByOfficeIdAndNameOrFloorNumberOrRoomNumber(testWorkspaceData.officeId1,"Name1", null, null))
                .thenReturn(expectedWorkspaces);
        List<WorkspaceDTO> actualWorkspacesDTO = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser, testWorkspaceData.officeId1,"Name1", null, null);

        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspacesDTO);
    }

    @Test
    public void searchWorkspaceWithFloorNumberForAdmin() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity2);
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForAdminDTO2);

        when(workspaceMapper.toWorkspaceForAdminDTO(testWorkspaceData.workspaceEntity2))
                .thenReturn(testWorkspaceData.workspaceForAdminDTO2);
        when(workspaceRepository.findByOfficeIdAndNameOrFloorNumberOrRoomNumber(testWorkspaceData.officeId2,null, (short) 2, null))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<WorkspaceDTO> actualWorkspaces = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser,testWorkspaceData.officeId2,null, (short) 2, null);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void searchWorkspaceWithFloorNumberForUser() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity2);
        List<WorkspaceForUserDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForUserDTO2);

        when(workspaceMapper.toWorkspaceForUserDTO(testWorkspaceData.workspaceEntity2))
                .thenReturn(testWorkspaceData.workspaceForUserDTO2);
        when(workspaceRepository.findByOfficeIdAndNameOrFloorNumberOrRoomNumber(testWorkspaceData.officeId2,null, (short) 2, null))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(false);
        List<WorkspaceDTO> actualWorkspaces = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser,testWorkspaceData.officeId2,null, (short) 2, null);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void searchWorkspaceWithFloorNumberNotFound() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = new ArrayList<>(0);
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = new ArrayList<>(0);

        when(workspaceRepository.findByOfficeIdAndNameOrFloorNumberOrRoomNumber(testWorkspaceData.officeId2,null, (short) 2, null))
                .thenReturn(expectedWorkspaces);
        List<WorkspaceDTO> actualWorkspacesDTO = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser, testWorkspaceData.officeId2,null, (short) 2, null);

        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspacesDTO);
    }

    @Test
    public void searchWorkspaceWithRoomNumberForAdmin() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity2);
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForAdminDTO2);

        when(workspaceMapper.toWorkspaceForAdminDTO(testWorkspaceData.workspaceEntity2))
                .thenReturn(testWorkspaceData.workspaceForAdminDTO2);
        when(workspaceRepository.findByOfficeIdAndNameOrFloorNumberOrRoomNumber(testWorkspaceData.officeId3,null, null, (short) 3))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<WorkspaceDTO> actualWorkspaces = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser, testWorkspaceData.officeId3,null, null, (short) 3);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void searchWorkspaceWithRoomNumberForUser() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity2);
        List<WorkspaceForUserDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForUserDTO2);

        when(workspaceMapper.toWorkspaceForUserDTO(testWorkspaceData.workspaceEntity2))
                .thenReturn(testWorkspaceData.workspaceForUserDTO2);
        when(workspaceRepository.findByOfficeIdAndNameOrFloorNumberOrRoomNumber(testWorkspaceData.officeId3, null, null, (short) 3))
                .thenReturn(expectedWorkspaces);
        when(userService.checkAdminRole(any())).thenReturn(false);
        List<WorkspaceDTO> actualWorkspaces = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser, testWorkspaceData.officeId3,null, null, (short) 3);

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void searchWorkspaceWithRoomNumberNotFound() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = new ArrayList<>(0);
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = new ArrayList<>(0);

        when(workspaceRepository.findByOfficeIdAndNameOrFloorNumberOrRoomNumber(testWorkspaceData.officeId3,null, null, (short) 3))
                .thenReturn(expectedWorkspaces);
        List<WorkspaceDTO> actualWorkspacesDTO = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser, testWorkspaceData.officeId3,null, null, (short) 3);

        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspacesDTO);
    }

    @Test
    public void searchWorkspaceWithEmptyParametersForAdmin() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> allWorkplaceEntities = testWorkspaceData.allWorkspaces;
        List<WorkspaceForAdminDTO> expectedWorkspacesDTO = new ArrayList<>(0);

        List<WorkspaceDTO> actualWorkspacesDTO = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser, testWorkspaceData.officeId3,null, null, null);

        Assertions.assertNotNull(actualWorkspacesDTO);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspacesDTO);
    }

    @Test
    public void searchWorkspaceWithEmptyParametersForUser() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> allWorkplaceEntities = testWorkspaceData.allWorkspaces;
        List<WorkspaceForUserDTO> expectedWorkspacesDTO = new ArrayList<>(0);

        List<WorkspaceDTO> actualWorkspacesDTO = workspaceService
                .searchWorkspaces(testWorkspaceData.currentUser, testWorkspaceData.officeId3,null, null, null);

        Assertions.assertNotNull(actualWorkspacesDTO);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspacesDTO);
    }

    @Test
    public void updateWorkspace() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        WorkspaceForAdminDTO expectedWorkspaceForAdminDTO = testWorkspaceData.workspaceForAdminDTO2;

        when(workspaceRepository.findById(testWorkspaceData.workspaceId1))
                .thenReturn(Optional.of(testWorkspaceData.workspaceEntity1));
        when(workspaceMapper.toWorkspaceForAdminDTO(any(WorkspaceEntity.class)))
                .thenReturn(expectedWorkspaceForAdminDTO);
        when(workspaceRepository.save(any(WorkspaceEntity.class)))
                .thenReturn(testWorkspaceData.workspaceEntity2);
        WorkspaceForAdminDTO actualWorkspaceForAdminDTO = workspaceService
                .updateWorkspace(testWorkspaceData.workspaceId1, expectedWorkspaceForAdminDTO);

        Assertions.assertEquals(expectedWorkspaceForAdminDTO, actualWorkspaceForAdminDTO);
    }

    @Test
    public void updateWorkspaceNotFound() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            UUID workspaceId = UUID.randomUUID();

            when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.empty());
            workspaceService.updateWorkspace(workspaceId, testWorkspaceData.workspaceForAdminDTO1);
        });

        Assertions.assertEquals("Помещение не найдено", thrown.getMessage());
    }

    @Test
    public void searchWorkspaceByName() {
        TestWorkspaceData testWorkspaceData = new TestWorkspaceData();
        List<WorkspaceEntity> expectedWorkspaces = Arrays.asList(testWorkspaceData.workspaceEntity1);
        List<WorkspaceForUserDTO> expectedWorkspacesDTO = Arrays.asList(testWorkspaceData.workspaceForUserDTO1);

        when(workspaceMapper.toWorkspaceForUserDTO(testWorkspaceData.workspaceEntity1))
                .thenReturn(testWorkspaceData.workspaceForUserDTO1);
        when(workspaceRepository.findByName("Name1")).thenReturn(expectedWorkspaces);
        List<WorkspaceForUserDTO> actualWorkspaces = workspaceService.searchWorkspacesByName("Name1");

        Assertions.assertNotNull(actualWorkspaces);
        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspaces);
    }

    @Test
    public void searchWorkspaceByNameNotFound() {
        List<WorkspaceEntity> expectedWorkspaces = new ArrayList<>(0);
        List<WorkspaceForUserDTO> expectedWorkspacesDTO = new ArrayList<>(0);

        when(workspaceRepository.findByName("Name2")).thenReturn(expectedWorkspaces);
        List<WorkspaceForUserDTO> actualWorkspacesDTO = workspaceService.searchWorkspacesByName("Name2");

        Assertions.assertEquals(expectedWorkspacesDTO, actualWorkspacesDTO);
    }
}