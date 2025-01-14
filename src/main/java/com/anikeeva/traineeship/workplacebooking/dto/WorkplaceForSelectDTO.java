package com.anikeeva.traineeship.workplacebooking.dto;

import java.util.UUID;

public record WorkplaceForSelectDTO (
        UUID id,
        Integer number,
        String description,
        UUID workspaceId,
        String workspaceName,
        UUID officeId,
        String officeName
)
{}
