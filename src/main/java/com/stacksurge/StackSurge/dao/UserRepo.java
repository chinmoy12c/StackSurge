package com.stacksurge.StackSurge.dao;

import com.stacksurge.StackSurge.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer>{
    public User getByEmail(String email);

    public User getByEmailAndType(String email, String type);
}
