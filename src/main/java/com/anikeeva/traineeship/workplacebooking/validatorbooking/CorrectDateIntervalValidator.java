package com.anikeeva.traineeship.workplacebooking.validatorbooking;

import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.exceptions.WrongBookParametersException;
import org.springframework.stereotype.Component;

@Component
public class CorrectDateIntervalValidator extends BookingValidator {
    @Override
    public boolean check(BookingEntity bookingEntity) {
        if(bookingEntity.getBookingStart().isAfter(bookingEntity.getBookingEnd())) {
            throw new WrongBookParametersException("Дата окончания не должна быть раньше даты завершения");
        }
        return checkNext(bookingEntity);
    }
}
