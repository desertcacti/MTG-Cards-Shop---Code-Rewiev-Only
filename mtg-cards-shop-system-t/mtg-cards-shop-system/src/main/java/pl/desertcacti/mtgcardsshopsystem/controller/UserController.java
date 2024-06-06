package pl.desertcacti.mtgcardsshopsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.desertcacti.mtgcardsshopsystem.entity.User;
import pl.desertcacti.mtgcardsshopsystem.service.user.UserService;

/** UserController class handles requests related to user operations
 such as finding a user by email. */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Find a user by their email address. */
    @GetMapping("/findByEmail")
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

}
