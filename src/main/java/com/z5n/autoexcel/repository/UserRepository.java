package com.z5n.autoexcel.repository;

import com.z5n.autoexcel.model.entity.User;

public interface UserRepository extends BaseRepository<User, String> {
    User findByName(String name);
    User findByEmail(String email);
}
