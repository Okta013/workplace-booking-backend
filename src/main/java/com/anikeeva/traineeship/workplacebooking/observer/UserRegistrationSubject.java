package com.anikeeva.traineeship.workplacebooking.observer;

import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRegistrationSubject {
    private final List<UserRegistrationObserver> observers = new ArrayList<>();

    public void attachObserver(UserRegistrationObserver observer) {
        observers.add(observer);
    }

    public void notifyAllObservers(UserEntity userEntity) {
        for (UserRegistrationObserver observer : observers) {
            observer.update(userEntity);
        }
    }
}
