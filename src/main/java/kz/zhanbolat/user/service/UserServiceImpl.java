package kz.zhanbolat.user.service;

import kz.zhanbolat.user.entity.User;
import kz.zhanbolat.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(Long userId) {
        Objects.requireNonNull(userId, "User id cannot be null");
        return userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User with such id doesn't exists"));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        if (isStringEmpty(user.getUsername()) || isStringEmpty(user.getPassword())) {
            throw new IllegalArgumentException("Username or password cannot be null");
        }
        if (Objects.nonNull(user.getId()) && userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        if (Objects.isNull(user.getId()) || isStringEmpty(user.getUsername())
                || isStringEmpty(user.getPassword())) {
            throw new IllegalArgumentException("Username or password cannot be empty, and id cannot be 0");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Cannot update not existing user");
        }
        return userRepository.save(user);
    }

    private boolean isStringEmpty(String string) {
        return Objects.isNull(string) || "".equals(string);
    }
}
