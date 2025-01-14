package com.anikeeva.traineeship.workplacebooking.services;

import com.anikeeva.traineeship.workplacebooking.dto.BookingDTO;
import com.anikeeva.traineeship.workplacebooking.dto.BookingForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.BookingForUserDTO;
import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.entities.OfficeEntity;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import com.anikeeva.traineeship.workplacebooking.entities.WorkplaceEntity;
import com.anikeeva.traineeship.workplacebooking.entities.WorkspaceEntity;
import com.anikeeva.traineeship.workplacebooking.entities.enums.StatusEnum;
import com.anikeeva.traineeship.workplacebooking.exceptions.ResourceNotFoundException;
import com.anikeeva.traineeship.workplacebooking.exceptions.TimeOutForConfirmException;
import com.anikeeva.traineeship.workplacebooking.mappers.BookingMapper;
import com.anikeeva.traineeship.workplacebooking.repositories.BookingRepository;
import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsImpl;
import com.anikeeva.traineeship.workplacebooking.validatorbooking.BookingValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private OfficeService officeService;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private WorkplaceService workplaceService;

    @Mock
    private UserService userService;

    @Mock
    private UserEntity userEntity;

    private static class TestBookingData {
        UUID bookingId = UUID.randomUUID();
        UUID bookingId2 = UUID.randomUUID();
        UUID bookingId3 = UUID.randomUUID();
        UUID bookingId4 = UUID.randomUUID();
        UUID unconfirmedBookingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID workplaceId = UUID.randomUUID();
        UUID workplaceId2 = UUID.randomUUID();
        UUID workplaceId3 = UUID.randomUUID();
        UUID workplaceId4 = UUID.randomUUID();
        UUID officeId = UUID.randomUUID();
        UUID workspaceId = UUID.randomUUID();
        LocalDateTime bookingDate = LocalDateTime.now();
        LocalDateTime bookingStart = LocalDateTime.now().plusDays(1);
        LocalDateTime bookingEnd = LocalDateTime.now().plusDays(2);
        BookingEntity bookingEntity = new BookingEntity(
                bookingId,
                bookingDate,
                bookingStart,
                bookingEnd,
                userId,
                workplaceId,
                true,
                null,
                null
        );
        BookingEntity bookingEntity2 = new BookingEntity(
                bookingId2,
                bookingDate,
                bookingStart,
                bookingEnd,
                userId,
                workplaceId2,
                true,
                null,
                null
        );
        BookingEntity bookingEntity3 = new BookingEntity(
                bookingId3,
                bookingDate,
                bookingStart,
                bookingEnd,
                userId,
                workplaceId3,
                true,
                LocalDateTime.of(2024, Month.JULY, 3, 11, 30, 0),
                "comment"
        );
        BookingEntity bookingEntity4 = new BookingEntity(
                bookingId4,
                bookingDate,
                bookingStart,
                bookingEnd,
                userId,
                workplaceId4,
                true,
                LocalDateTime.of(2024, Month.JULY, 3, 11, 30, 0),
                "comment"
        );
        List<BookingEntity> allBookingsEntity = Arrays.asList(bookingEntity, bookingEntity2, bookingEntity3,
                bookingEntity4);
        List<BookingEntity> activeBookingsEntity = Arrays.asList(bookingEntity, bookingEntity2);
        List<BookingEntity> deletedBookingsEntity = Arrays.asList(bookingEntity3, bookingEntity4);
        UserEntity userEntity = new UserEntity(
                userId,
                "Full Name",
                "89009009090",
                "email@gmail.com",
                "password",
                2400
        );
        BookingEntity unconfirmedBookingEntity = new BookingEntity(
                unconfirmedBookingId,
                bookingDate,
                bookingStart,
                bookingEnd,
                userId,
                workplaceId,
                false,
                null,
                null
        );
        BookingEntity pastBookingEntity = new BookingEntity(
                bookingId,
                LocalDateTime.of(2024, Month.JULY, 3, 11, 30, 0),
                LocalDateTime.of(2024, Month.JULY, 4, 9, 0, 0),
                LocalDateTime.of(2024, Month.JULY, 4, 18, 0, 0),
                userId,
                workplaceId,
                true,
                null,
                null
        );
        BookingEntity completedBookingEntity = new BookingEntity(
                bookingId,
                bookingDate.minusDays(1),
                bookingDate.minusHours(5),
                bookingDate.minusMinutes(30),
                userId,
                workplaceId,
                true,
                null,
                null
        );
        BookingEntity bookingEntityToAutoCancel = new BookingEntity(
                bookingId,
                bookingDate.minusMinutes(30),
                bookingStart,
                bookingEnd,
                userId,
                workplaceId,
                false,
                null,
                null
        );
        OfficeEntity officeEntity = new OfficeEntity(officeId, "Address", "Name", false);
        WorkspaceEntity workspaceEntity = new WorkspaceEntity(workspaceId, "Name", (short) 1 , (short) 2,
                false, officeId);
        WorkplaceEntity workplaceEntity = new WorkplaceEntity(workplaceId, 1, "Description",
                workspaceId, false);
        WorkplaceEntity workplaceEntity2 = new WorkplaceEntity(workplaceId2, 2, "Description",
                workspaceId, false);
        WorkplaceEntity workplaceEntity3 = new WorkplaceEntity(workplaceId3, 3, "Description",
                workspaceId, false);
        WorkplaceEntity workplaceEntity4 = new WorkplaceEntity(workplaceId4, 4, "Description",
                workspaceId, false);
        BookingForUserDTO bookingForUserDTO = new BookingForUserDTO(
                bookingId,
                "Name",
                "Name",
                (short) 1,
                1,
                bookingStart,
                bookingEnd,
                StatusEnum.ACTIVE,
                false,
                "Description"
        );
        BookingForUserDTO bookingForUserDTO2 = new BookingForUserDTO(
                bookingId2,
                "Name",
                "Name",
                (short) 1,
                2,
                bookingStart,
                bookingEnd,
                StatusEnum.ACTIVE,
                false,
                "Description"
        );
        BookingForUserDTO bookingForUserDTO3 = new BookingForUserDTO(
                bookingId3,
                "Name",
                "Name",
                (short) 1,
                3,
                bookingStart,
                bookingEnd,
                StatusEnum.ACTIVE,
                false,
                "Description"
        );
        BookingForUserDTO bookingForUserDTO4 = new BookingForUserDTO(
                bookingId4,
                "Name",
                "Name",
                (short) 1,
                4,
                bookingStart,
                bookingEnd,
                StatusEnum.ACTIVE,
                false,
                "Description"
        );
        List<BookingForUserDTO> allBookingsForUserDTO = Arrays.asList(bookingForUserDTO, bookingForUserDTO2,
                bookingForUserDTO3, bookingForUserDTO4);
        List<BookingForUserDTO> bookingsWithActiveStatusForUser = Arrays.asList(bookingForUserDTO, bookingForUserDTO2);
        List<BookingForUserDTO> bookingsWithDeletedStatusForUser = Arrays.asList(bookingForUserDTO3, bookingForUserDTO4);
        BookingForAdminDTO bookingForAdminDTO = new BookingForAdminDTO(
                bookingId,
                "Name",
                "Name",
                (short) 1,
                1,
                bookingStart,
                bookingEnd,
                "Description",
                StatusEnum.ACTIVE,
                false,
                "Full Name"
        );
        BookingForAdminDTO bookingForAdminDTO2 = new BookingForAdminDTO(
                bookingId2,
                "Name",
                "Name",
                (short) 1,
                2,
                bookingStart,
                bookingEnd,
                "Description",
                StatusEnum.ACTIVE,
                false,
                "Full Name"
        );
        BookingForAdminDTO bookingForAdminDTO3 = new BookingForAdminDTO(
                bookingId3,
                "Name",
                "Name",
                (short) 1,
                3,
                bookingStart,
                bookingEnd,
                "Description",
                StatusEnum.DELETED,
                false,
                "Full Name"
        );
        BookingForAdminDTO bookingForAdminDTO4 = new BookingForAdminDTO(
                bookingId4,
                "Name",
                "Name",
                (short) 1,
                4,
                bookingStart,
                bookingEnd,
                "Description",
                StatusEnum.DELETED,
                false,
                "Full Name"
        );
        List<BookingForAdminDTO> allBookingsDTO = Arrays.asList(bookingForAdminDTO, bookingForAdminDTO2,
                bookingForAdminDTO3, bookingForAdminDTO4);
        List<BookingForAdminDTO> bookingsWithActiveStatus = Arrays.asList(bookingForAdminDTO, bookingForAdminDTO2);
        List<BookingForAdminDTO> bookingsWithDeletedStatus = Arrays.asList(bookingForAdminDTO3, bookingForAdminDTO4);
        UserDetailsImpl currentUser = new UserDetailsImpl(
                userId,
                "Full Name",
                "89009009090",
                "email@gmail.com",
                "password",
                false
                );
        BookingValidator validator1 = mock(BookingValidator.class);
        BookingValidator validator2 = mock(BookingValidator.class);
        BookingValidator validator3 = mock(BookingValidator.class);
        BookingValidator validator4 = mock(BookingValidator.class);
        BookingValidator validator5 = mock(BookingValidator.class);
        BookingValidator validator6 = mock(BookingValidator.class);
        BookingValidator validator7 = mock(BookingValidator.class);
        List<BookingValidator> mockValidators = Arrays.asList(validator1, validator2, validator3, validator4,
                validator5, validator6, validator7);
    }

//    @Test
//    public void createBookingByAdmin() {
//        TestBookingData testBookingData = new TestBookingData();
//        UserEntity userEntity = testBookingData.userEntity;
//        BookingEntity bookingEntity = testBookingData.bookingEntity;
//        bookingService.setBookingValidators(testBookingData.mockValidators);
//        BookingForAdminDTO expectedBookingForAdminDTO = testBookingData.bookingForAdminDTO;
//
//        when(userService.getUserById(testBookingData.userId)).thenReturn(userEntity);
//        when(bookingMapper.toBookingEntity(expectedBookingForAdminDTO)).thenReturn(bookingEntity);
//        doNothing().when(userService).update(any(UserEntity.class));
//        when(workplaceService.getById(testBookingData.workplaceId))
//                .thenReturn(testBookingData.workplaceEntity);
//        when(workspaceService.getById(testBookingData.workspaceId))
//                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
//        when(officeService.getById(testBookingData.officeId))
//                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
//        when(userService.getUserById(testBookingData.userId)).thenReturn(testBookingData.userEntity);
//        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity, testBookingData.officeEntity,
//                testBookingData.workspaceEntity, testBookingData.workplaceEntity, testBookingData.userEntity))
//                .thenReturn(expectedBookingForAdminDTO);
//        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);
//        when(userService.checkAdminRole(any())).thenReturn(true);
//        when(userService.getUserByName("Full Name")).thenReturn(userEntity);
//        when(testBookingData.validator1.check(any())).thenReturn(true);
//        when(testBookingData.validator2.check(any())).thenReturn(true);
//        when(testBookingData.validator3.check(any())).thenReturn(true);
//        when(testBookingData.validator4.check(any())).thenReturn(true);
//        when(testBookingData.validator5.check(any())).thenReturn(true);
//        when(testBookingData.validator6.check(any())).thenReturn(true);
//        when(testBookingData.validator7.check(any())).thenReturn(true);
//        BookingDTO actualBookingForAdminDTO = bookingService.createBooking(testBookingData.currentUser,
//                testBookingData.workplaceId, expectedBookingForAdminDTO);
//
//        Assertions.assertNotNull(actualBookingForAdminDTO);
//        Assertions.assertEquals(expectedBookingForAdminDTO, actualBookingForAdminDTO);
//    }

//    @Test
//    public void createBookingByUser() {
//        TestBookingData testBookingData = new TestBookingData();
//        UserEntity user = testBookingData.userEntity;
//        BookingEntity bookingEntity = testBookingData.bookingEntity;
//        bookingService.setBookingValidators(testBookingData.mockValidators);
//        BookingForUserDTO expectedBookingForUserDTO = testBookingData.bookingForUserDTO;
//
//        when(userService.getUserById(testBookingData.userId)).thenReturn(user);
//        doNothing().when(userService).update(any(UserEntity.class));
//        when(bookingMapper.toBookingEntity(testBookingData.bookingForAdminDTO)).thenReturn(bookingEntity);
//        when(workplaceService.getById(testBookingData.workplaceId))
//                .thenReturn(testBookingData.workplaceEntity);
//        when(workspaceService.getById(testBookingData.workspaceId))
//                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
//        when(officeService.getById(testBookingData.officeId))
//                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
//        when(userService.getUserById(any())).thenReturn(user);
//        when(bookingMapper.toBookingForUserDTO(testBookingData.bookingEntity, testBookingData.officeEntity,
//                testBookingData.workspaceEntity, testBookingData.workplaceEntity))
//                .thenReturn(expectedBookingForUserDTO);
//        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);
//        when(userService.checkAdminRole(any())).thenReturn(false);
//        when(testBookingData.validator1.check(any())).thenReturn(true);
//        when(testBookingData.validator2.check(any())).thenReturn(true);
//        when(testBookingData.validator3.check(any())).thenReturn(true);
//        when(testBookingData.validator4.check(any())).thenReturn(true);
//        when(testBookingData.validator5.check(any())).thenReturn(true);
//        when(testBookingData.validator6.check(any())).thenReturn(true);
//        when(testBookingData.validator7.check(any())).thenReturn(true);
//        BookingDTO actualBookingForUserDTO = bookingService.createBooking(testBookingData.currentUser,
//                testBookingData.workplaceId, testBookingData.bookingForAdminDTO);
//
//        Assertions.assertNotNull(actualBookingForUserDTO);
//        Assertions.assertEquals(expectedBookingForUserDTO, actualBookingForUserDTO);
//    }

//    @Test
//    public void createBookingWithFailChecks() {
//        TestBookingData testBookingData = new TestBookingData();
//        UserEntity userEntity = testBookingData.userEntity;
//        BookingEntity bookingEntity = testBookingData.bookingEntity;
//        bookingService.setBookingValidators(testBookingData.mockValidators);
//
//        when(userService.getUserById(testBookingData.userId)).thenReturn(userEntity);
//        when(bookingMapper.toBookingEntity(any(BookingForAdminDTO.class))).thenReturn(bookingEntity);
//        when(testBookingData.validator1.check(any())).thenReturn(false);
//        when(userService.checkAdminRole(testBookingData.userId)).thenReturn(false);
//        BookingDTO actualBookingDTO = bookingService.createBooking(testBookingData.currentUser,
//                testBookingData.workplaceId, testBookingData.bookingForAdminDTO);
//
//        Assertions.assertNull(actualBookingDTO);
//    }

    @Test
    public void showAllBookingsForAdmin() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingForAdminDTO> expectedBookingsDTO = testBookingData.allBookingsDTO;

        when(bookingRepository.findAll()).thenReturn(testBookingData.allBookingsEntity);
        when(workplaceService.getById(testBookingData.workplaceId))
                .thenReturn(testBookingData.workplaceEntity);
        when(workplaceService.getById(testBookingData.workplaceId2))
                .thenReturn(testBookingData.workplaceEntity2);
        when(workplaceService.getById(testBookingData.workplaceId3))
                .thenReturn(testBookingData.workplaceEntity3);
        when(workplaceService.getById(testBookingData.workplaceId4))
                .thenReturn(testBookingData.workplaceEntity4);
        when(workspaceService.getById(testBookingData.workspaceId))
                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
        when(officeService.getById(testBookingData.officeId))
                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
        when(userService.getUserById(testBookingData.userId)).thenReturn(testBookingData.userEntity);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity2, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity2, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO2);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity3, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity3, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO3);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity4, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity4, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO4);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<BookingDTO> actualBookingsForAdminDTO = bookingService.showAllBookings(testBookingData.currentUser,null);

        Assertions.assertNotNull(actualBookingsForAdminDTO);
        Assertions.assertEquals(expectedBookingsDTO.size(), actualBookingsForAdminDTO.size());
        Assertions.assertEquals(expectedBookingsDTO, actualBookingsForAdminDTO);
    }

    @Test
    public void showAllBookingsWithActiveStatusForAdmin() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingForAdminDTO> expectedBookingsDTO = testBookingData.bookingsWithActiveStatus;

        when(bookingRepository.getByCancellationDateNull()).thenReturn(testBookingData.activeBookingsEntity);
        when(workplaceService.getById(testBookingData.workplaceId)).thenReturn(testBookingData.workplaceEntity);
        when(workplaceService.getById(testBookingData.workplaceId2)).thenReturn(testBookingData.workplaceEntity2);
        when(workspaceService.getById(testBookingData.workspaceId))
                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
        when(officeService.getById(testBookingData.officeId))
                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
        when(userService.getUserById(testBookingData.userId)).thenReturn(testBookingData.userEntity);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity2, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity2, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO2);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<BookingDTO> actualBookingsForAdminDTO = bookingService.showAllBookings(testBookingData.currentUser,
                StatusEnum.ACTIVE);

        Assertions.assertNotNull(actualBookingsForAdminDTO);
        Assertions.assertEquals(expectedBookingsDTO.size(), actualBookingsForAdminDTO.size());
        Assertions.assertEquals(expectedBookingsDTO, actualBookingsForAdminDTO);
    }

    @Test
    public void showAllBookingsWithDeletedStatusForAdmin() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingForAdminDTO> expectedBookingsDTO = testBookingData.bookingsWithDeletedStatus;

        when(bookingRepository.getByCancellationDateNotNull()).thenReturn(testBookingData.deletedBookingsEntity);
        when(workplaceService.getById(testBookingData.workplaceId3)).thenReturn(testBookingData.workplaceEntity3);
        when(workplaceService.getById(testBookingData.workplaceId4)).thenReturn(testBookingData.workplaceEntity4);
        when(workspaceService.getById(testBookingData.workspaceId))
                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
        when(officeService.getById(testBookingData.officeId))
                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
        when(userService.getUserById(testBookingData.userId)).thenReturn(testBookingData.userEntity);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity3, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity3, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO3);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity4, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity4, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO4);
        when(userService.checkAdminRole(any())).thenReturn(true);
        List<BookingDTO> actualBookingsForAdminDTO = bookingService.showAllBookings(testBookingData.currentUser, StatusEnum.DELETED);

        Assertions.assertNotNull(actualBookingsForAdminDTO);
        Assertions.assertEquals(expectedBookingsDTO.size(), actualBookingsForAdminDTO.size());
        Assertions.assertEquals(expectedBookingsDTO, actualBookingsForAdminDTO);
    }

    @Test
    public void searchBookingsByOfficeName() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingEntity> bookingsEntity = testBookingData.allBookingsEntity;
        List<BookingForAdminDTO> expectedBookingsDTO = testBookingData.allBookingsDTO;

        when(bookingRepository.findBookings("Name", null, null,
                null)).thenReturn(bookingsEntity);
        when(workplaceService.getById(testBookingData.workplaceId))
                .thenReturn(testBookingData.workplaceEntity);
        when(workplaceService.getById(testBookingData.workplaceId2))
                .thenReturn(testBookingData.workplaceEntity2);
        when(workplaceService.getById(testBookingData.workplaceId3))
                .thenReturn(testBookingData.workplaceEntity3);
        when(workplaceService.getById(testBookingData.workplaceId4))
                .thenReturn(testBookingData.workplaceEntity4);
        when(workspaceService.getById(testBookingData.workspaceId))
                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
        when(officeService.getById(testBookingData.officeId))
                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
        when(userService.getUserById(testBookingData.userId)).thenReturn(testBookingData.userEntity);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity2, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity2, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO2);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity3, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity3, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO3);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity4, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity4, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO4);
        List<BookingForAdminDTO> actualBookingsForAdminDTO = bookingService.searchBookings("Name",
                null, null, null);

        Assertions.assertEquals(expectedBookingsDTO, actualBookingsForAdminDTO);
    }

    @Test
    public void searchBookingsByWorkspaceName() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingEntity> bookingsEntity = testBookingData.allBookingsEntity;
        List<BookingForAdminDTO> expectedBookingsDTO = testBookingData.allBookingsDTO;

        when(bookingRepository.findBookings(null, "Name", null,
                null)).thenReturn(bookingsEntity);
        when(workplaceService.getById(testBookingData.workplaceId))
                .thenReturn(testBookingData.workplaceEntity);
        when(workplaceService.getById(testBookingData.workplaceId2))
                .thenReturn(testBookingData.workplaceEntity2);
        when(workplaceService.getById(testBookingData.workplaceId3))
                .thenReturn(testBookingData.workplaceEntity3);
        when(workplaceService.getById(testBookingData.workplaceId4))
                .thenReturn(testBookingData.workplaceEntity4);
        when(workspaceService.getById(testBookingData.workspaceId))
                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
        when(officeService.getById(testBookingData.officeId))
                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
        when(userService.getUserById(testBookingData.userId)).thenReturn(testBookingData.userEntity);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity2, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity2, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO2);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity3, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity3, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO3);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity4, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity4, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO4);
        List<BookingForAdminDTO> actualBookingsForAdminDTO = bookingService.searchBookings(
                null, "Name",null, null);

        Assertions.assertEquals(expectedBookingsDTO, actualBookingsForAdminDTO);
    }

    @Test
    public void searchBookingsByWorkplaceNumber() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingEntity> bookingsEntity = Collections.singletonList(testBookingData.bookingEntity);
        List<BookingForAdminDTO> expectedBookingsDTO = Collections.singletonList(testBookingData.bookingForAdminDTO);

        when(bookingRepository.findBookings(null, null, 1,
                null)).thenReturn(bookingsEntity);
        when(workplaceService.getById(testBookingData.workplaceId))
                .thenReturn(testBookingData.workplaceEntity);
        when(workspaceService.getById(testBookingData.workspaceId))
                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
        when(officeService.getById(testBookingData.officeId))
                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
        when(userService.getUserById(testBookingData.userId)).thenReturn(testBookingData.userEntity);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO);
        List<BookingForAdminDTO> actualBookingsForAdminDTO = bookingService.searchBookings(
                null, null,1, null);

        Assertions.assertEquals(expectedBookingsDTO, actualBookingsForAdminDTO);
    }

    @Test
    public void searchBookingsByUsername() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingEntity> bookingsEntity = testBookingData.allBookingsEntity;
        List<BookingForAdminDTO> expectedBookingsDTO = testBookingData.allBookingsDTO;

        when(bookingRepository.findBookings(null, null, null,
                "Full Name")).thenReturn(bookingsEntity);
        when(workplaceService.getById(testBookingData.workplaceId)).thenReturn(testBookingData.workplaceEntity);
        when(workplaceService.getById(testBookingData.workplaceId2)).thenReturn(testBookingData.workplaceEntity2);
        when(workplaceService.getById(testBookingData.workplaceId3)).thenReturn(testBookingData.workplaceEntity3);
        when(workplaceService.getById(testBookingData.workplaceId4)).thenReturn(testBookingData.workplaceEntity4);
        when(workspaceService.getById(testBookingData.workspaceId))
                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
        when(officeService.getById(testBookingData.officeId))
                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
        when(userService.getUserById(testBookingData.userId)).thenReturn(testBookingData.userEntity);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity2, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity2, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO2);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity3, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity3, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO3);
        when(bookingMapper.toBookingForAdminDTO(testBookingData.bookingEntity4, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity4, testBookingData.userEntity))
                .thenReturn(testBookingData.bookingForAdminDTO4);
        List<BookingForAdminDTO> actualBookingsForAdminDTO = bookingService.searchBookings(
                null, null,null, "Full Name");

        Assertions.assertEquals(expectedBookingsDTO, actualBookingsForAdminDTO);
    }

    @Test
    public void showCurrentBooking() {
        TestBookingData testBookingData = new TestBookingData();
        BookingForUserDTO expectedBookingForUserDTO = testBookingData.bookingForUserDTO;

        when(workplaceService.getById(testBookingData.workplaceId))
                .thenReturn(testBookingData.workplaceEntity);
        when(workspaceService.getById(testBookingData.workspaceId))
                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
        when(officeService.getById(testBookingData.officeId))
                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
        when(bookingMapper.toBookingForUserDTO(testBookingData.bookingEntity, testBookingData.officeEntity,
                testBookingData.workspaceEntity, testBookingData.workplaceEntity))
                .thenReturn(expectedBookingForUserDTO);
        when(bookingRepository.findCurrentBooking(
                any(UUID.class), any(LocalDateTime.class)))
                .thenReturn(testBookingData.bookingEntity);
        BookingDTO actualBookingDTO = bookingService.showCurrentBooking(testBookingData.currentUser);

        Assertions.assertEquals(expectedBookingForUserDTO, actualBookingDTO);
    }

    @Test
    public void showCurrentBookingNotFound() {
        TestBookingData testBookingData = new TestBookingData();
        when(bookingRepository.findCurrentBooking(
                any(UUID.class), any(LocalDateTime.class)))
                .thenReturn(null);
        BookingDTO actualBookingDTO = bookingService.showCurrentBooking(testBookingData.currentUser);

        Assertions.assertEquals(null, actualBookingDTO);
    }

    @Test
    public void showConfirmedBookings() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingForUserDTO> expectedBookingsForUserDTO =
                Collections.singletonList(testBookingData.bookingForUserDTO);

        when(bookingRepository.findByUserIdAndIsConfirmedAndCancellationComment(testBookingData.currentUser.getId(), true, null))
                .thenReturn(Collections.singletonList(testBookingData.bookingEntity));
        when(workplaceService.getById(testBookingData.workplaceId))
                .thenReturn(testBookingData.workplaceEntity);
        when(workspaceService.getById(testBookingData.workspaceId))
                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
        when(officeService.getById(testBookingData.officeId))
                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
        when(bookingMapper.toBookingForUserDTO(testBookingData.bookingEntity,
                testBookingData.officeEntity, testBookingData.workspaceEntity, testBookingData.workplaceEntity))
                .thenReturn(testBookingData.bookingForUserDTO);
        List<BookingDTO> actualBookingsDTO =
                bookingService.showBookingsByConfirmed(testBookingData.currentUser, true);

        Assertions.assertEquals(expectedBookingsForUserDTO, actualBookingsDTO);
    }

    @Test
    public void showConfirmedBookingsNotFound() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingForUserDTO> expectedBookingsForUserDTO = new ArrayList<>(0);

        when(bookingRepository.findByUserIdAndIsConfirmedAndCancellationComment(any(UUID.class), eq(true), eq(null)))
                .thenReturn(Collections.emptyList());

        List<BookingDTO> actualBookingsForUserDTO =
                bookingService.showBookingsByConfirmed(testBookingData.currentUser, true);

        Assertions.assertEquals(expectedBookingsForUserDTO, actualBookingsForUserDTO);
    }

    @Test
    public void showUnconfirmedBookings() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingForUserDTO> expectedBookingsForUserDTO =
                Collections.singletonList(testBookingData.bookingForUserDTO);
        when(bookingRepository.findByUserIdAndIsConfirmedAndCancellationComment(testBookingData.currentUser.getId(), false, null))
                .thenReturn(Collections.singletonList(testBookingData.unconfirmedBookingEntity));
        when(workplaceService.getById(testBookingData.workplaceId))
                .thenReturn(testBookingData.workplaceEntity);
        when(workspaceService.getById(testBookingData.workspaceId))
                .thenReturn(Optional.ofNullable(testBookingData.workspaceEntity));
        when(officeService.getById(testBookingData.officeId))
                .thenReturn(Optional.ofNullable(testBookingData.officeEntity));
        when(bookingMapper.toBookingForUserDTO(testBookingData.bookingEntity,
                testBookingData.officeEntity, testBookingData.workspaceEntity, testBookingData.workplaceEntity))
                .thenReturn(testBookingData.bookingForUserDTO);
        List<BookingDTO> actualBookingsForUserDTO =
                bookingService.showBookingsByConfirmed(testBookingData.currentUser, false);

        Assertions.assertEquals(expectedBookingsForUserDTO, actualBookingsForUserDTO);
    }

    @Test
    public void showUnconfirmedBookingsNotFound() {
        TestBookingData testBookingData = new TestBookingData();
        List<BookingForUserDTO> expectedBookingsForUserDTO = new ArrayList<>(0);

        when(bookingRepository.findByUserIdAndIsConfirmedAndCancellationComment(any(UUID.class), eq(false), eq(null)))
                .thenReturn(Collections.emptyList());

        List<BookingDTO> actualBookingsForUserDTO =
                bookingService.showBookingsByConfirmed(testBookingData.currentUser, false);

        Assertions.assertEquals(expectedBookingsForUserDTO, actualBookingsForUserDTO);
    }

    @Test
    public void cancelBooking() {
        TestBookingData testBookingData = new TestBookingData();
        BookingEntity expectedBookingEntity = new BookingEntity(
                testBookingData.bookingId,
                testBookingData.bookingDate,
                testBookingData.bookingStart,
                testBookingData.bookingEnd,
                testBookingData.userId,
                testBookingData.workplaceId,
                false,
                LocalDateTime.of(2024, Month.JULY, 3, 13, 30, 0),
                "Reason"
        );

        when(bookingRepository.findById(testBookingData.bookingId))
                .thenReturn(Optional.ofNullable(testBookingData.bookingEntity));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(expectedBookingEntity);
        when(userService.getUserById(any())).thenReturn(testBookingData.userEntity);
        bookingService.cancelBooking(testBookingData.currentUser, testBookingData.bookingId, "Reason");
        BookingEntity actualBookingEntity = bookingRepository.findById(testBookingData.bookingId).get();

        Assertions.assertEquals(expectedBookingEntity, actualBookingEntity);
    }

    @Test
    public void cancelBookingNotFound() {
        TestBookingData testBookingData = new TestBookingData();

        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
            bookingService.cancelBooking(testBookingData.currentUser, UUID.randomUUID(), "Reason")
        );
        Assertions.assertEquals("Бронирование не найдено", thrown.getMessage());

    }

    @Test
    public void confirmBooking() {
        TestBookingData testBookingData = new TestBookingData();
        BookingEntity expectedBookingEntity = new BookingEntity(
                testBookingData.unconfirmedBookingId,
                testBookingData.bookingDate,
                testBookingData.bookingStart,
                testBookingData.bookingEnd,
                testBookingData.userId,
                testBookingData.workplaceId,
                true,
                null,
                null
        );

        when(bookingRepository.findById(testBookingData.unconfirmedBookingId))
                .thenReturn(Optional.ofNullable(testBookingData.unconfirmedBookingEntity));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(expectedBookingEntity);
        bookingService.confirmBooking(testBookingData.currentUser, testBookingData.unconfirmedBookingId);
        BookingEntity actualBookingEntity = bookingRepository.findById(testBookingData.unconfirmedBookingId).get();

        Assertions.assertEquals(expectedBookingEntity, actualBookingEntity);
    }

    @Test
    public void confirmBookingNotFound() {
        TestBookingData testBookingData = new TestBookingData();

        when(bookingRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                bookingService.confirmBooking(testBookingData.currentUser, UUID.randomUUID()));
        Assertions.assertEquals("Бронирование не найдено", thrown.getMessage());
    }

    @Test
    public void confirmBookingPastBooking() {
        TestBookingData testBookingData = new TestBookingData();

        when(bookingRepository.findById(testBookingData.bookingId))
                .thenReturn(Optional.of(testBookingData.pastBookingEntity));

        TimeOutForConfirmException thrown = Assertions.assertThrows(TimeOutForConfirmException.class, () ->
                bookingService.confirmBooking(testBookingData.currentUser, testBookingData.bookingId));
        Assertions.assertEquals("Невозможно подтвердить прошедшее бронирование", thrown.getMessage());
    }

    @Test
    public void completeBooking() {
        TestBookingData testBookingData = new TestBookingData();
        BookingEntity bookingEntity = testBookingData.completedBookingEntity;

        when(bookingRepository.findByIsConfirmedAndCancellationDate(true, null))
                .thenReturn(Collections.singletonList(bookingEntity));
        when(userService.getUserById(testBookingData.userId)).thenReturn(testBookingData.userEntity);
        doNothing().when(userService).update(any(UserEntity.class));
        bookingService.completeBooking();

        Assertions.assertNotNull(bookingEntity.getCancellationDate());
        Assertions.assertEquals(bookingEntity.getCancellationComment(), "Бронирование завершилось");
        verify(bookingRepository, times(1)).save(bookingEntity);
    }

    @Test
    public void completeBookingNotFound() {
        TestBookingData testBookingData = new TestBookingData();
        BookingEntity bookingEntity = testBookingData.bookingEntity;

        when(bookingRepository.findByIsConfirmedAndCancellationDate(true, null))
                .thenReturn(Collections.singletonList(bookingEntity));
        bookingService.completeBooking();

        Assertions.assertNull(bookingEntity.getCancellationDate());
        Assertions.assertNull(bookingEntity.getCancellationComment());
        verify(bookingRepository, times(0)).save(bookingEntity);
    }

    @Test
    public void autoCancelBooking() {
        TestBookingData testBookingData = new TestBookingData();
        BookingEntity bookingEntity = testBookingData.bookingEntityToAutoCancel;

        when(bookingRepository.findByIsConfirmedAndCancellationDate(false, null))
                .thenReturn(Collections.singletonList(bookingEntity));
        when(userService.getUserById(testBookingData.userId)).thenReturn(testBookingData.userEntity);
        doNothing().when(userService).update(any(UserEntity.class));
        bookingService.autoCancelBooking();

        Assertions.assertNotNull(bookingEntity.getCancellationDate());
        Assertions.assertEquals(bookingEntity.getCancellationComment(), "Бронирование не подтверждено в отведенное время");
        verify(bookingRepository, times(1)).save(bookingEntity);
    }

    @Test
    public void autoCancelBookingNotFound() {
        TestBookingData testBookingData = new TestBookingData();
        BookingEntity bookingEntity = testBookingData.unconfirmedBookingEntity;

        when(bookingRepository.findByIsConfirmedAndCancellationDate(false, null))
                .thenReturn(Collections.singletonList(bookingEntity));
        bookingService.autoCancelBooking();

        Assertions.assertNull(bookingEntity.getCancellationDate());
        Assertions.assertNull(bookingEntity.getCancellationComment());
        verify(bookingRepository, times(0)).save(bookingEntity);
    }
}
