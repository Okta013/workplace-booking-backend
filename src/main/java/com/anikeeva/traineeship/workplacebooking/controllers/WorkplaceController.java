package com.anikeeva.traineeship.workplacebooking.controllers;

import com.anikeeva.traineeship.workplacebooking.dto.WorkplaceDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkplaceForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.services.WorkplaceService;
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
@RequestMapping("/workplaces")
@RequiredArgsConstructor
@Tag(name = "Рабочие места", description = "Операции с рабочими местами")
public class WorkplaceController {
    private final WorkplaceService workplaceService;

    @GetMapping
    @Operation(summary = "Отображение списка рабочих мест",
            description = "Отображает список рабочих мест с номерами, описаниями и статусами")
    public List<WorkplaceDTO> showAllWorkplaces(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                                @RequestParam UUID workspaceId,
                                                @RequestParam(required = false) Boolean status) {
        return workplaceService.showAllWorkplaces(currentUser, workspaceId, status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отображение рабочего места",
            description = "Отображает номер, описание со списком техники и статус рабочего места")
    public WorkplaceDTO showWorkplace(@AuthenticationPrincipal UserDetailsImpl currentUser, @PathVariable UUID id) {
        return workplaceService.showWorkplace(currentUser, id);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск рабочего места", description = "Проводит поиск по номеру рабочего места")
    public List<WorkplaceDTO> searchWorkplaces(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                               @RequestParam(required = false) UUID workspaceId,
                                               @RequestParam(required = false) Integer number) {
        return workplaceService.searchWorkplaces(currentUser, workspaceId, number);
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создание рабочего места",
            description = "Создает новое рабочее место, принимая номер, описание и id помещения")
    public WorkplaceForAdminDTO createWorkplace(@RequestBody WorkplaceForAdminDTO workplace) {
        return workplaceService.createWorkplace(workplace);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Редактирование рабочего места",
            description = "Позволяет изменить номер, описание и id помещения для выбранного рабочего места")
    public WorkplaceForAdminDTO updateWorkplace(@PathVariable UUID id, @RequestBody WorkplaceForAdminDTO workplace) {
        return workplaceService.updateWorkplace(id, workplace);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление рабочего места", description = "Переводит статус рабочего места в 'удален'")
    public void deleteWorkplace(@PathVariable UUID id) {
        workplaceService.deleteWorkplace(id);
    }
}
