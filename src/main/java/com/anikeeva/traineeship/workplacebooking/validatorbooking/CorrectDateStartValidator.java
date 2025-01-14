package com.anikeeva.traineeship.workplacebooking.validatorbooking;

import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.exceptions.WrongBookParametersException;
import org.springframework.stereotype.Component;

@Component
public class CorrectDateStartValidator extends BookingValidator {
    @Override
    public boolean check(BookingEntity bookingEntity) {
        if(bookingEntity.getBookingStart().isBefore(bookingEntity.getBookingDate())) {
            throw new WrongBookParametersException("Дата начала бронирования не должна быть раньше текущей даты");
        }
        return checkNext(bookingEntity);
    }
}
