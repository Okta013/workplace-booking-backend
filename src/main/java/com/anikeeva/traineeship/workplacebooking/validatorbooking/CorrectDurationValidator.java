package com.anikeeva.traineeship.workplacebooking.validatorbooking;

import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.exceptions.WrongBookParametersException;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CorrectDurationValidator extends BookingValidator {
    private final int MIN_DURATION = 10;
    @Override
    public boolean check(BookingEntity bookingEntity) {
        int bookingMinutes = Math.toIntExact(Duration.between(bookingEntity.getBookingStart(),
                bookingEntity.getBookingEnd()).toMinutes());
        if(bookingMinutes < MIN_DURATION) {
            throw new WrongBookParametersException("Бронирование можно создать минимум на 10 минут");
        }
        return  checkNext(bookingEntity);
    }
}
