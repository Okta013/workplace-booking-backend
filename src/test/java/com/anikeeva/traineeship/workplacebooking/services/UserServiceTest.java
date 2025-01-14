package com.anikeeva.traineeship.workplacebooking.services;

import com.anikeeva.traineeship.workplacebooking.dto.UserDTO;
import com.anikeeva.traineeship.workplacebooking.dto.UserForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.UserForUserDTO;
import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.entities.Role;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import com.anikeeva.traineeship.workplacebooking.entities.enums.EnumRole;
import com.anikeeva.traineeship.workplacebooking.exceptions.ResourceNotFoundException;
import com.anikeeva.traineeship.workplacebooking.mappers.UserMapper;
import com.anikeeva.traineeship.workplacebooking.observer.UserRegistrationSubject;
import com.anikeeva.traineeship.workplacebooking.repositories.RoleRepository;
import com.anikeeva.traineeship.workplacebooking.repositories.UserRepository;
import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    private UserRegistrationSubject userRegistrationSubject;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JavaMailSender javaMailSender;

    private UserEntity user;

    private static class TestData {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        UUID userId3 = UUID.randomUUID();
        UUID userId4 = UUID.randomUUID();
        UUID bookingId1 = UUID.randomUUID();
        UUID bookingId2 = UUID.randomUUID();
        UUID workplaceId1 = UUID.randomUUID();
        UUID workplaceId2 = UUID.randomUUID();
        Role userRole = new Role(1, EnumRole.ROLE_USER);
        UserEntity userEntity1 = new UserEntity(
                userId1,
                "Smith Steve Anita",
                "89276547890",
                "overlord@gmail.com",
                "12345",
                false
        );
        UserEntity userEntity2 = new UserEntity(
                userId2,
                "Bender Bending Rodriguez",
                "89054678950",
                "givemearod@mail.ru",
                "12345",
                false
        );
        UserEntity userEntity3 = new UserEntity(
                userId3,
                "Eric Theodore Cartman",
                "89867584767",
                "imnotfat@gmail.com",
                "12345",
                true
        );
        UserEntity userEntity4 = new UserEntity(
                userId4,
                "Harleen Frances Quinzel",
                "89006660000",
                "pieforpudding@yandex.ru",
                "12345",
                true
        );
        UserForAdminDTO userForAdminDTO1 = new UserForAdminDTO(
                userId1,
                "Smith Steve Anita",
                "89054678950",
                "overlord@gmail.com",
                false,
                false
        );
        UserForAdminDTO userForAdminDTO2 = new UserForAdminDTO(
                userId2,
                "Bender Bending Rodriguez",
                "89054678950",
                "givemearod@mail.ru",
                false,
                false
        );
        UserForAdminDTO userForAdminDTO3 = new UserForAdminDTO(
                userId3,
                "Eric Theodore Cartman",
                "89867584767",
                "imnotfat@gmail.com",
                true,
                false
        );
        UserForAdminDTO userForAdminDTO4 = new UserForAdminDTO(
                userId4,
                "Harleen Frances Quinzel",
                "89006660000",
                "pieforpudding@yandex.ru",
                true,
                false
        );
        BookingEntity bookingEntity1 = new BookingEntity(
                bookingId1,
                LocalDateTime.of(2024, Month.JULY, 3, 11, 30, 0),
                LocalDateTime.of(2024, Month.JULY, 4, 9, 0, 0),
                LocalDateTime.of(2024, Month.JULY, 4, 18, 0, 0),
                userId2,
                workplaceId1,
                true,
                null,
                null
        );
        BookingEntity bookingEntity2 = new BookingEntity(
                bookingId2,
                LocalDateTime.of(2024, Month.AUGUST, 7, 11, 30, 0),
                LocalDateTime.of(2024, Month.AUGUST, 8, 9, 0, 0),
                LocalDateTime.of(2024, Month.AUGUST, 8, 18, 0, 0),
                userId2,
                workplaceId2,
                true,
                null,
                null
        );
        UserDetailsImpl currentUser = new UserDetailsImpl(
                userId1,
                "Full Name",
                "89009009090",
                "email@gmail.com",
                "password",
                false
        );
        List<UserEntity> allUsers = Arrays.asList(userEntity1, userEntity2, userEntity3, userEntity4);
        List<UserForAdminDTO> allUsersDTO = Arrays.asList(userForAdminDTO1, userForAdminDTO2, userForAdminDTO3,
                userForAdminDTO4);
        List<UserEntity> usersWithFalseStatus = Arrays.asList(userEntity1, userEntity2);
        List<UserForAdminDTO> usersWithFalseStatusDTO = Arrays.asList(userForAdminDTO1, userForAdminDTO2);
        List<UserEntity> usersWithTrueStatus = Arrays.asList(userEntity3, userEntity4);
        List<UserForAdminDTO> usersWithTrueStatusDTO = Arrays.asList(userForAdminDTO3, userForAdminDTO4);
        List<BookingEntity> availableBookings = Arrays.asList(bookingEntity1, bookingEntity2);
    }

    @Test
    public void showUser() {
        TestData testData = new TestData();
        UserForUserDTO expectedUserForUserDTO = new UserForUserDTO(
                "Smith Steve Anita",
                "+79276547890",
                "overlord@gmail.com",
                false
        );

        when(userMapper.toUserForUserDTO(testData.userEntity1)).thenReturn(expectedUserForUserDTO);
        when(userRepository.findById(testData.userId1)).thenReturn(Optional.of(testData.userEntity1));
        UserDTO actualUserForUserDTO = userService.showUser(testData.currentUser);

        Assertions.assertNotNull(actualUserForUserDTO);
        assertEquals(expectedUserForUserDTO, actualUserForUserDTO);
    }

    @Test
    public void showUserNotFound() {
        TestData testData = new TestData();
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            UUID userId = UUID.randomUUID();

            when(userRepository.findById(any())).thenReturn(Optional.empty());
            userService.showUser(testData.currentUser);
        });
        assertEquals("Пользователь не найден", thrown.getMessage());
    }

    @Test
    public void showAllUsers() {
        TestData testData = new TestData();
        List<UserEntity> expectedUsers = testData.allUsers;
        List<UserForAdminDTO> expectedUsersDTO = testData.allUsersDTO;

        when(userMapper.toUserForAdminDTO(testData.userEntity1)).thenReturn(testData.userForAdminDTO1);
        when(userMapper.toUserForAdminDTO(testData.userEntity2)).thenReturn(testData.userForAdminDTO2);
        when(userMapper.toUserForAdminDTO(testData.userEntity3)).thenReturn(testData.userForAdminDTO3);
        when(userMapper.toUserForAdminDTO(testData.userEntity4)).thenReturn(testData.userForAdminDTO4);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserForAdminDTO> actualUsersDTO = userService.showAllUsers(null);
        Assertions.assertNotNull(actualUsersDTO);
        assertEquals(expectedUsersDTO, actualUsersDTO);
    }

    @Test
    public void showAllUsersWithFalseStatus() {
        TestData testData = new TestData();
        List<UserEntity> expectedUsers = testData.usersWithFalseStatus;
        List<UserForAdminDTO> expectedUsersDTO = testData.usersWithFalseStatusDTO;

        when(userMapper.toUserForAdminDTO(testData.userEntity1)).thenReturn(testData.userForAdminDTO1);
        when(userMapper.toUserForAdminDTO(testData.userEntity2)).thenReturn(testData.userForAdminDTO2);
        when(userRepository.findByIsDeleted(false)).thenReturn(expectedUsers);

        List<UserForAdminDTO> actualUsersDTO = userService.showAllUsers(false);
        Assertions.assertNotNull(actualUsersDTO);
        assertEquals(expectedUsersDTO, actualUsersDTO);
    }

    @Test
    public void showAllUsersWithTrueStatus() {
        TestData testData = new TestData();
        List<UserEntity> expectedUsers = testData.usersWithTrueStatus;
        List<UserForAdminDTO> expectedUsersDTO = testData.usersWithTrueStatusDTO;

        when(userMapper.toUserForAdminDTO(testData.userEntity3)).thenReturn(testData.userForAdminDTO3);
        when(userMapper.toUserForAdminDTO(testData.userEntity4)).thenReturn(testData.userForAdminDTO4);
        when(userRepository.findByIsDeleted(true)).thenReturn(expectedUsers);

        List<UserForAdminDTO> actualUsersDTO = userService.showAllUsers(true);
        Assertions.assertNotNull(actualUsersDTO);
        assertEquals(expectedUsersDTO, actualUsersDTO);
    }

    @Test
    public void showAllUsersNotFound() {
        List<UserEntity> expectedUsers = new ArrayList<>(0);
        List<UserForAdminDTO> expectedUsersDTO = new ArrayList<>(0);

        when(userRepository.findAll()).thenReturn(expectedUsers);
        List<UserForAdminDTO> actualUsersDTO = userService.showAllUsers(null);

        assertEquals(expectedUsersDTO, actualUsersDTO);
    }

    @Test
    public void searchUsersByFullName() {
        TestData testData = new TestData();
        List<UserEntity> expectedUsers = Arrays.asList(testData.userEntity3);
        List<UserForAdminDTO> expectedUsersDTO = Arrays.asList(testData.userForAdminDTO3);

        when(userMapper.toUserForAdminDTO(testData.userEntity3)).thenReturn(testData.userForAdminDTO3);
        when(userRepository.findByFullNameOrPhoneNumberOrEmail("Eric Theodore Cartman",
                null, null)).thenReturn(expectedUsers);
        List<UserForAdminDTO> actualUserDTO =
                userService.searchUsers("Eric Theodore Cartman", null, null);

        Assertions.assertNotNull(actualUserDTO);
        assertEquals(expectedUsersDTO, actualUserDTO);
    }

    @Test
    public void searchUsersByEmail() {
        TestData testData = new TestData();
        List<UserEntity> expectedUsers = Arrays.asList(testData.userEntity3);
        List<UserForAdminDTO> expectedUsersDTO = Arrays.asList(testData.userForAdminDTO3);

        when(userMapper.toUserForAdminDTO(testData.userEntity3)).thenReturn(testData.userForAdminDTO3);
        when(userRepository.findByFullNameOrPhoneNumberOrEmail(null,
                null, "imnotfat@gmail.com")).thenReturn(expectedUsers);
        List<UserForAdminDTO> actualUsersDTO =
                userService.searchUsers(null, null, "imnotfat@gmail.com");

        Assertions.assertNotNull(actualUsersDTO);
        assertEquals(expectedUsersDTO, actualUsersDTO);
    }

    @Test
    public void searchUsersByPhoneNumber() {
        TestData testData = new TestData();
        List<UserEntity> expectedUsers = Arrays.asList(testData.userEntity3);
        List<UserForAdminDTO> expectedUsersDTO = Arrays.asList(testData.userForAdminDTO3);

        when(userMapper.toUserForAdminDTO(testData.userEntity3)).thenReturn(testData.userForAdminDTO3);
        when(userRepository.findByFullNameOrPhoneNumberOrEmail(null,
                "+79867584767", null)).thenReturn(expectedUsers);
        List<UserForAdminDTO> actualUsersDTO =
                userService.searchUsers(null, "+79867584767", null);

        Assertions.assertNotNull(actualUsersDTO);
        assertEquals(expectedUsersDTO, actualUsersDTO);
    }

    @Test
    public void searchUsersByFullNameNotFound() {
        List<UserEntity> expectedUsers = new ArrayList<>(0);
        List<UserForAdminDTO> expectedUsersDTO = new ArrayList<>(0);

        when(userRepository.findByFullNameOrPhoneNumberOrEmail("Eric Theodore Cartman",
                null, null)).thenReturn(expectedUsers);
        List<UserForAdminDTO> actualUsersDTO =
                userService.searchUsers("Eric Theodore Cartman", null, null);

        assertEquals(expectedUsersDTO, actualUsersDTO);
    }

    @Test
    public void searchUsersByEmailNotFound() {
        List<UserEntity> expectedUsers = new ArrayList<>(0);
        List<UserForAdminDTO> expectedUsersDTO = new ArrayList<>(0);

        when(userRepository.findByFullNameOrPhoneNumberOrEmail(null,
                null, "imnotfat@gmail.com")).thenReturn(expectedUsers);
        List<UserForAdminDTO> actualUsersDTO =
                userService.searchUsers(null, null, "imnotfat@gmail.com");

        assertEquals(expectedUsersDTO, actualUsersDTO);
    }

    @Test
    public void searchUsersByPhoneNumberNotFound() {
        List<UserEntity> expectedUsers = new ArrayList<>(0);
        List<UserForAdminDTO> expectedUsersDTO = new ArrayList<>(0);

        when(userRepository.findByFullNameOrPhoneNumberOrEmail(null,
                "+79867584767", null)).thenReturn(expectedUsers);
        List<UserForAdminDTO> actualUsersDTO =
                userService.searchUsers(null, "+79867584767", null);

        assertEquals(expectedUsersDTO, actualUsersDTO);
    }

    @Test
    public void searchUsersWithoutParameters() {
        TestData testData = new TestData();
        List<UserEntity> expectedUsers = Arrays.asList(testData.userEntity1, testData.userEntity2);
        List<UserForAdminDTO> expectedUsersDTO = Arrays.asList(testData.userForAdminDTO1,
                testData.userForAdminDTO2);

        when(userRepository.findAll()).thenReturn(expectedUsers);
        when(userMapper.toUserForAdminDTO(testData.userEntity1)).thenReturn(testData.userForAdminDTO1);
        when(userMapper.toUserForAdminDTO(testData.userEntity2)).thenReturn(testData.userForAdminDTO2);
        List<UserForAdminDTO> actualUsersDTO =
                userService.searchUsers(null, null, null);

        Assertions.assertNotNull(actualUsersDTO);
        assertEquals(expectedUsersDTO, actualUsersDTO);
    }

    @Test
    public void searchUsersWithAllParameters() {
        TestData testData = new TestData();
        List<UserEntity> expectedUsers = Arrays.asList(testData.userEntity1,
                testData.userEntity2, testData.userEntity3);
        List<UserForAdminDTO> expectedUsersDTO = Arrays.asList(testData.userForAdminDTO1,
                testData.userForAdminDTO2, testData.userForAdminDTO3);

        when(userMapper.toUserForAdminDTO(testData.userEntity1)).thenReturn(testData.userForAdminDTO1);
        when(userMapper.toUserForAdminDTO(testData.userEntity2)).thenReturn(testData.userForAdminDTO2);
        when(userMapper.toUserForAdminDTO(testData.userEntity3)).thenReturn(testData.userForAdminDTO3);
        when(userRepository.findByFullNameOrPhoneNumberOrEmail("Smith Steve Anita",
                "89054678950", "imnotfat@gmail.com")).thenReturn(expectedUsers);

        List<UserForAdminDTO> actualUsersDTO = userService.searchUsers("Smith Steve Anita",
                "89054678950", "imnotfat@gmail.com");

        Assertions.assertNotNull(actualUsersDTO);
        assertEquals(expectedUsersDTO, actualUsersDTO);

    }

    @Test
    public void createUser() {
        TestData testData = new TestData();
        UserForAdminDTO expectedUserForAdminDTO = testData.userForAdminDTO1;

        when(userMapper.toUserEntity(expectedUserForAdminDTO)).thenReturn(testData.userEntity1);
        when(passwordEncoder.encode(any())).thenReturn(null);
        when(roleRepository.findByRoles(EnumRole.ROLE_USER)).thenReturn(Optional.of(testData.userRole));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testData.userEntity1);
        doNothing().when(userRegistrationSubject).notifyAllObservers(any(UserEntity.class));
        when(userMapper.toUserForAdminDTO(any(UserEntity.class))).thenReturn(expectedUserForAdminDTO);
        UserForAdminDTO actualUserForAdminDTO = userService.createUser(expectedUserForAdminDTO);

        assertEquals(expectedUserForAdminDTO, actualUserForAdminDTO);
        verify(userMapper, times(1)).toUserEntity(expectedUserForAdminDTO);
        verify(passwordEncoder, times(1)).encode(any());
        verify(roleRepository, times(1)).findByRoles(EnumRole.ROLE_USER);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userMapper, times(1)).toUserForAdminDTO(any(UserEntity.class));
        verify(userRegistrationSubject, times(1)).notifyAllObservers(any(UserEntity.class));
    }

    @Test
    public void updateUser() {
        TestData testData = new TestData();
        UserForAdminDTO expectedUserForAdminDTO = testData.userForAdminDTO1;

        when(userRepository.findById(testData.userId1)).thenReturn(Optional.of(testData.userEntity1));
        when(userMapper.toUserForAdminDTO(testData.userEntity1)).thenReturn(expectedUserForAdminDTO);
        when(userRepository.save(testData.userEntity1)).thenReturn(testData.userEntity1);
        UserForAdminDTO actualUserForAdminDTO = userService.updateUser(testData.userId1, expectedUserForAdminDTO);

        assertEquals(expectedUserForAdminDTO, actualUserForAdminDTO);
    }

    @Test
    public void deleteUser() {
        TestData testData = new TestData();
        UserEntity expectedUserEntity = testData.userEntity2;
        expectedUserEntity.setAvailableBookings(testData.availableBookings);
        BookingEntity actualBookingEntity1 = new BookingEntity(
                testData.bookingId1,
                LocalDateTime.of(2024, Month.JULY, 3, 11, 30, 0),
                LocalDateTime.of(2024, Month.JULY, 4, 9, 0, 0),
                LocalDateTime.of(2024, Month.JULY, 4, 18, 0, 0),
                testData.userId2,
                testData.workplaceId1,
                false,
                null,
                null
        );
        BookingEntity actualBookingEntity2 = new BookingEntity(
                testData.bookingId2,
                LocalDateTime.of(2024, Month.AUGUST, 7, 11, 30, 0),
                LocalDateTime.of(2024, Month.AUGUST, 8, 9, 0, 0),
                LocalDateTime.of(2024, Month.AUGUST, 8, 18, 0, 0),
                testData.userId2,
                testData.workplaceId2,
                false,
                null,
                null
        );

        when(userRepository.findById(testData.userId2)).thenReturn(Optional.of(testData.userEntity2));
        when(userRepository.save(testData.userEntity2)).thenReturn(testData.userEntity2);
        userService.deleteUser(testData.userId2);
        UserEntity actualUserEntity = userRepository.findById(testData.userId2).get();

        assertEquals(expectedUserEntity, actualUserEntity);
        Assertions.assertEquals(expectedUserEntity, actualUserEntity);
        Assertions.assertEquals(testData.bookingEntity1, actualBookingEntity1);
        Assertions.assertEquals(testData.bookingEntity2, actualBookingEntity2);
    }

    @Test
    public void deleteUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        userService.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPasswordNeedsChange(true);
        user.setPassword("encodedOldPassword");  // Example of encoded password
    }

//    @Test
//    void changePassword_ShouldChangePassword_WhenOldPasswordIsCorrect() {
//        ChangePasswordRequest request = new ChangePasswordRequest("oldPassword", "newPassword");
//
//        when(userRepository.findByEmail()).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(eq("oldPassword"), eq(user.getPassword()))).thenReturn(true);
//        when(passwordEncoder.encode(eq("newPassword"))).thenReturn("encodedNewPassword");
//
//        userService.changePassword(request);
//
//        assertEquals("encodedNewPassword", user.getPassword());
//        assertFalse(user.isPasswordNeedsChange());
//        verify(userRepository, times(1)).save(user);
//    }
//
//    @Test
//    void changePassword_ShouldThrowException_WhenUserNotFound() {
//        ChangePasswordRequest request = new ChangePasswordRequest("notfound@example.com", "oldPassword", "newPassword");
//        when(userRepository.findByEmail(eq(request.email()))).thenReturn(Optional.empty());
//
//        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
//            userService.changePassword(request);
//        });
//
//        assertEquals("User not foundnotfound@example.com", exception.getMessage());
//        verify(userRepository, never()).save(any(UserEntity.class));
//    }
//
//    @Test
//    void changePassword_ShouldThrowException_WhenOldPasswordIsIncorrect() {
//        ChangePasswordRequest request = new ChangePasswordRequest("test@example.com", "wrongOldPassword", "newPassword");
//
//        when(userRepository.findByEmail(eq(request.email()))).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(eq("wrongOldPassword"), eq(user.getPassword()))).thenReturn(false);
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.changePassword(request);
//        });
//
//        assertEquals("Old password is incorrect.", exception.getMessage());
//        verify(userRepository, never()).save(any(UserEntity.class));
//    }
}