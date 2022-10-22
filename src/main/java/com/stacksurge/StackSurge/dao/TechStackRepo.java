package com.stacksurge.StackSurge.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stacksurge.StackSurge.Models.TechStack;

public interface TechStackRepo extends JpaRepository<TechStack, Integer> {
    public TechStack getByCodename(String codename);

    public List<TechStack> getByIsPrimary(boolean isPrimary);
}
