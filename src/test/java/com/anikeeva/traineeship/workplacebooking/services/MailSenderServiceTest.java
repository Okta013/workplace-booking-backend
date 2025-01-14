package com.anikeeva.traineeship.workplacebooking.services;

import com.anikeeva.traineeship.workplacebooking.config.MailSenderProperties;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import com.anikeeva.traineeship.workplacebooking.observer.UserRegistrationSubject;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MailSenderServiceTest {

    @InjectMocks
    private MailSenderService mailSenderService;

    @Mock
    private UserRegistrationSubject userRegistrationSubject;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MailSenderProperties mailSenderProperties;

    @Test
    public void init() {
        mailSenderService.init();
        verify(userRegistrationSubject, times(1)).attachObserver(any(MailSenderService.class));
    }

    @Test
    @SneakyThrows
    public void update() {
        UUID userId = UUID.randomUUID();
        UserEntity userEntity = new UserEntity(
                userId,
                "Bender Bending Rodriguez",
                "89054678950",
                "givemearod@mail.ru",
                "12345",
                false
        );
        when(mailSenderProperties.subjectLine()).thenReturn("Your password for log in to the system");
        when(mailSenderProperties.messageBody())
                .thenReturn("Hi! To log in using your email address and a one-time password: ");
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        mailSenderService.update(userEntity);
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender).send(messageCaptor.capture());
        SimpleMailMessage message = messageCaptor.getValue();
        assertEquals("givemearod@mail.ru", Objects.requireNonNull(message.getTo())[0]);
        assertEquals("Your password for log in to the system", message.getSubject());
        assertEquals("Hi! To log in using your email address and a one-time password: 12345"
                , message.getText());
    }
}