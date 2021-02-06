package kz.zhanbolat.user.service;

import kz.zhanbolat.user.entity.User;

public interface UserService {
    User getUser(Long userId);
    User createUser(User user);
    User updateUser(User user);
}
