package com.stacksurge.StackSurge.utility;

import com.stacksurge.StackSurge.Models.User;
import com.stacksurge.StackSurge.dao.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    @Autowired
    UserRepo userRepo;

    public boolean isAdmin(String email) {
        User user = userRepo.getByEmail(email);
        if (user.getType().equals("admin"))
            return true;
        else
            return false;
    }
}
