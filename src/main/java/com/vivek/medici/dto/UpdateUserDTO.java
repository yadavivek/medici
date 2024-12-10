package com.vivek.medici.dto;

import com.vivek.medici.model.User;
import jakarta.validation.constraints.*;

public class UpdateUserDTO {
    private Long id;

    @Size(min = 4, max = 50)
    @NotEmpty
    @NotBlank
    private String username;
    @NotEmpty
    @NotBlank
    private String firstName;
    private String lastName;
    @Email
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")

    public UpdateUserDTO(Long id, String username, String firstName, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UpdateUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
    public UpdateUserDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
