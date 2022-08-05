package com.stacksurge.StackSurge.utility;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.stacksurge.StackSurge.Models.ResponseBody;
import com.stacksurge.StackSurge.Models.User;
import com.stacksurge.StackSurge.dao.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    @Autowired
    UserRepo userRepo;

    /// Check if the email belongs to an admin user.
    public boolean isAdmin(String email) {
        User user = userRepo.getByEmailAndType(email, "admin");
        if (user == null)
            return false;
        else
            return true;
    }

    /// Sets the jwt auth token as a cookie.
    public void setJwtCookie(HttpServletResponse response, ResponseBody loginData) {
        Cookie authCookie = new Cookie("authToken", loginData.getResponse());
        // TODO: uncomment these
        // authCookie.setHttpOnly(true);
        // authCookie.setSecure(true);
        response.addCookie(authCookie);
    }
}
