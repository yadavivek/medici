package com.vivek.medici.controller;

import com.vivek.medici.dto.LoginDTO;
import com.vivek.medici.dto.LoginResponse;
import com.vivek.medici.dto.UpdateUserDTO;
import com.vivek.medici.dto.UserDTO;
import com.vivek.medici.response.ResponseHandler;
import com.vivek.medici.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getOneUser(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDTO userDTO) {
        logger.debug("Creating user with details " , userDTO);
        UserDTO userDTO1 = userService.createUser(userDTO);
        return ResponseHandler.generateResponse("User registered successfully", HttpStatus.CREATED, userDTO1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UpdateUserDTO userDTO, @PathVariable Long id) {
        System.out.println("Creating user: " + userDTO);
        UserDTO userDTO1 = userService.updateUser(userDTO, id);
        return ResponseHandler.generateResponse("User Updated successfully", HttpStatus.OK, userDTO1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginDTO loginDTO) {
        LoginResponse loginResponse = userService.loginUser(loginDTO);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
