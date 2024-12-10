package com.vivek.medici.service;

import com.vivek.medici.controller.UserController;
import com.vivek.medici.dto.LoginDTO;
import com.vivek.medici.dto.LoginResponse;
import com.vivek.medici.dto.UpdateUserDTO;
import com.vivek.medici.dto.UserDTO;
import com.vivek.medici.exception.InvalidCredentialException;
import com.vivek.medici.exception.UserAlreadyExistsException;
import com.vivek.medici.model.User;
import com.vivek.medici.repository.UserRepository;
import com.vivek.medici.util.JwtTokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = mapToEntity(userDTO);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        try {
            user = userRepository.save(user);
            return mapToDTO(user);
        } catch (DataIntegrityViolationException ex) {
            logger.error("failed to store user" , ex);
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    public UserDTO updateUser(UpdateUserDTO userDTO, Long id) {
        User user = mapToEntity(userDTO);
        User userFromDb = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setId(id);
        user.setPassword(userFromDb.getPassword());
        try {
            user = userRepository.save(user);
            return mapToDTO(user);
        } catch (DataIntegrityViolationException ex) {
            logger.error("User already exists " , ex);
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(user);
    }

    private LoginResponse mapToLoginResponse(String token) {
        return new LoginResponse(token);
    }

    private User mapToEntity(UserDTO userDTO) {
        return new User(userDTO);
    }

    private User mapToEntity(UpdateUserDTO userDTO) {
        return new User(userDTO);
    }

    public LoginResponse loginUser(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if(user == null) {
            logger.error("User not found for user username " , loginDTO.getUsername());
            throw new UsernameNotFoundException("User not found");
        }
        if(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            String token = jwtTokenUtil.generateToken(user.getUsername());
            return new LoginResponse(token);
        }
        logger.error("Invalid username or password " , loginDTO.getUsername());
        throw new InvalidCredentialException("Invalid username or password");
    }

}
