package com.capgemini.wsb.fitnesstracker.user.api;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);
    Optional<User> getUser(Long userId);
    List<User> searchUsersByEmail(String email);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    List<User> findAllUsers();
}

