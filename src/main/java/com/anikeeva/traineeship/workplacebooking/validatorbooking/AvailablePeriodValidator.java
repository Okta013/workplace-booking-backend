package com.anikeeva.traineeship.workplacebooking.validatorbooking;

import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.exceptions.WrongBookParametersException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class AvailablePeriodValidator extends BookingValidator {
    private final int MAX_GAP = 30;

    @Override
    public boolean check(BookingEntity bookingEntity) {
        if(Duration.between(LocalDateTime.now(), bookingEntity.getBookingStart()).toDays() > MAX_GAP) {
            throw new WrongBookParametersException("Создать бронирование можно максимум за 30 дней дней до нужной даты");
        }
        return checkNext(bookingEntity);
    }
}
