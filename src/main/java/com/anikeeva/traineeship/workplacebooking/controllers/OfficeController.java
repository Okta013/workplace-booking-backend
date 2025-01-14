package com.anikeeva.traineeship.workplacebooking.controllers;

import com.anikeeva.traineeship.workplacebooking.dto.OfficeDTO;
import com.anikeeva.traineeship.workplacebooking.dto.OfficeForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.services.OfficeService;
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
@RequestMapping("/offices")
@RequiredArgsConstructor
@Tag(name = "Офисы", description = "Конструктор офисов")
public class OfficeController {
    private final OfficeService officeService;

    @GetMapping
    @Operation(summary = "Отображение всех офисов",
            description = "Отображает список всех офисов с названиями, адресами и статусами")
    public List<OfficeDTO> showAllOffices(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                          @RequestParam(required = false) Boolean status) {
        return officeService.showAllOffices(currentUser, status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отображение офиса", description = "Отображает название, адрес и статус")
    public OfficeDTO showOffice(@AuthenticationPrincipal UserDetailsImpl currentUser, @PathVariable UUID id) {
        return officeService.showOffice(currentUser, id);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск офиса", description = "Производит поиск офиса по названию")
    public List<OfficeDTO> searchOffices(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                                 @RequestParam(required = false) String name) {
        return officeService.searchOffice(currentUser, name);
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создание офиса", description = "Создает новый офис, принимая название и адрес")
    public OfficeForAdminDTO createOffice(@RequestBody OfficeForAdminDTO office) {
        return officeService.createOffice(office);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Редактирование офиса", description = "Позволяет изменить название, адрес и статус")
    public OfficeForAdminDTO updateOffice(@PathVariable UUID id, @RequestBody OfficeForAdminDTO office) {
        return officeService.updateOffice(id, office);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление офиса",
            description = "Переводит статус офиса, его помещений и рабочих мест в 'удален'")
    public void deleteOffice(@PathVariable UUID id) {
        officeService.deleteOffice(id);
    }
}
