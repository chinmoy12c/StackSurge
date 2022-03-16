package com.stacksurge.StackSurge.controllers;

import java.util.HashMap;
import java.util.UUID;

import com.stacksurge.StackSurge.Models.ResponseBody;
import com.stacksurge.StackSurge.Models.User;
import com.stacksurge.StackSurge.dao.UserRepo;
import com.stacksurge.StackSurge.utility.Constants;
import com.stacksurge.StackSurge.utility.DockerUtil;
import com.stacksurge.StackSurge.utility.JwtUtils;
import com.stacksurge.StackSurge.utility.Sanitize;
import com.stacksurge.StackSurge.utility.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
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

    @PostMapping(path = "/processRegister", consumes = "application/json")
    public ResponseBody registerUser(@RequestBody HashMap<String, String> request) {
        ResponseBody response = new ResponseBody();
        String email = sanitize.makeSafe(request.getOrDefault("email", "").strip());
        String password = sanitize.makeSafe(request.getOrDefault("password", "").strip());
        String jwt = sanitize.makeSafe(request.getOrDefault("jwt", "").strip());
        if (email.length() == 0 || password.length() == 0 || jwt.length() == 0) {
            response.setSuccess(false);
            response.setError("Missing fields!");
            return response;
        }

        ResponseBody jwtVerifyResponse = jwtUtils.verifyToken(jwt);
        if (!jwtVerifyResponse.isSuccess())
            return jwtVerifyResponse;
        if (!userUtils.isAdmin(jwtVerifyResponse.getResponse())) {
            response.setSuccess(false);
            response.setError("Unauthorized!");
            return response;
        }
        User existingUser = userRepo.getByEmail(email);
        if (existingUser != null) {
            response.setSuccess(false);
            response.setError("User already exists!");
            return response;
        }

        String volume = UUID.randomUUID().toString();
        ResponseBody createVolumeResponse = dockerUtil.createVolume(volume);
        if (!createVolumeResponse.isSuccess())
            return response;
        ResponseBody caddyHashResponse = dockerUtil.createCaddyHash(password);
        if (!caddyHashResponse.isSuccess())
            return caddyHashResponse;
        String caddyHash = caddyHashResponse.getResponse();
        password = DigestUtils.md5DigestAsHex((password + Constants.HASH_SECRET_KEY).getBytes());

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setVolume(volume);
        newUser.setType("user");
        newUser.setCaddyPass(caddyHash);
        userRepo.save(newUser);

        String userToken = jwtUtils.createToken(newUser.getEmail());
        response.setSuccess(true);
        response.setResponse(userToken);
        return response;
    }

    @PostMapping(path = "/processLogin", consumes = "application/json")
    public ResponseBody loginUser(@RequestBody HashMap<String, String> request) {
        ResponseBody response = new ResponseBody();
        String email = sanitize.makeSafe(request.getOrDefault("email", "").strip());
        String password = sanitize.makeSafe(request.getOrDefault("password", "").strip());
        if (email.length() == 0 || password.length() == 0) {
            response.setSuccess(false);
            response.setError("Missing fields!");
            return response;
        }

        password = DigestUtils.md5DigestAsHex((password + Constants.HASH_SECRET_KEY).getBytes());
        User loggedUser = userRepo.getByEmailAndPassword(email, password);
        if (loggedUser == null) {
            response.setSuccess(false);
            response.setError("Wrong username or password!");
            return response;
        }

        String userToken = jwtUtils.createToken(email);
        response.setSuccess(true);
        response.setResponse(userToken);
        return response;
    }
}
