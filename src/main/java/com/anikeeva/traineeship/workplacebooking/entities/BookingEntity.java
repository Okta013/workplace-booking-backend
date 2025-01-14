package com.anikeeva.traineeship.workplacebooking.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id")
    private UUID id;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(name = "booking_start")
    private LocalDateTime bookingStart;

    @Column(name = "booking_end")
    private LocalDateTime bookingEnd;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "workplace_id")
    private UUID workplaceId;

    @Column(name = "is_confirmed")
    private boolean isConfirmed;

    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;

    @Column(name = "cancellation_comment")
    private String cancellationComment;

    @Override
    public String toString() {
        return "Booking of " + bookingDate + " for " + bookingStart + " - " + bookingEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return bookingDate.equals(that.bookingDate);
    }

    @Override
    public int hashCode() {
        return bookingDate.hashCode();
    }
}
