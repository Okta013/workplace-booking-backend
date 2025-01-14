package com.anikeeva.traineeship.workplacebooking.validatorbooking;

import com.anikeeva.traineeship.workplacebooking.entities.BookingEntity;
import com.anikeeva.traineeship.workplacebooking.entities.WorkplaceEntity;
import com.anikeeva.traineeship.workplacebooking.exceptions.WrongBookParametersException;
import com.anikeeva.traineeship.workplacebooking.repositories.WorkplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistingWorkplaceValidator extends BookingValidator {
    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Override
    public boolean check(BookingEntity bookingEntity) {
        WorkplaceEntity workplace = workplaceRepository.getById(bookingEntity.getWorkplaceId());
        if (workplace.getIsDeleted()) {
            throw new WrongBookParametersException("Рабочее место было удалено");
        }
        return checkNext(bookingEntity);
    }
}
