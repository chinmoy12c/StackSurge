package com.stacksurge.StackSurge.controllers;

import java.util.HashMap;
import java.util.UUID;

import com.stacksurge.StackSurge.Models.ResponseBody;
import com.stacksurge.StackSurge.Models.User;
import com.stacksurge.StackSurge.dao.UserRepo;
import com.stacksurge.StackSurge.utility.DockerUtil;
import com.stacksurge.StackSurge.utility.JwtUtils;
import com.stacksurge.StackSurge.utility.Sanitize;
import com.stacksurge.StackSurge.utility.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private DockerUtil dockerUtil;
    @Autowired
    private Sanitize sanitize;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserUtils userUtils;

    /// API end point for registering a user.
    /// Only accessible to admin users.
    ///
    /// Required Data:
    /// email - Email of user being registered
    /// password - Password of user being registered
    /// jwt - Auth token of current user
    @PostMapping(path = "/processRegister", consumes = "application/json")
    public ResponseBody registerUser(@RequestBody HashMap<String, String> request) {
        ResponseBody response = new ResponseBody();
        String email = sanitize.makeSafe(request.getOrDefault("email", "").strip());
        String password = sanitize.makeSafe(request.getOrDefault("password", "").strip());
        String jwt = sanitize.makeSafe(request.getOrDefault("jwt", "").strip());
        if (jwt.length() == 0) {
            response.setSuccess(false);
            response.setError("Unauthorized!");
            response.setStatusCode(401);
            return response;
        }
        if (email.length() == 0 || password.length() == 0) {
            response.setSuccess(false);
            response.setError("Missing fields!");
            response.setStatusCode(200);
            return response;
        }

        ResponseBody jwtVerifyResponse = jwtUtils.verifyToken(jwt);
        if (!jwtVerifyResponse.isSuccess())
            return jwtVerifyResponse;
        if (!userUtils.isAdmin(jwtVerifyResponse.getResponse())) {
            response.setSuccess(false);
            response.setError("Unauthorized!");
            response.setStatusCode(403);
            return response;
        }
        User existingUser = userRepo.getByEmail(email);
        if (existingUser != null) {
            response.setSuccess(false);
            response.setStatusCode(200);
            response.setError("User already exists!");
            return response;
        }

        String volume = UUID.randomUUID().toString();
        ResponseBody createVolumeResponse = dockerUtil.createVolume(volume);
        if (!createVolumeResponse.isSuccess())
            return response;
        password = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setVolume(volume);
        newUser.setType("user");
        userRepo.save(newUser);

        String userToken = jwtUtils.createToken(newUser.getEmail());
        response.setSuccess(true);
        response.setStatusCode(200);
        response.setResponse(userToken);
        return response;
    }

    /// Api endpoint to login a user.
    ///
    /// Required Data:
    /// email - Email of user being logged in
    /// password - Password of user being logged in
    @PostMapping(path = "/processLogin", consumes = "application/json")
    public ResponseBody loginUser(@RequestBody HashMap<String, String> request) {
        ResponseBody response = new ResponseBody();
        String email = sanitize.makeSafe(request.getOrDefault("email", "").strip());
        String password = sanitize.makeSafe(request.getOrDefault("password", "").strip());
        if (email.length() == 0 || password.length() == 0) {
            response.setSuccess(false);
            response.setStatusCode(200);
            response.setError("Missing fields!");
            return response;
        }

        User loggedUser = userRepo.getByEmail(email);
        if (loggedUser == null || !BCrypt.checkpw(password, loggedUser.getPassword())) {
            response.setSuccess(false);
            response.setStatusCode(200);
            response.setError("Wrong username or password!");
            return response;
        }

        String userToken = jwtUtils.createToken(email);
        response.setSuccess(true);
        response.setStatusCode(200);
        response.setResponse(userToken);
        return response;
    }
}
