package com.stacksurge.StackSurge.dao;

import java.util.List;

import com.stacksurge.StackSurge.Models.Instance;
import com.stacksurge.StackSurge.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceRepo extends JpaRepository<Instance, Integer> {
    public Instance getByUserAndStackContainerId(User user, String stackContainerId);

    public List<Instance> getByUser(User user);
}
