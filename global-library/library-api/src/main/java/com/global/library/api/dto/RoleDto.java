package com.global.library.api.dto;

import com.global.library.api.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RoleDto {

    private long id;
    private String name;

    public String mapName () {
        String name = getName();
        for (RoleName value : RoleName.values()) {
            if (value.getId() == getId())
                name = value.getName();
        }
        return name;
    }
}

