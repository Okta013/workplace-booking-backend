package com.anikeeva.traineeship.workplacebooking.validatorbooking;

import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import com.anikeeva.traineeship.workplacebooking.exceptions.WrongBookParametersException;
import com.anikeeva.traineeship.workplacebooking.services.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@NoArgsConstructor(force = true)
public class EnoughMinutesValidator extends BookingValidator {
    @Autowired
    private UserService userService;

    @Override
    public boolean check(BookingEntity bookingEntity) {
        int bookingMinutes = Math.toIntExact(Duration.between(bookingEntity.getBookingStart(),
                bookingEntity.getBookingEnd()).toMinutes());
        UserEntity userEntity = userService.getUserById(bookingEntity.getUserId());
        if (bookingMinutes > userEntity.getAvailableMinutes()) {
            throw new WrongBookParametersException("У вас недостаточно минут для создания бронирования");
        }
        return checkNext(bookingEntity);
    }
}