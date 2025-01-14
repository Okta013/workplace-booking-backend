package com.anikeeva.traineeship.workplacebooking.config;

import com.anikeeva.traineeship.workplacebooking.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BookingScheduler {
    private final BookingService bookingService;

    @Scheduled(fixedRate = 60000)
    public void checkBookings() {
        bookingService.completeBooking();
        bookingService.autoCancelBooking();
    }
}
