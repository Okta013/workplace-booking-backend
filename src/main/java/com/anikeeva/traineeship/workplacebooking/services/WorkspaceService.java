package com.anikeeva.traineeship.workplacebooking.services;

import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceDTO;
import com.anikeeva.traineeship.workplacebooking.exceptions.BlankFieldsException;
import com.anikeeva.traineeship.workplacebooking.exceptions.ResourceNotFoundException;
import com.anikeeva.traineeship.workplacebooking.repositories.WorkspaceRepository;
import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsImpl;
import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceForUserDTO;
import com.anikeeva.traineeship.workplacebooking.entities.WorkspaceEntity;
import com.anikeeva.traineeship.workplacebooking.mappers.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;
    private final UserService userService;

    public WorkspaceForAdminDTO createWorkspace(final WorkspaceForAdminDTO workspaceForAdminDTO) {
        if (Objects.isNull(workspaceForAdminDTO.name()) || Objects.isNull(workspaceForAdminDTO.floorNumber())
                || Objects.isNull(workspaceForAdminDTO.officeId())) {
            throw new BlankFieldsException("Заполнены не все обязательные поля");
        }
        WorkspaceEntity workspaceEntity = workspaceMapper.toWorkspaceEntity(workspaceForAdminDTO);
        workspaceEntity.setIsDeleted(false);
        return workspaceMapper.toWorkspaceForAdminDTO(workspaceRepository.save(workspaceEntity));
    }

    public WorkspaceDTO showWorkspace(final UserDetailsImpl currentUser, final UUID id) {
        WorkspaceEntity workspaceEntity = workspaceRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Помещение не найдено"));
        return userService.checkAdminRole(currentUser.getId()) ?
                workspaceMapper.toWorkspaceForAdminDTO(workspaceEntity) :
                workspaceMapper.toWorkspaceForUserDTO(workspaceEntity);
    }

    public List<WorkspaceDTO> showAllWorkspaces(final UserDetailsImpl currentUser, final UUID officeId,
                                                final Boolean status) {
        List<WorkspaceEntity> workspaces = (Objects.isNull(status)) ? workspaceRepository.findByOfficeId(officeId) :
                workspaceRepository.findByOfficeIdAndIsDeleted(officeId, status);
        return workspaces.stream().map(workspaceEntity ->
                mapToWorkspaceDTO(workspaceEntity, checkCurrenUserRole(currentUser))).toList();
    }

    public List<WorkspaceForAdminDTO> showAllWorkspacesForSelect() {
        List<WorkspaceEntity> workspaces = workspaceRepository.findByIsDeleted(false);
        return workspaces.stream().map(workspaceMapper::toWorkspaceForAdminDTO).toList();
    }

    public Optional<WorkspaceEntity> getById(final UUID id) {
        return workspaceRepository.findById(id);
    }

    public List<WorkspaceDTO> searchWorkspaces(final UserDetailsImpl currentUser, final UUID officeId,
                                               final String name, final Short floorNumber, final Short roomNumber) {
        List<WorkspaceEntity> workspaces = (Objects.isNull(name) & Objects.isNull(floorNumber)
                & Objects.isNull(roomNumber)) ? workspaceRepository.findByOfficeId(officeId) :
                workspaceRepository.findByOfficeIdAndNameOrFloorNumberOrRoomNumber(officeId, name, floorNumber, roomNumber);
        return workspaces.stream().map(workspaceEntity ->
                        mapToWorkspaceDTO(workspaceEntity, checkCurrenUserRole(currentUser))).toList();
    }

    public WorkspaceForAdminDTO updateWorkspace(final UUID id, final WorkspaceForAdminDTO workspaceForAdminDTO) {
        WorkspaceEntity workspaceEntity = workspaceRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Помещение не найдено"));
        workspaceMapper.updateWorkspaceEntityFromWorkspaceForAdminDTO(workspaceForAdminDTO, workspaceEntity);
        workspaceEntity.setIsDeleted(false);
        return workspaceMapper.toWorkspaceForAdminDTO(workspaceRepository.save(workspaceEntity));
    }

    @Transactional
    public void deleteWorkspace(final UUID workspaceId) {
        WorkspaceEntity workspace = workspaceRepository.getWorkspace(workspaceId);
        if (Objects.nonNull(workspace)) {
            workspace.setIsDeleted(true);
            workspace.getWorkplaces().forEach(workplace -> {
                workplace.setIsDeleted(true);
                workplace.getBookings().forEach(booking -> {
                    booking.setConfirmed(false);
                    booking.setCancellationDate(LocalDateTime.now());
                    booking.setCancellationComment("Помещение было удалено");
                });
            });
            workspaceRepository.save(workspace);
        }
    }

    private boolean checkCurrenUserRole(final UserDetailsImpl currentUser) {
        return userService.checkAdminRole(currentUser.getId());
    }

    private WorkspaceDTO mapToWorkspaceDTO(final WorkspaceEntity workspaceEntity, final boolean isAdmin) {
        return isAdmin ? workspaceMapper.toWorkspaceForAdminDTO(workspaceEntity) :
                workspaceMapper.toWorkspaceForUserDTO(workspaceEntity);
    }

    @Deprecated
    public List<WorkspaceForUserDTO> searchWorkspacesByName(final String name) {
        if (Objects.isNull(name)) {
            return workspaceRepository.findAll().stream().map(workspaceMapper::toWorkspaceForUserDTO).toList();
        }
        return workspaceRepository.findByName(name).stream().map(workspaceMapper::toWorkspaceForUserDTO).toList();
    }
}
