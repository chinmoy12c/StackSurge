package com.stacksurge.StackSurge.controllers;

import java.util.UUID;

import com.stacksurge.StackSurge.Models.User;
import com.stacksurge.StackSurge.dao.UserRepo;
import com.stacksurge.StackSurge.utility.Constants;
import com.stacksurge.StackSurge.utility.DockerUtil;
import com.stacksurge.StackSurge.utility.JwtUtils;
import com.stacksurge.StackSurge.utility.ResponseBody;
import com.stacksurge.StackSurge.utility.Sanitize;
import com.stacksurge.StackSurge.utility.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping(path = "/register")
    public ResponseBody registerUser(
        @RequestParam(defaultValue = "") String email,
        @RequestParam(defaultValue = "") String password,
        @RequestParam(defaultValue = "") String jwt) {
        ResponseBody response = new ResponseBody();
        email = sanitize.makeSafe(email.strip());
        password = sanitize.makeSafe(password.strip());
        jwt = sanitize.makeSafe(jwt.strip());
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

    @PostMapping(path = "/login")
    public ResponseBody loginUser(
        @RequestParam(defaultValue = "") String email,
        @RequestParam(defaultValue = "") String password) {
        ResponseBody response = new ResponseBody();
        email = sanitize.makeSafe(email.strip());
        password = sanitize.makeSafe(password.strip());
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
