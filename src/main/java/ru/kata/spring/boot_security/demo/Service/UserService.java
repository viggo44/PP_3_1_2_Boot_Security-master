package ru.kata.spring.boot_security.demo.Service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    UserDetails loadUserByUsername(String username);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    User getUser(Long id);

    List<User> getAllUsers();

}
