package com.crediario.crediario_api.controller;

import com.crediario.crediario_api.business.dto.user.request.CreateUserRequest;

import com.crediario.crediario_api.business.dto.user.request.UpdatePasswordRequest;
import com.crediario.crediario_api.business.dto.user.response.UserResponse;
import com.crediario.crediario_api.business.enums.UserType;
import com.crediario.crediario_api.business.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{userType}")
    public List<UserResponse> findByType(@Valid @PathVariable UserType userType){
        return userService.findByUserType(userType);
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request){
        return userService.createUser(request);
    }

    @PutMapping("/password")
    public UserResponse updatePassword(@Valid @RequestBody UpdatePasswordRequest request){
        return userService.updatePassword(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @PathVariable Long id){
        userService.delete(id);
    }





}
