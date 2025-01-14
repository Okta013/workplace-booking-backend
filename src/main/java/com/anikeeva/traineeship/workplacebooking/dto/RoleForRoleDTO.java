package com.anikeeva.traineeship.workplacebooking.dto;

import com.anikeeva.traineeship.workplacebooking.entities.enums.EnumRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleForRoleDTO {
    private Integer id;
    private EnumRole roles;
}
