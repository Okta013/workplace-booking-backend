package com.anikeeva.traineeship.workplacebooking.validatorbooking;

import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import org.springframework.stereotype.Component;

@Component
public abstract class BookingValidator {
    private BookingValidator next;

    public static BookingValidator link(BookingValidator first, BookingValidator... chain) {
        BookingValidator head = first;
        for (BookingValidator nextInChain: chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(BookingEntity bookingEntity);

    public boolean checkNext(BookingEntity bookingEntity) {
        if (next == null) {
            return true;
        }
        return next.check(bookingEntity);
    }
}