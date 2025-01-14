package com.anikeeva.traineeship.workplacebooking.validatorbooking;

import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.exceptions.WrongBookParametersException;
import com.anikeeva.traineeship.workplacebooking.repositories.BookingRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(force = true)
public class FreeWorkplaceValidator extends BookingValidator {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public boolean check(BookingEntity bookingEntity) {
        if (!bookingRepository.findByWorkplaceAndCancellated(bookingEntity.getWorkplaceId(), bookingEntity.getBookingStart(),
                bookingEntity.getBookingEnd(), null).isEmpty()) {
            throw new WrongBookParametersException("Рабочее место уже занято на выбранное время");
        }
        return checkNext(bookingEntity);
    }
}