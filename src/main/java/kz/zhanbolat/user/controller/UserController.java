package kz.zhanbolat.user.controller;

import kz.zhanbolat.user.controller.dto.ErrorResponse;
import kz.zhanbolat.user.entity.User;
import kz.zhanbolat.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/auth")
    public User authenticateUser(User user) {
        return userService.authenticateUser(user.getUsername(), user.getPassword());
    }

    @ExceptionHandler
    public ErrorResponse handleException(Exception exception) {
        logger.error("Caught exception: " + exception);
        return new ErrorResponse("Internal server error.");
    }
}
