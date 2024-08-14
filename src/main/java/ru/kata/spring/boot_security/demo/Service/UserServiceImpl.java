package ru.kata.spring.boot_security.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.Repository.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get().getUser();
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        Optional<User> optionalExistingUser = userRepository.findById(user.getId());

        if (optionalExistingUser.isPresent()) {
            User existingUser = optionalExistingUser.get();

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            }
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setLastName(user.getLastName());
            existingUser.setAge(user.getAge());

            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                existingUser.setRoles(user.getRoles());
            }
            userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("User not found with id: " + user.getId());
        }
    }


    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
