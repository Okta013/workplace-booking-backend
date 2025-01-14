package com.anikeeva.traineeship.workplacebooking.config;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "email")
@Validated
public record MailSenderProperties(
        @NotEmpty String subjectLine,
        @NotEmpty String messageBody
)
{}