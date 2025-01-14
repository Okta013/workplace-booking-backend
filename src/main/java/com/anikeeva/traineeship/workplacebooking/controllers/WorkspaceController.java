package com.anikeeva.traineeship.workplacebooking.controllers;

import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkspaceForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.services.WorkspaceService;
import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
@Tag(name = "Помещения", description = "Операции с помещениями")
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @GetMapping
    @Operation(summary = "Отображение списка помещений офиса", description =
            "Отображает помещения выбранного офиса с названиями, номерами этажей, номерами кабинетов и статусами")
    public List<WorkspaceDTO> showAllWorkspaces(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                                @RequestParam UUID officeId,
                                                @RequestParam(required = false) Boolean status) {
        return workspaceService.showAllWorkspaces(currentUser, officeId, status);
    }

    @GetMapping("/all")
    @Operation(summary = "Отображение всех активных помещений",
            description = "Отображает все активные помещения для изменения рабочего места")
    public List<WorkspaceForAdminDTO> showAllWorkspacesForSelect() {
        return workspaceService.showAllWorkspacesForSelect();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отображение помещения",
            description = "Отображает название, этаж, кабинет при наличии и статус помещения")
    public WorkspaceDTO showWorkspace(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                      @PathVariable UUID id) {
        return workspaceService.showWorkspace(currentUser, id);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск помещения", description = "Проводит поиск по имени, этажу и(или) кабинету")
    public List<WorkspaceDTO> searchWorkspace(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                              @RequestParam(required = false) UUID officeId,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false) Short floorNumber,
                                              @RequestParam(required = false) Short roomNumber) {
        return workspaceService.searchWorkspaces(currentUser, officeId, name, floorNumber, roomNumber);
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создание нового помещения",
            description = "Создает новое помещение, принимая название, этаж, кабинет (не обязательно) и id офиса")
    public WorkspaceForAdminDTO createWorkspace(@RequestBody WorkspaceForAdminDTO workspace) {
        return workspaceService.createWorkspace(workspace);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Редактирование помещения",
            description = "Позволяет изменить название, этаж, кабинет и id офиса")
    public WorkspaceForAdminDTO updateWorkspace(@PathVariable UUID id, @RequestBody WorkspaceForAdminDTO workspace) {
        return workspaceService.updateWorkspace(id, workspace);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление помещения",
            description = "Переводит статус помещения и всех его рабочих мест в 'удален'")
    public void deleteWorkspace(@PathVariable UUID id) {
        workspaceService.deleteWorkspace(id);
    }
}
