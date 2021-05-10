package com.global.library.api.mappers;

import com.global.library.api.dto.RoleDto;
import com.global.library.entity.Role;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class RoleMapper {

    public Role mapRole(RoleDto source) {
        return Role.builder()
                .id(source.getId())
                .name(source.getName())
                .build();

    }

    public RoleDto mapRoleDto(Role source) {
        return RoleDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

    public Set<Role> mapAllRoles(Set<RoleDto> source) {
        return source.stream().map(RoleMapper::mapRole).collect(Collectors.toSet());
    }

    public Set<RoleDto> mapAllRolesDto(Set<Role> source) {
        return source.stream().map(RoleMapper::mapRoleDto).collect(Collectors.toSet());
    }
}
