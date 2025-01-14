package com.anikeeva.traineeship.workplacebooking.services;

import com.anikeeva.traineeship.workplacebooking.dto.WorkplaceDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkplaceForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkplaceForUserDTO;
import com.anikeeva.traineeship.workplacebooking.entities.WorkplaceEntity;
import com.anikeeva.traineeship.workplacebooking.exceptions.BlankFieldsException;
import com.anikeeva.traineeship.workplacebooking.exceptions.NotUniqueNumberException;
import com.anikeeva.traineeship.workplacebooking.exceptions.ResourceNotFoundException;
import com.anikeeva.traineeship.workplacebooking.mappers.WorkplaceMapper;
import com.anikeeva.traineeship.workplacebooking.repositories.WorkplaceRepository;
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
class WorkplaceServiceTest {
    @InjectMocks
    WorkplaceService workplaceService;

    @Mock
    WorkplaceRepository workplaceRepository;

    @Mock
    WorkplaceMapper workplaceMapper;

    @Mock
    UserService userService;

    private static class TestWorkplaceData {
        UUID workplaceId1 = UUID.randomUUID();
        UUID workplaceId2 = UUID.randomUUID();
        UUID workplaceId3 = UUID.randomUUID();
        UUID workplaceId4 = UUID.randomUUID();
        UUID workspaceId1 = UUID.randomUUID();
        UUID workspaceId2 = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        WorkplaceEntity workplaceEntity1 = new WorkplaceEntity(
                workplaceId1,
                1,
                "Description",
                workspaceId1,
                false
        );
        WorkplaceEntity workplaceEntity2 = new WorkplaceEntity(
                workplaceId2,
                2,
                "Description",
                workspaceId2,
                false
        );
        WorkplaceEntity workplaceEntity3 = new WorkplaceEntity(
                workplaceId3,
                2,
                "Description",
                workspaceId1,
                true
        );
        WorkplaceEntity workplaceEntity4 = new WorkplaceEntity(
                workplaceId4,
                4,
                "Description",
                workspaceId2,
                true
        );
        WorkplaceForAdminDTO workplaceForAdminDTO1 = new WorkplaceForAdminDTO(
                workspaceId1,
                1,
                "Description",
                workspaceId1,
                false
        );
        WorkplaceForAdminDTO workplaceForAdminDTO2 = new WorkplaceForAdminDTO(
                workplaceId2,
                2,
                "Description",
                workspaceId2,
                false
        );
        WorkplaceForAdminDTO workplaceForAdminDTO3 = new WorkplaceForAdminDTO(
                workplaceId3,
                2,
                "Description",
                workspaceId1,
                true
        );
        WorkplaceForAdminDTO workplaceForAdminDTO4 = new WorkplaceForAdminDTO(
                workplaceId4,
                4,
                "Description",
                workspaceId2,
                true
        );

        WorkplaceForUserDTO workplaceForUserDTO1 = new WorkplaceForUserDTO(
                workplaceId1,
                1,
                "Description",
                workspaceId1
        );
        WorkplaceForUserDTO workplaceForUserDTO2 = new WorkplaceForUserDTO(
                workplaceId2,
                2,
                "Description",
                workspaceId2
        );
        WorkplaceForUserDTO workplaceForUserDTO3 = new WorkplaceForUserDTO(
                workplaceId3,
                2,
                "Description",
                workspaceId1
        );
        WorkplaceForUserDTO workplaceForUserDTO4 = new WorkplaceForUserDTO(
                workplaceId4,
                4,
                "Description",
                workspaceId2
        );
        UserDetailsImpl currentUser = new UserDetailsImpl(
                userId,
                "Full Name",
                "89009009090",
                "email@gmail.com",
                "password",
                false
        );
        List<WorkplaceEntity> allWorkplaces = Arrays.asList(workplaceEntity1, workplaceEntity2,
                workplaceEntity3, workplaceEntity4);
        List<WorkplaceForAdminDTO> allWorkplacesForAdminDTO = Arrays.asList(workplaceForAdminDTO1,
                workplaceForAdminDTO2, workplaceForAdminDTO3, workplaceForAdminDTO4);
        List<WorkplaceForUserDTO> allWorkplacesForUserDTO = Arrays.asList(workplaceForUserDTO1, workplaceForUserDTO2,
                workplaceForUserDTO3, workplaceForUserDTO4);
        List<WorkplaceEntity> allWorkplacesOfFirstWorkspace = Arrays.asList(workplaceEntity1, workplaceEntity3);
        List<WorkplaceForAdminDTO> allWorkplacesForAdminOfFirstWorkspaceDTO = Arrays.asList(workplaceForAdminDTO1,
                workplaceForAdminDTO3);
        List<WorkplaceForUserDTO> allWorkplacesForUserOfFirstWorkspaceDTO = Arrays.asList(workplaceForUserDTO1,
                workplaceForUserDTO3);
        List<WorkplaceEntity> allWorkplacesWithNumberTwo = Arrays.asList(workplaceEntity2, workplaceEntity3);
        List<WorkplaceForAdminDTO> allWorkplacesForAdminWithNumberTwoDTO = Arrays.asList(workplaceForAdminDTO2,
                workplaceForAdminDTO3);
        List<WorkplaceForUserDTO> allWorkplacesForUserWithNumberTwoDTO = Arrays.asList(workplaceForUserDTO2,
                workplaceForUserDTO3);
    }

    @Test
    public void createWorkplace() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        WorkplaceForAdminDTO expectedWorkplaceForAdminDTO = testWorkplaceData.workplaceForAdminDTO1;

        when(workplaceRepository.save(any(WorkplaceEntity.class))).thenReturn(testWorkplaceData.workplaceEntity1);
        when(workplaceMapper.toWorkplaceForAdminDTO(any(WorkplaceEntity.class)))
                .thenReturn(expectedWorkplaceForAdminDTO);
        when(workplaceMapper.toWorkplaceEntity(any(WorkplaceForAdminDTO.class)))
                .thenReturn(testWorkplaceData.workplaceEntity1);
        WorkplaceForAdminDTO actualWorkplaceForAdminDTO = workplaceService
                .createWorkplace(expectedWorkplaceForAdminDTO);

        Assertions.assertEquals(expectedWorkplaceForAdminDTO, actualWorkplaceForAdminDTO);
        verify(workplaceRepository, times(1)).save(any(WorkplaceEntity.class));
        verify(workplaceMapper, times(1)).toWorkplaceEntity(any(WorkplaceForAdminDTO.class));
        verify(workplaceMapper, times(1)).toWorkplaceForAdminDTO(any(WorkplaceEntity.class));
    }

    @Test
    public void createWorkplaceWithMissingParameters() {
        WorkplaceForAdminDTO expectedWorkplaceForAdminDTO = new WorkplaceForAdminDTO(
                UUID.randomUUID(),
                null,
                "Description",
                UUID.randomUUID(),
                false
        );

        BlankFieldsException thrown = Assertions.assertThrows(BlankFieldsException.class, () -> {
            workplaceService.createWorkplace(expectedWorkplaceForAdminDTO);
        });

        Assertions.assertEquals("Заполнены не все обязательные поля", thrown.getMessage());
    }

    @Test
    public void createWorkplaceWithExistingNumber() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        UUID workplaceId = UUID.randomUUID();
        WorkplaceEntity workplaceEntity = new WorkplaceEntity(
                workplaceId,
                1,
                "Description",
                testWorkplaceData.workspaceId1,
                false
        );
        WorkplaceForAdminDTO expectedWorkplaceForAdminDTO = new WorkplaceForAdminDTO(
                UUID.randomUUID(),
                1,
                "Description",
                testWorkplaceData.workspaceId1,
                false
        );

        NotUniqueNumberException thrown = Assertions.assertThrows(NotUniqueNumberException.class, () -> {
            when(workplaceRepository.existsByWorkspaceIdAndNumber(testWorkplaceData.workspaceId1, 1))
                    .thenReturn(true);
            workplaceService.createWorkplace(expectedWorkplaceForAdminDTO);
        });

        Assertions.assertEquals("Рабочее место с таким номером уже существует в этом помещении",
                thrown.getMessage());
    }

    @Test
    public void showWorkplaceForAdmin() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        WorkplaceForAdminDTO expectedWorkplaceForAdminDTO = testWorkplaceData.workplaceForAdminDTO1;

        when(workplaceMapper.toWorkplaceForAdminDTO(any(WorkplaceEntity.class)))
                .thenReturn(expectedWorkplaceForAdminDTO);
        when(workplaceRepository.findById(testWorkplaceData.workplaceId1))
                .thenReturn(Optional.of(testWorkplaceData.workplaceEntity1));
        when(userService.checkAdminRole(any())).thenReturn(true);
        WorkplaceDTO actualWorkplaceForAdminDTO = workplaceService
                .showWorkplace(testWorkplaceData.currentUser, testWorkplaceData.workplaceId1);

        Assertions.assertNotNull(actualWorkplaceForAdminDTO);
        Assertions.assertEquals(expectedWorkplaceForAdminDTO, actualWorkplaceForAdminDTO);
    }

    @Test
    public void showWorkplaceForUser() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        WorkplaceForUserDTO expectedWorkplaceForUserDTO = testWorkplaceData.workplaceForUserDTO1;

        when(workplaceMapper.toWorkplaceForUserDTO(any(WorkplaceEntity.class))).thenReturn(expectedWorkplaceForUserDTO);
        when(workplaceRepository.findById(testWorkplaceData.workplaceId1))
                .thenReturn(Optional.of(testWorkplaceData.workplaceEntity1));
        when(userService.checkAdminRole(any())).thenReturn(false);
        WorkplaceDTO actualWorkplaceForUserDTO = workplaceService
                .showWorkplace(testWorkplaceData.currentUser, testWorkplaceData.workplaceId1);

        Assertions.assertNotNull(actualWorkplaceForUserDTO);
        Assertions.assertEquals(expectedWorkplaceForUserDTO, actualWorkplaceForUserDTO);
    }

    @Test
    public void showWorkplaceNotFound() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            UUID workplaceId = UUID.randomUUID();

            when(workplaceRepository.findById(workplaceId)).thenReturn(Optional.empty());
            workplaceService.showWorkplace(testWorkplaceData.currentUser, workplaceId);
        });

        Assertions.assertEquals("Рабочее место не найдено", thrown.getMessage());
    }

    @Test
    public void showAllWorkplacesForAdmin() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceEntity> expectedWorkplaces = testWorkplaceData.allWorkplacesOfFirstWorkspace;
        List<WorkplaceForAdminDTO> expectedWorkplacesDTO = testWorkplaceData.allWorkplacesForAdminOfFirstWorkspaceDTO;

        when(workplaceMapper.toWorkplaceForAdminDTO(testWorkplaceData.workplaceEntity1))
                .thenReturn(testWorkplaceData.workplaceForAdminDTO1);
        when(workplaceMapper.toWorkplaceForAdminDTO(testWorkplaceData.workplaceEntity3))
                .thenReturn(testWorkplaceData.workplaceForAdminDTO3);
        when(workplaceRepository.findByWorkspaceId(testWorkplaceData.workspaceId1)).thenReturn(expectedWorkplaces);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<WorkplaceDTO> actualWorkplacesForAdminDTO = workplaceService
                .showAllWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workspaceId1, null);

        Assertions.assertNotNull(actualWorkplacesForAdminDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForAdminDTO);
    }

    @Test
    public void showAllWorkplacesForUser() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceEntity> expectedWorkplaces = testWorkplaceData.allWorkplacesOfFirstWorkspace;
        List<WorkplaceForUserDTO> expectedWorkplacesDTO = testWorkplaceData.allWorkplacesForUserOfFirstWorkspaceDTO;

        when(workplaceMapper.toWorkplaceForUserDTO(testWorkplaceData.workplaceEntity1))
                .thenReturn(testWorkplaceData.workplaceForUserDTO1);
        when(workplaceMapper.toWorkplaceForUserDTO(testWorkplaceData.workplaceEntity3))
                .thenReturn(testWorkplaceData.workplaceForUserDTO3);
        when(workplaceRepository.findByWorkspaceId(testWorkplaceData.workspaceId1)).thenReturn(expectedWorkplaces);
        when(userService.checkAdminRole(any())).thenReturn(false);
        List<WorkplaceDTO> actualWorkplacesForUserDTO = workplaceService
                .showAllWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workspaceId1, null);

        Assertions.assertNotNull(actualWorkplacesForUserDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForUserDTO);
    }

    @Test
    public void showAllWorkplacesWithFalseStatusForAdmin() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceEntity> expectedWorkplaces = Arrays.asList(testWorkplaceData.workplaceEntity1);
        List<WorkplaceForAdminDTO> expectedWorkplacesDTO = Arrays.asList(testWorkplaceData.workplaceForAdminDTO1);

        when(workplaceMapper.toWorkplaceForAdminDTO(testWorkplaceData.workplaceEntity1))
                .thenReturn(testWorkplaceData.workplaceForAdminDTO1);
        when(workplaceRepository.findByWorkspaceIdAndIsDeleted(testWorkplaceData.workspaceId1, false))
                .thenReturn(expectedWorkplaces);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<WorkplaceDTO> actualWorkplacesForAdminDTO = workplaceService
                .showAllWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workspaceId1, false);

        Assertions.assertNotNull(actualWorkplacesForAdminDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForAdminDTO);
    }

    @Test
    public void showAllWorkplacesWithFalseStatusForUser() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceEntity> expectedWorkplaces = Arrays.asList(testWorkplaceData.workplaceEntity1);
        List<WorkplaceForUserDTO> expectedWorkplacesDTO = Arrays.asList(testWorkplaceData.workplaceForUserDTO1);

        when(workplaceMapper.toWorkplaceForUserDTO(testWorkplaceData.workplaceEntity1))
                .thenReturn(testWorkplaceData.workplaceForUserDTO1);
        when(workplaceRepository.findByWorkspaceIdAndIsDeleted(testWorkplaceData.workspaceId1, false))
                .thenReturn(expectedWorkplaces);
        when(userService.checkAdminRole(any())).thenReturn(false);
        List<WorkplaceDTO> actualWorkplacesForUserDTO = workplaceService
                .showAllWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workspaceId1, false);

        Assertions.assertNotNull(actualWorkplacesForUserDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForUserDTO);
    }

    @Test
    public void showAllWorkplacesWithTrueStatusForAdmin() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceEntity> expectedWorkplaces = Arrays.asList(testWorkplaceData.workplaceEntity3);
        List<WorkplaceForAdminDTO> expectedWorkplacesDTO = Arrays.asList(testWorkplaceData.workplaceForAdminDTO3);

        when(workplaceMapper.toWorkplaceForAdminDTO(testWorkplaceData.workplaceEntity3))
                .thenReturn(testWorkplaceData.workplaceForAdminDTO3);
        when(workplaceRepository.findByWorkspaceIdAndIsDeleted(testWorkplaceData.workspaceId1, true))
                .thenReturn(expectedWorkplaces);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<WorkplaceDTO> actualWorkplacesForAdminDTO = workplaceService
                .showAllWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workspaceId1, true);

        Assertions.assertNotNull(actualWorkplacesForAdminDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForAdminDTO);
    }

    @Test
    public void showAllWorkplacesWithTrueStatusForUser() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceEntity> expectedWorkplaces = Arrays.asList(testWorkplaceData.workplaceEntity3);
        List<WorkplaceForUserDTO> expectedWorkplacesDTO = Arrays.asList(testWorkplaceData.workplaceForUserDTO3);

        when(workplaceMapper.toWorkplaceForUserDTO(testWorkplaceData.workplaceEntity3))
                .thenReturn(testWorkplaceData.workplaceForUserDTO3);
        when(workplaceRepository.findByWorkspaceIdAndIsDeleted(testWorkplaceData.workspaceId1, true))
                .thenReturn(expectedWorkplaces);
        when(userService.checkAdminRole(any())).thenReturn(false);
        List<WorkplaceDTO> actualWorkplacesForUserDTO = workplaceService
                .showAllWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workspaceId1, true);

        Assertions.assertNotNull(actualWorkplacesForUserDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForUserDTO);
    }

    @Test
    public void showAllWorkplacesNotFound() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceForAdminDTO> expectedWorkplacesForAdminDTO = new ArrayList<>(0);

        List<WorkplaceDTO> actualWorkplacesForAdminDTO = workplaceService
                .showAllWorkplaces(testWorkplaceData.currentUser, null, null);

        Assertions.assertEquals(expectedWorkplacesForAdminDTO, actualWorkplacesForAdminDTO);
    }

    @Test
    public void searchWorkplacesForAdmin() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceForAdminDTO> expectedWorkplacesDTO = new ArrayList<>(0);

        List<WorkplaceDTO> actualWorkplacesForAdminDTO =
                workplaceService.searchWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workspaceId2, 2);

        Assertions.assertNotNull(actualWorkplacesForAdminDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForAdminDTO);
    }

    @Test
    public void searchWorkplacesForUser() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceForUserDTO> expectedWorkplacesDTO = new ArrayList<>(0);

        List<WorkplaceDTO> actualWorkplacesForAdminDTO =
                workplaceService.searchWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workspaceId1,2);

        Assertions.assertNotNull(actualWorkplacesForAdminDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForAdminDTO);
    }

    @Test
    public void searchWorkplacesNotFound() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceForAdminDTO> expectedWorkplacesDTO = new ArrayList<>(0);

        List<WorkplaceDTO> actualWorkplacesForAdminDTO =
                workplaceService.searchWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workplaceId3,13);

        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForAdminDTO);
    }

    @Test
    public void searchWorkplacesWithEmptyParametersForAdmin() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceForAdminDTO> expectedWorkplacesDTO = new ArrayList<>(0);

        List<WorkplaceDTO> actualWorkplacesForAdminDTO =
                workplaceService.searchWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workspaceId1, null);

        Assertions.assertNotNull(actualWorkplacesForAdminDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForAdminDTO);
    }

    @Test
    public void searchWorkplacesWithEmptyParametersForUser() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceForUserDTO> expectedWorkplacesDTO = new ArrayList<>(0);

        List<WorkplaceDTO> actualWorkplacesForAdminDTO =
                workplaceService.searchWorkplaces(testWorkplaceData.currentUser, testWorkplaceData.workspaceId1,null);

        Assertions.assertNotNull(actualWorkplacesForAdminDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForAdminDTO);
    }

    @Test
    public void updateWorkplace() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        WorkplaceForAdminDTO expectedWorkplaceForAdminDTO = testWorkplaceData.workplaceForAdminDTO1;

        when(workplaceRepository.findById(testWorkplaceData.workplaceId2))
                .thenReturn(Optional.of(testWorkplaceData.workplaceEntity2));
        when(workplaceMapper.toWorkplaceForAdminDTO(any(WorkplaceEntity.class)))
                .thenReturn(expectedWorkplaceForAdminDTO);
        when(workplaceRepository.save(any(WorkplaceEntity.class))).thenReturn(testWorkplaceData.workplaceEntity1);
        WorkplaceForAdminDTO actualWorkplaceForAdminDTO = workplaceService
                .updateWorkplace(testWorkplaceData.workplaceId2, expectedWorkplaceForAdminDTO);

        Assertions.assertNotNull(actualWorkplaceForAdminDTO);
        Assertions.assertEquals(expectedWorkplaceForAdminDTO, actualWorkplaceForAdminDTO);
    }

    @Test
    public void updateWorkplaceNotFound() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        UUID workplaceId = UUID.randomUUID();

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            when(workplaceRepository.findById(workplaceId)).thenReturn(Optional.empty());
            workplaceService.updateWorkplace(workplaceId, testWorkplaceData.workplaceForAdminDTO1);
        });

        Assertions.assertEquals(thrown.getMessage(), "Рабочее место не найдено");
    }


    @Test
    public void searchWorkplaceByNumber() {
        TestWorkplaceData testWorkplaceData = new TestWorkplaceData();
        List<WorkplaceEntity> expectedWorkplaces = Arrays.asList(testWorkplaceData.workplaceEntity1);
        List<WorkplaceForUserDTO> expectedWorkplacesDTO = Arrays.asList(testWorkplaceData.workplaceForUserDTO1);
        when(workplaceMapper.toWorkplaceForUserDTO(testWorkplaceData.workplaceEntity1))
                .thenReturn(testWorkplaceData.workplaceForUserDTO1);
        when(workplaceRepository.findByNumber(1))
                .thenReturn(expectedWorkplaces);
        List<WorkplaceForUserDTO> actualWorkplacesDTO = workplaceService
                .searchWorkplaceByNumber(1);
        Assertions.assertNotNull(actualWorkplacesDTO);
        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesDTO);
    }

    @Test
    public void searchWorkplaceByNumberNotFound() {
        List<WorkplaceEntity> expectedWorkplaceEntities = List.of();
        List<WorkplaceForAdminDTO> expectedWorkplacesDTO = List.of();

        when(workplaceRepository.findByNumber(3)).thenReturn(expectedWorkplaceEntities);
        List<WorkplaceForUserDTO> actualWorkplacesForUserDTO = workplaceService.searchWorkplaceByNumber(3);

        Assertions.assertEquals(expectedWorkplacesDTO, actualWorkplacesForUserDTO);
    }
}