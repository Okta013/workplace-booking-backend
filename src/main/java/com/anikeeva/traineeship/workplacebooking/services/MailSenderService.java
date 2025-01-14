package com.anikeeva.traineeship.workplacebooking.services;

import com.anikeeva.traineeship.workplacebooking.config.MailSenderProperties;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import com.anikeeva.traineeship.workplacebooking.observer.UserRegistrationObserver;
import com.anikeeva.traineeship.workplacebooking.observer.UserRegistrationSubject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "email.enable", havingValue = "true")
public class MailSenderService implements UserRegistrationObserver {
    private final JavaMailSender javaMailSender;
    private final UserRegistrationSubject userRegistrationSubject;
    private final MailSenderProperties mailSenderProperties;

    @PostConstruct
    public void init() {
        userRegistrationSubject.attachObserver(this);
    }

    @Override
    public void update(UserEntity userEntity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEntity.getEmail());
        message.setSubject(mailSenderProperties.subjectLine());
        message.setText(mailSenderProperties.messageBody() + userEntity.getPassword());
        javaMailSender.send(message);
    }
}