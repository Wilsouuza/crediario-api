package com.crediario.crediario_api.business.service;

import com.crediario.crediario_api.business.dto.user.request.CreateUserRequest;
import com.crediario.crediario_api.business.dto.user.request.UpdatePasswordRequest;
import com.crediario.crediario_api.business.dto.user.response.UserResponse;
import com.crediario.crediario_api.business.entity.User;
import com.crediario.crediario_api.business.enums.UserType;
import com.crediario.crediario_api.business.exception.BusinessException;
import com.crediario.crediario_api.business.mapper.UserMapper;
import com.crediario.crediario_api.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(CreateUserRequest request){
        Optional<User> user = userRepository.findByLogin(request.login());

        if (user.isPresent()){
            throw new BusinessException("User already exists.");
        }

        User newUser = UserMapper.toEntity(request);

        User savedUser = userRepository.save(newUser);

        return UserMapper.toResponse(savedUser);
    }

    public User login(String login, String password){
        User user = userRepository.findByLogin(login)
                .orElseThrow(()-> new BusinessException("User not found."));

        if (!password.equals(user.getPassword())){
            throw new BusinessException("Incorrect password.");
        }
        return user;
    }

    public UserResponse updatePassword(UpdatePasswordRequest request){
        User user = login(request.login(), request.currentPassword());

        if(request.newPassword().equals(user.getPassword())){
            throw new BusinessException("The new password cannot be the same as the current password.");
        }
        user.setPassword(request.newPassword());
        User savedUser = userRepository.save(user);

        return  new UserResponse(savedUser.getId(), savedUser.getLogin(), savedUser.getUserType());
    }

    public void delete(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new BusinessException("User not found."));

        validateDeletableUser(user);

        userRepository.delete(user);
    }

    private void validateDeletableUser(User user){
        UserType userType = user.getUserType();
        if (userType == UserType.ADMIN){
            throw new BusinessException("Admin users cannot be deleted");
        }
        if (userType == UserType.CUSTOMER) {
            throw new BusinessException("Customer users cannot be deleted");
        }
    }

    public List<UserResponse> findAll(){
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public List<UserResponse> findByUserType(UserType userType){
        return userRepository.findByUserType(userType)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public void addCommission(Long id, BigDecimal amount){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new BusinessException("User not found."));

        user.setCommission(user.getCommission().add(amount));
        userRepository.save(user);
    }

}
