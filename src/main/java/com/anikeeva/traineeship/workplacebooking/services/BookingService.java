package com.anikeeva.traineeship.workplacebooking.services;

import com.anikeeva.traineeship.workplacebooking.dto.BookingDTO;
import com.anikeeva.traineeship.workplacebooking.dto.BookingForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.BookingForUserDTO;
import com.anikeeva.traineeship.workplacebooking.exceptions.ResourceNotFoundException;
import com.anikeeva.traineeship.workplacebooking.exceptions.TimeOutForConfirmException;
import com.anikeeva.traineeship.workplacebooking.repositories.BookingRepository;
import com.anikeeva.traineeship.workplacebooking.repositories.OfficeRepository;
import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsImpl;
import com.anikeeva.traineeship.workplacebooking.validatorbooking.BookingValidator;
import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.entities.OfficeEntity;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import com.anikeeva.traineeship.workplacebooking.entities.WorkplaceEntity;
import com.anikeeva.traineeship.workplacebooking.entities.WorkspaceEntity;
import com.anikeeva.traineeship.workplacebooking.entities.enums.StatusEnum;
import com.anikeeva.traineeship.workplacebooking.mappers.BookingMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final WorkplaceService workplaceService;
    private final WorkspaceService workspaceService;
    private final OfficeService officeService;
    private final UserService userService;

    private final int MINUTES_FOR_CONFIRMATION = 15;

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    @Setter
    private List<BookingValidator> bookingValidators;
    @Autowired
    private OfficeRepository officeRepository;

    @Transactional
    public BookingDTO createBooking(final UserDetailsImpl currentUser,
                                    final UUID workplaceId,
                                    final BookingForAdminDTO bookingForAdminDTO,
                                    final String username) {
        boolean isAdmin = userService.checkAdminRole(currentUser.getId());
        BookingEntity bookingEntity = bookingMapper.toBookingEntity(bookingForAdminDTO);
        bookingEntity.setWorkplaceId(workplaceId);
        bookingEntity.setBookingDate(LocalDateTime.now());
        if (Objects.nonNull(username)) {
            UserEntity user = userService.getUserByName(username);
            bookingEntity.setUserId(user.getId());
        }
        else {
            bookingEntity.setUserId(currentUser.getId());
        }
        int bookingMinutes = Math.toIntExact(
                Duration.between(bookingEntity.getBookingStart(), bookingEntity.getBookingEnd()).toMinutes()
        );
        for (BookingValidator bookingValidator : bookingValidators) {
            if (!bookingValidator.check(bookingEntity)) {
                return null;
            }
        }
        UserEntity userEntity = userService.getUserById(bookingEntity.getUserId());
        userEntity.setAvailableMinutes(userEntity.getAvailableMinutes() - bookingMinutes);
        userService.update(userEntity);
        if (isAdmin) {
            return convertToBookingForAdminDTO(bookingRepository.save(bookingEntity));
        }
        return convertToBookingForUserDTO(bookingRepository.save(bookingEntity));
    }

    public BookingDTO showBooking(final UserDetailsImpl currentUser, final UUID id) {
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Бронирование не найдено"));
        if (userService.checkAdminRole(currentUser.getId())) {
            return convertToBookingForAdminDTO(bookingEntity);
        }
        return convertToBookingForUserDTO(bookingEntity);
    }

    public List<BookingDTO> showAllBookings(final UserDetailsImpl currentUser, final StatusEnum status) {
        List<BookingDTO> bookingDTOs = new ArrayList<>();
        List<BookingEntity> bookingEntities;
        switch (status) {
            case null:
                bookingEntities = bookingRepository.findAll();
                break;
            case ACTIVE:
                bookingEntities = bookingRepository.getByCancellationDateNull();
                break;
            case DELETED:
                bookingEntities = bookingRepository.getByCancellationDateNotNull();
                break;
        }
        if (userService.checkAdminRole(currentUser.getId())) {
            for (BookingEntity bookingEntity : bookingEntities) {
                bookingDTOs.add(convertToBookingForAdminDTO(bookingEntity));
            }
        }
        else {
            for (BookingEntity bookingEntity : bookingEntities) {
                bookingDTOs.add(convertToBookingForUserDTO(bookingEntity));
            }
        }
        return bookingDTOs;
    }

    public List<BookingForAdminDTO> searchBookings(final String officeName, final String workspaceName,
                                                   final Integer workplaceNumber, final String username) {
        List<BookingForAdminDTO> bookingsDTO = new ArrayList<>();
        List<BookingEntity> bookingsEntity = bookingRepository
                .findBookings(officeName, workspaceName, workplaceNumber, username);
        for(BookingEntity bookingEntity : bookingsEntity) {
            bookingsDTO.add(convertToBookingForAdminDTO(bookingEntity));
        }
        return bookingsDTO;
    }

    public BookingDTO showCurrentBooking(final UserDetailsImpl currentUser) {
        BookingEntity bookingEntity = bookingRepository.findCurrentBooking(currentUser.getId(), LocalDateTime.now());
        if (Objects.isNull(bookingEntity) || !bookingEntity.isConfirmed()) {
            return null;
        }
        if (userService.checkAdminRole(currentUser.getId())) {
            return convertToBookingForAdminDTO(bookingEntity);
        }
        return convertToBookingForUserDTO(bookingEntity);
    }

    public List<BookingDTO> showBookingsByConfirmed(final UserDetailsImpl currentUser, final boolean isConfirmed) {
        List<BookingEntity> bookings = bookingRepository
                .findByUserIdAndIsConfirmedAndCancellationComment(currentUser.getId(), isConfirmed, null);
        List<BookingDTO> bookingsDTO = new ArrayList<>();
        if (userService.checkAdminRole(currentUser.getId())) {
            for(BookingEntity bookingEntity : bookings) {
                bookingsDTO.add(convertToBookingForAdminDTO(bookingEntity));
            }
        }
        else {
            for(BookingEntity bookingEntity : bookings) {
                bookingsDTO.add(convertToBookingForUserDTO(bookingEntity));
            }
        }
        return bookingsDTO;
    }

    public void cancelBooking(final UserDetailsImpl currentUser, final UUID id, final String reason) {
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Бронирование не найдено"));
        UserEntity userEntity = userService.getUserById(bookingEntity.getUserId());
        int bookingMinutes = Math.toIntExact(Duration.between(bookingEntity.getBookingStart(),
                bookingEntity.getBookingEnd()).toMinutes());
        if (bookingEntity.getUserId().equals(currentUser.getId()) || userService.checkAdminRole(currentUser.getId())) {
            bookingEntity.setConfirmed(false);
            bookingEntity.setCancellationDate(LocalDateTime.now());
            bookingEntity.setCancellationComment(reason);
            bookingRepository.save(bookingEntity);
            userEntity.setAvailableMinutes(userEntity.getAvailableMinutes() + bookingMinutes);
            userService.update(userEntity);
        }
    }

    public void confirmBooking(final UserDetailsImpl currentUser, final UUID id) {
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Бронирование не найдено"));
        if (bookingEntity.getUserId().equals(currentUser.getId())) {
            if(LocalDateTime.now().isAfter(bookingEntity.getBookingEnd())) {
                throw new TimeOutForConfirmException("Невозможно подтвердить прошедшее бронирование");
            }
            bookingEntity.setConfirmed(true);
            bookingRepository.save(bookingEntity);
        }
    }

    @Transactional
    public void completeBooking() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingEntity> bookingEntities =
                bookingRepository.findByIsConfirmedAndCancellationDate(true, null);
        for(BookingEntity bookingEntity : bookingEntities) {
            if (Duration.between(bookingEntity.getBookingEnd(), now).toMinutes() >= 0) {
                bookingEntity.setCancellationDate(now);
                bookingEntity.setCancellationComment("Бронирование завершилось");
                bookingRepository.save(bookingEntity);
                changeAvailableMinutesForUser(bookingEntity);
            }
        }
    }

    @Transactional
    public void autoCancelBooking() {
        List<BookingEntity> bookingEntities =
                bookingRepository.findByIsConfirmedAndCancellationDate(false, null);
        LocalDateTime now = LocalDateTime.now();
        for(BookingEntity bookingEntity : bookingEntities) {
            if (Duration.between(bookingEntity.getBookingDate(), now).toMinutes() > MINUTES_FOR_CONFIRMATION) {
                bookingEntity.setCancellationDate(now);
                bookingEntity.setCancellationComment("Бронирование не подтверждено в отведенное время");
                bookingRepository.save(bookingEntity);
                changeAvailableMinutesForUser(bookingEntity);
            }
        }
    }

    private WorkplaceEntity getWorkplaceByBooking(final BookingEntity bookingEntity) {
        return workplaceService.getById(bookingEntity.getWorkplaceId());
    }

    private WorkspaceEntity getWorkspaceByBooking(final BookingEntity bookingEntity) {
        return workspaceService.getById(getWorkplaceByBooking(bookingEntity).getWorkspaceId()).orElseThrow(()
                -> new ResourceNotFoundException("Помещение не найдено"));
    }

    private OfficeEntity getOfficeByBooking(final BookingEntity bookingEntity) {
        return officeService.getById(getWorkspaceByBooking(bookingEntity).getOfficeId()).orElseThrow(() ->
                new ResourceNotFoundException("Офис не найден"));
    }

    private UserEntity getUserByBooking(final BookingEntity bookingEntity) {
        return userService.getUserById(bookingEntity.getUserId());
    }

    private BookingForUserDTO convertToBookingForUserDTO(final BookingEntity bookingEntity) {
        BookingForUserDTO bookingForUserDTO = bookingMapper.toBookingForUserDTO(bookingEntity, getOfficeByBooking(bookingEntity),
                getWorkspaceByBooking(bookingEntity), getWorkplaceByBooking(bookingEntity));
        LocalDateTime.parse(bookingEntity.getBookingStart().format(ISO_FORMATTER));
        LocalDateTime.parse(bookingEntity.getBookingEnd().format(ISO_FORMATTER));
        return bookingForUserDTO;
    }

    private BookingForAdminDTO convertToBookingForAdminDTO(final BookingEntity bookingEntity) {
        BookingForAdminDTO bookingForAdminDTO = bookingMapper.toBookingForAdminDTO(bookingEntity, getOfficeByBooking(bookingEntity),
                getWorkspaceByBooking(bookingEntity), getWorkplaceByBooking(bookingEntity),
                getUserByBooking(bookingEntity));
        LocalDateTime.parse(bookingEntity.getBookingStart().format(ISO_FORMATTER));
        LocalDateTime.parse(bookingEntity.getBookingEnd().format(ISO_FORMATTER));
        return bookingForAdminDTO;
    }

    private void changeAvailableMinutesForUser(final BookingEntity bookingEntity) {
        UserEntity userEntity = userService.getUserById(bookingEntity.getUserId());
        userEntity.setAvailableMinutes(userEntity.getAvailableMinutes() + Math.toIntExact(
                Duration.between(bookingEntity.getBookingStart(), bookingEntity.getBookingEnd()).toMinutes())
        );
        userService.update(userEntity);
    }
}