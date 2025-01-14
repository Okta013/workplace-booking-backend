package com.anikeeva.traineeship.workplacebooking.controllers;

import com.anikeeva.traineeship.workplacebooking.dto.BookingDTO;
import com.anikeeva.traineeship.workplacebooking.dto.BookingForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.BookingForUserDTO;
import com.anikeeva.traineeship.workplacebooking.dto.WorkplaceDTO;
import com.anikeeva.traineeship.workplacebooking.entities.enums.CreateWayEnum;
import com.anikeeva.traineeship.workplacebooking.entities.enums.StatusEnum;
import com.anikeeva.traineeship.workplacebooking.services.BookingService;
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
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Бронирования", description = "Операции с бронированиями")
public class BookingController {
    private final BookingService bookingService;
    private final WorkplaceService workplaceService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Отображение всех бронирований",
            description = "Отображает все бронирования для администратора")
    public List<BookingDTO> showAllBookings(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                            @RequestParam(required = false) StatusEnum status) {
        return bookingService.showAllBookings(currentUser, status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отображение выбранного бронирования",
            description = "Отображает все бронирования для пользователя или администратора")
    public BookingDTO showBooking(@AuthenticationPrincipal UserDetailsImpl currentUser, @PathVariable UUID id) {
        return bookingService.showBooking(currentUser, id);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск бронирований",
            description = "Позволяет администратору искать бронирования по названию офиса, названию помещения, " +
                    "номеру рабочего места, имени пользователя или дате бронирования")
    public List<BookingForAdminDTO> searchBookings(@RequestParam(required = false) String officeName,
                                                   @RequestParam(required = false) String workspaceName,
                                                   @RequestParam(required = false) Integer workplaceNumber,
                                                   @RequestParam(required = false) String username) {
        return bookingService.searchBookings(officeName, workspaceName, workplaceNumber, username);
    }

    @GetMapping("/available")
    @Operation(summary = "Отображение доступных для бронирования рабочих мест",
            description = "Отображает список всех активных рабочих мест в зависимости от выбранного способа создания")
    public List<WorkplaceDTO> showWorkplacesForBookingByWorkplace(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return workplaceService.getWorkplacesForNewBooking(currentUser, CreateWayEnum.WORKPLACE, null);
    }

    @PostMapping("available/byDate")
    @Operation(summary = "Отображение рабочих мест для бронирования по дате",
            description = "Отображает список доступных рабочих мест для выбранного периода")
    public List<WorkplaceDTO> showWorkplacesForBookingByDate(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                                             @RequestBody BookingForUserDTO bookingForUserDTO) {
        return workplaceService.getWorkplacesForNewBooking(currentUser, CreateWayEnum.DATE, bookingForUserDTO);
    }

    @PostMapping("/{workplaceId}")
    @Operation(summary = "Создание нового бронирования на рабочее место",
            description = "Создает бронирование по id рабочего места, дате начала и конца бронирования")
    public BookingDTO createBooking(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                                      @PathVariable UUID workplaceId,
                                                      @RequestBody BookingForAdminDTO bookingForAdminDTO,
                                    @RequestParam(required = false) String username) {
        return bookingService.createBooking(currentUser, workplaceId, bookingForAdminDTO, username);
    }

    @GetMapping("/current")
    @Operation(summary = "Отображение текущего бронирования",
            description = "Отображает текущее бронирование текущего пользователя")
    public BookingDTO showCurrentBooking(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return bookingService.showCurrentBooking(currentUser);
    }

    @GetMapping("/confirmed")
    @Operation(summary = "Отображение подтвержденных бронирований",
            description = "Отображает подтвержденные бронирования текущего пользователя")
    public List<BookingDTO> showConfirmedBookings(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return bookingService.showBookingsByConfirmed(currentUser, true);
    }

    @GetMapping("/unconfirmed")
    @Operation(summary = "Отображение неподтвержденных бронирований",
            description = "Отображает неподтвержденные бронирования текущего пользователя")
    public List<BookingDTO> showUnconfirmedBookings(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return bookingService.showBookingsByConfirmed(currentUser, false);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Отмена бронирования", description = "Отменяет выбранное бронирование текущего пользователя")
    public void cancelBooking(@AuthenticationPrincipal UserDetailsImpl currentUser, @PathVariable UUID id,
                              @RequestParam String reason) {
        bookingService.cancelBooking(currentUser, id, reason);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Подтверждение бронирования",
            description = "Подтверждает выбранное бронирование текущего пользователя")
    public void confirmBooking(@AuthenticationPrincipal UserDetailsImpl currentUser, @PathVariable UUID id) {
        bookingService.confirmBooking(currentUser, id);
    }
}
