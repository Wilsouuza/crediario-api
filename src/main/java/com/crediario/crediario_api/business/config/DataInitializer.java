package com.crediario.crediario_api.business.config;

import com.crediario.crediario_api.business.dto.user.request.CreateUserRequest;
import com.crediario.crediario_api.business.enums.UserType;
import com.crediario.crediario_api.business.service.UserService;
import com.crediario.crediario_api.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${app.admin-password:admin123}")
    private String adminPassword;

    public DataInitializer(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByLogin("admin").isEmpty()) {
            userService.createUser(new CreateUserRequest("admin", "admin123", UserType.ADMIN));
        }
    }
}