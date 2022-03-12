package com.stacksurge.StackSurge.controllers;

import java.util.UUID;

import com.stacksurge.StackSurge.Models.Instance;
import com.stacksurge.StackSurge.Models.User;
import com.stacksurge.StackSurge.dao.InstanceRepo;
import com.stacksurge.StackSurge.dao.UserRepo;
import com.stacksurge.StackSurge.utility.DockerUtil;
import com.stacksurge.StackSurge.utility.JwtUtils;
import com.stacksurge.StackSurge.utility.ResponseBody;
import com.stacksurge.StackSurge.utility.Sanitize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstanceController {
    @Autowired
    InstanceRepo instanceRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    Sanitize sanitize;
    @Autowired
    DockerUtil dockerUtil;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/launchInstance/{containerId}")
    public ResponseBody launchContainer(
        @PathVariable("containerId") String containerId,
        @RequestParam(defaultValue = "") String jwt) {
        ResponseBody response = new ResponseBody();
        jwt = sanitize.makeSafe(jwt.strip());
        ResponseBody jwtVerifyReponse = jwtUtils.verifyToken(jwt);
        if (!jwtVerifyReponse.isSuccess())
            return jwtVerifyReponse;
        User loggedUser = userRepo.getByEmail(jwtVerifyReponse.getResponse());
        ResponseBody resolveContainerResponse = dockerUtil.resolveContainer(containerId);
        if (!resolveContainerResponse.isSuccess())
            return resolveContainerResponse;
        ResponseBody portResponse = dockerUtil.getFreePort();
        if (!portResponse.isSuccess())
            return portResponse;
        String containerTag = UUID.randomUUID().toString();
        ResponseBody containerReponse = dockerUtil.runContainer(loggedUser.getVolume(), containerTag, resolveContainerResponse.getResponse());
        if (!containerReponse.isSuccess())
            return containerReponse;
        String caddyTag = UUID.randomUUID().toString();
        ResponseBody caddyLaunchResponse = dockerUtil.runCaddy(loggedUser.getEmail(), loggedUser.getCaddyPass(), loggedUser.getVolume(), portResponse.getResponse(), containerTag, caddyTag);
        if (!caddyLaunchResponse.isSuccess())
            return caddyLaunchResponse;
        Instance createdInstance = new Instance();
        createdInstance.setUser(loggedUser);
        createdInstance.setCaddyContainerId(caddyTag);
        createdInstance.setStackContainerId(containerTag);
        createdInstance.setPort(Integer.parseInt(portResponse.getResponse()));
        createdInstance.setStackName(resolveContainerResponse.getResponse());
        instanceRepo.save(createdInstance);

        response.setSuccess(true);
        response.setResponse("Instance Created!");
        return response;
    }

    @PostMapping("/stopInstance") 
    public ResponseBody stopContainer(
        @RequestParam(defaultValue = "") String containerId,
        @RequestParam(defaultValue = "") String jwt) {
        ResponseBody response = new ResponseBody();
        containerId = sanitize.makeSafe(containerId.strip());
        jwt = sanitize.makeSafe(jwt.strip());

        ResponseBody jwtVerifyResponse = jwtUtils.verifyToken(jwt);
        if (!jwtVerifyResponse.isSuccess())
            return jwtVerifyResponse;
        User loggedUser = userRepo.getByEmail(jwtVerifyResponse.getResponse());
        Instance runningInstance = instanceRepo.getByUserAndStackContainerId(loggedUser, containerId);
        if (runningInstance == null) {
            response.setSuccess(false);
            response.setError("Invalid container!");
            return response;
        }
        dockerUtil.stopContainer(containerId, runningInstance.getCaddyContainerId());
        instanceRepo.delete(runningInstance);
        response.setSuccess(true);
        response.setResponse("Stopped successfully");

        return response;
    }
}
