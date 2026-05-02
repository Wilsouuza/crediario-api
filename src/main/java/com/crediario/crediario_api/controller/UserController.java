package com.crediario.crediario_api.controller;

import com.crediario.crediario_api.business.dto.user.request.CreateUserRequest;

import com.crediario.crediario_api.business.dto.user.request.LoginRequest;
import com.crediario.crediario_api.business.dto.user.request.UpdatePasswordRequest;
import com.crediario.crediario_api.business.dto.user.response.UserResponse;
import com.crediario.crediario_api.business.enums.UserType;
import com.crediario.crediario_api.business.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public  ResponseEntity<List<UserResponse>>  findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userType}")
    public ResponseEntity<List<UserResponse>>  findByType(@Valid @PathVariable UserType userType){
        return ResponseEntity.ok(userService.findByUserType(userType));
    }

    @PostMapping
    public ResponseEntity<UserResponse>  createUser(@Valid @RequestBody CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(userService.login(request));

    }

    @PutMapping("/password")
    public ResponseEntity<UserResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest request){
        return ResponseEntity.ok(userService.updatePassword(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
