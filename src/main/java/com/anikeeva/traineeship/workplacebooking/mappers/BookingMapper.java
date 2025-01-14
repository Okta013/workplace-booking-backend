package com.anikeeva.traineeship.workplacebooking.mappers;

import com.anikeeva.traineeship.workplacebooking.dto.BookingForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.BookingForUserDTO;
import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.entities.OfficeEntity;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import com.anikeeva.traineeship.workplacebooking.entities.WorkplaceEntity;
import com.anikeeva.traineeship.workplacebooking.entities.WorkspaceEntity;
import com.anikeeva.traineeship.workplacebooking.entities.enums.StatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "id", source = "bookingEntity.id")
    @Mapping(target = "officeName", source = "officeEntity.name")
    @Mapping(target = "workspaceName", source = "workspaceEntity.name")
    @Mapping(target = "floorNumber", source = "workspaceEntity.floorNumber")
    @Mapping(target = "workplaceNumber", source = "workplaceEntity.number")
    @Mapping(target = "status", expression = "java(getStatus(bookingEntity))")
    @Mapping(target = "isConfirmed", source = "bookingEntity.confirmed")
    @Mapping(target = "equipment", source = "workplaceEntity.description")
    BookingForUserDTO toBookingForUserDTO(BookingEntity bookingEntity, OfficeEntity officeEntity,
                                          WorkspaceEntity workspaceEntity, WorkplaceEntity workplaceEntity);

    BookingEntity toBookingEntity(BookingForUserDTO bookingForUserDTO);

    @Mapping(target = "id", source = "bookingEntity.id")
    @Mapping(target = "officeName", source = "officeEntity.name")
    @Mapping(target = "workspaceName", source = "workspaceEntity.name")
    @Mapping(target = "floorNumber", source = "workspaceEntity.floorNumber")
    @Mapping(target = "workplaceNumber", source = "workplaceEntity.number")
    @Mapping(target = "equipment", source = "workplaceEntity.description")
    @Mapping(target = "status", expression = "java(getStatus(bookingEntity))")
    @Mapping(target = "isConfirmed", source = "bookingEntity.confirmed")
    @Mapping(target = "user", source = "userEntity.fullName")
    BookingForAdminDTO toBookingForAdminDTO(BookingEntity bookingEntity, OfficeEntity officeEntity,
                                            WorkspaceEntity workspaceEntity, WorkplaceEntity workplaceEntity,
                                            UserEntity userEntity);

    default StatusEnum getStatus(BookingEntity bookingEntity) {
        if(Objects.isNull(bookingEntity.getCancellationDate())) {
            return StatusEnum.ACTIVE;
        }
        return StatusEnum.DELETED;
    }

    BookingEntity toBookingEntity(BookingForAdminDTO bookingForAdminDTO);
}

