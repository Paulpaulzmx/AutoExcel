package com.z5n.autoexcel.repository;

import com.z5n.autoexcel.model.entity.Role;

public interface RoleRepository extends BaseRepository<Role, String> {
    Role findByName(String name);
}
