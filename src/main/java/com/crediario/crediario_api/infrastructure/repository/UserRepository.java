package com.crediario.crediario_api.infrastructure.repository;

import com.crediario.crediario_api.business.entity.User;
import com.crediario.crediario_api.business.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByLogin(String login);
    public List<User> findByUserType(UserType type);

}
