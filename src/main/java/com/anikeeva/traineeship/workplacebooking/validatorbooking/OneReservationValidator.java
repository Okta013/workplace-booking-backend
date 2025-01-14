package com.anikeeva.traineeship.workplacebooking.validatorbooking;

import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.exceptions.WrongBookParametersException;
import com.anikeeva.traineeship.workplacebooking.repositories.BookingRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor(force = true)
public class OneReservationValidator extends BookingValidator {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public boolean check(BookingEntity bookingEntity) {
        if(Objects.nonNull(bookingRepository.findByDateAndCancelled(bookingEntity.getBookingStart().toLocalDate(),
                null))) {
            throw new WrongBookParametersException("У вас уже есть активное бронирование на выбранный день");
        }
        return checkNext(bookingEntity);
    }
}