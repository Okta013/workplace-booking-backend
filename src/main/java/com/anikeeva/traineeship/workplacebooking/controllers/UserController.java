package com.anikeeva.traineeship.workplacebooking.controllers;

import com.anikeeva.traineeship.workplacebooking.dto.UserDTO;
import com.anikeeva.traineeship.workplacebooking.dto.UserForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.payload.request.ChangePasswordRequest;
import com.anikeeva.traineeship.workplacebooking.services.UserService;
import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Операции с пользователями")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Отображение профиля пользователя",
            description = "Отображает ФИО, телефон и email пользователя, вошедшего в систему")
    public UserDTO showUser(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return userService.showUser(currentUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Отображение профиля пользователя администратору",
            description = "Отображает администратору страницу с подробным описанием польвателя и операциями над профилем")
    public UserForAdminDTO showUserForAdmin(@PathVariable UUID id) {
        return userService.showUserForAdmin(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Отображение всех пользователей",
            description = "Отображает ФИО всех пользователей")
    public List<UserForAdminDTO> showAllUsers(@RequestParam(required = false) Boolean status) {
        return userService.showAllUsers(status);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Поиск пользователей", description = "Поиск пользователей по ФИО, телефону или email")
    public List<UserForAdminDTO> searchUsers(@RequestParam(required = false) String fullName,
                                             @RequestParam(required = false) String phoneNumber,
                                             @RequestParam(required = false) String email) {
        return userService.searchUsers(fullName, phoneNumber, email);
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создание нового пользователя",
            description = "Создает пользователя, принимая ФИО, телефон и email, отправляет пароль по указанному email")
    public UserForAdminDTO createUser(@RequestBody UserForAdminDTO userForAdminDTO) {
        return userService.createUser(userForAdminDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Изменение выбранного пользователя",
            description = "Можно изменить ФИО, телефон, email и статус пользователя")
    public UserForAdminDTO updateUser(@PathVariable UUID id, @RequestBody UserForAdminDTO userForAdminDTO) {
        return userService.updateUser(id, userForAdminDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление пользователя",
            description = "Пользователь остается в базе, статус его профиля и статусы бронирований меняются на 'удален'")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @PutMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Изменение пароля пользователя", description = "Позволяет пользователю изменить свой пароль.")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                                 @RequestBody ChangePasswordRequest request) {
        userService.changePassword(currentUser, request);
        return ResponseEntity.ok("Password changed successfully.");
    }

    @GetMapping("/role")
    @Operation(summary = "Определение роли пользователя",
            description = "Возвращает True, если текущий пользователь администратор, используется для взаимодействия с фронтендом")
    public boolean isAdmin(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return userService.checkAdminRole(currentUser.getId());
    }
}