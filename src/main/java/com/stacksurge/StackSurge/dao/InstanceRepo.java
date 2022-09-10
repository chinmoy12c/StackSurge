package com.stacksurge.StackSurge.dao;

import java.util.List;

import com.stacksurge.StackSurge.Models.Instance;
import com.stacksurge.StackSurge.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceRepo extends JpaRepository<Instance, Integer> {
    public Instance getByUserAndStackContainerId(User user, String stackContainerId);

    public List<Instance> getByUser(User user);

    /// Remove user details to protect user data from client side.
    public default List<Instance> getUserInstances(User user) {
        List<Instance> instances = getByUser(user);
        for (Instance instance : instances)
            instance.setUser(null);
        return instances;
    }
}
