package com.anikeeva.traineeship.workplacebooking.mappers;

import com.anikeeva.traineeship.workplacebooking.dto.BookingForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.BookingForUserDTO;
import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.entities.OfficeEntity;
import com.anikeeva.traineeship.workplacebooking.entities.WorkplaceEntity;
import com.anikeeva.traineeship.workplacebooking.entities.WorkspaceEntity;
import com.anikeeva.traineeship.workplacebooking.entities.enums.StatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

public class BookingMapperTest {
    BookingMapper bookingMapper = new BookingMapperImpl();

    private static class TestBookingMapperData {
        UUID bookingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID officeId = UUID.randomUUID();
        UUID workspaceId = UUID.randomUUID();
        UUID workplaceId = UUID.randomUUID();
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
        BookingForUserDTO bookingForUserDTO = new BookingForUserDTO(
                bookingId,
                "Name",
                "Name",
                (short) 1,
                1,
                bookingStart,
                bookingEnd,
                StatusEnum.ACTIVE,
                true,
                "Description"
        );
        BookingForUserDTO bookingForUserDTOWithBookingEntityIsNull = new BookingForUserDTO(
                null,
                "Name",
                "Name",
                (short) 1,
                1,
                null,
                null,
                StatusEnum.ACTIVE,
                true,
                "Description"
        );
        BookingForUserDTO bookingForUserDTOWithOfficeEntityIsNull = new BookingForUserDTO(
                bookingId,
                null,
                "Name",
                (short) 1,
                1,
                bookingStart,
                bookingEnd,
                StatusEnum.ACTIVE,
                true,
                "Description"
        );
        BookingForUserDTO bookingForUserDTOWithWorkspaceEntityIsNull = new BookingForUserDTO(
                bookingId,
                "Name",
                null,
                (short) 0,
                1,
                bookingStart,
                bookingEnd,
                StatusEnum.ACTIVE,
                true,
                "Description"
        );
        BookingForUserDTO bookingForUserDTOWithWorkplaceEntityIsNull = new BookingForUserDTO(
                bookingId,
                "Name",
                "Name",
                (short) 1,
                0,
                bookingStart,
                bookingEnd,
                StatusEnum.ACTIVE,
                true,
                null
        );
        OfficeEntity officeEntity = new OfficeEntity(
                officeId,
                "Address",
                "Name",
                false
        );
        WorkspaceEntity workspaceEntity = new  WorkspaceEntity(
                workspaceId,
                "Name" ,
                (short) 1,
                (short) 2,
                false,
                officeId
        );
        WorkplaceEntity workplaceEntity = new WorkplaceEntity(
                workplaceId,
                1,
                "Description",
                workspaceId,
                false
        );
    }

    @Test
    public void toBookingForUserDTO() {
        TestBookingMapperData testBookingMapperData = new TestBookingMapperData();
        BookingForUserDTO expectedBookingForUserDTO = testBookingMapperData.bookingForUserDTO;

        BookingForUserDTO actualBookingForUserDTO = bookingMapper.toBookingForUserDTO(
                testBookingMapperData.bookingEntity, testBookingMapperData.officeEntity,
                testBookingMapperData.workspaceEntity, testBookingMapperData.workplaceEntity
        );

        Assertions.assertEquals(expectedBookingForUserDTO, actualBookingForUserDTO);
    }

    @Test
    public void toBookingForUserDTOWithAllParametersIsNull() {
        BookingForUserDTO actualBookingForUserDTO = bookingMapper.toBookingForUserDTO(null, null,
                null, null);

        Assertions.assertNull(actualBookingForUserDTO);
    }

    @Test
    public void toBookingForUserDTOWithOfficeEntityIsNull() {
        TestBookingMapperData testBookingMapperData = new TestBookingMapperData();
        BookingForUserDTO expectedBookingForUserDTO = testBookingMapperData.bookingForUserDTOWithOfficeEntityIsNull;

        BookingForUserDTO actualBookingForUserDTO =
                bookingMapper.toBookingForUserDTO(testBookingMapperData.bookingEntity, null,
                        testBookingMapperData.workspaceEntity, testBookingMapperData.workplaceEntity);

        Assertions.assertEquals(expectedBookingForUserDTO, actualBookingForUserDTO);
    }

    @Test
    public void toBookingForUserDTOWithWorkspaceEntityIsNull() {
        TestBookingMapperData testBookingMapperData = new TestBookingMapperData();
        BookingForUserDTO expectedBookingForUserDTO = testBookingMapperData.bookingForUserDTOWithWorkspaceEntityIsNull;

        BookingForUserDTO actualBookingForUserDTO =
                bookingMapper.toBookingForUserDTO(testBookingMapperData.bookingEntity,
                        testBookingMapperData.officeEntity, null, testBookingMapperData.workplaceEntity);

        Assertions.assertEquals(expectedBookingForUserDTO, actualBookingForUserDTO);
    }

    @Test
    public void toBookingForUserDTOWithWorkplaceEntityIsNull() {
        TestBookingMapperData testBookingMapperData = new TestBookingMapperData();
        BookingForUserDTO expectedBookingForUserDTO = testBookingMapperData.bookingForUserDTOWithWorkplaceEntityIsNull;

        BookingForUserDTO actualBookingForUserDTO =
                bookingMapper.toBookingForUserDTO(testBookingMapperData.bookingEntity,
                        testBookingMapperData.officeEntity, testBookingMapperData.workspaceEntity, null);

        Assertions.assertEquals(expectedBookingForUserDTO, actualBookingForUserDTO);
    }

    @Test
    public void toBookingEntity() {
        TestBookingMapperData testBookingMapperData = new TestBookingMapperData();
        BookingEntity expectedBookingEntity = testBookingMapperData.bookingEntity;
        BookingForUserDTO bookingForUserDTO = testBookingMapperData.bookingForUserDTO;

        BookingEntity actualBookingEntity = bookingMapper.toBookingEntity(bookingForUserDTO);
        actualBookingEntity.setBookingDate(testBookingMapperData.bookingDate);

        Assertions.assertEquals(expectedBookingEntity, actualBookingEntity);
    }

    @Test
    public void toBookingEntityWithNull() {
        BookingForAdminDTO bookingForAdminDTO = null;
        BookingEntity actualBookingEntity = bookingMapper.toBookingEntity(bookingForAdminDTO);

        Assertions.assertNull(actualBookingEntity);
    }
}
