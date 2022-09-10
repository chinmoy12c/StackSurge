package com.stacksurge.StackSurge.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.stacksurge.StackSurge.Models.Instance;
import com.stacksurge.StackSurge.Models.ResponseBody;
import com.stacksurge.StackSurge.Models.User;
import com.stacksurge.StackSurge.dao.InstanceRepo;
import com.stacksurge.StackSurge.dao.TechStackRepo;
import com.stacksurge.StackSurge.dao.UserRepo;
import com.stacksurge.StackSurge.utility.DockerUtil;
import com.stacksurge.StackSurge.utility.JwtUtils;
import com.stacksurge.StackSurge.utility.Sanitize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

@RestController
public class InstanceController {
    @Autowired
    InstanceRepo instanceRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    TechStackRepo techStackRepo;
    @Autowired
    Sanitize sanitize;
    @Autowired
    DockerUtil dockerUtil;
    @Autowired
    JwtUtils jwtUtils;

    /// Api endpoint to launch an instance.
    ///
    /// Required Data:
    /// jwt - Auth token of the current user
    /// codename - Path variable containing the id of the instance to be launched.
    @PostMapping(path = "/launchInstance/{codename}", consumes = "application/json")
    public ResponseBody launchContainer(@PathVariable("codename") String codename, @RequestBody HashMap<String, String> request) {
        ResponseBody response = new ResponseBody();
        String jwt = sanitize.makeSafe(request.getOrDefault("jwt", "").strip());
        codename = sanitize.makeSafe(codename.strip());
        if (jwt.length() == 0) {
            response.setSuccess(false);
            response.setError("Unauthorized!");
            response.setStatusCode(401);
            return response;
        }
        if (codename.length() == 0) {
            response.setSuccess(false);
            response.setError("Missing fields!");
            response.setStatusCode(200);
            return response;
        }
        ResponseBody jwtVerifyReponse = jwtUtils.verifyToken(jwt);
        if (!jwtVerifyReponse.isSuccess())
            return jwtVerifyReponse;
        User loggedUser = userRepo.getByEmail(jwtVerifyReponse.getResponse());
        ResponseBody resolveContainerResponse = dockerUtil.resolveContainer(codename);
        if (!resolveContainerResponse.isSuccess())
            return resolveContainerResponse;
        ResponseBody portResponse = dockerUtil.getFreePort();
        if (!portResponse.isSuccess())
            return portResponse;
        String containerTag = UUID.randomUUID().toString();
        ResponseBody containerReponse = dockerUtil.runContainer(loggedUser.getVolume(), containerTag, loggedUser.getEmail(), loggedUser.getPassword(), portResponse.getResponse(), resolveContainerResponse.getResponse());
        if (!containerReponse.isSuccess())
            return containerReponse;
        Instance createdInstance = new Instance();
        createdInstance.setUser(loggedUser);
        createdInstance.setStackContainerId(containerTag);
        createdInstance.setPort(Integer.parseInt(portResponse.getResponse()));
        createdInstance.setTechStack(techStackRepo.getByCodename(codename));
        instanceRepo.save(createdInstance);

        response.setSuccess(true);
        response.setStatusCode(200);
        response.setResponse("Instance Created!");
        return response;
    }

    /// Api endpoint to stop a running instance.
    ///
    /// Required Data:
    /// jwt - Auth token of the current user
    /// containerId - Unique id of the running container
    @PostMapping(path = "/stopInstance", consumes = "application/json")
    public ResponseBody stopContainer(@RequestBody HashMap<String, String> request) {
        ResponseBody response = new ResponseBody();
        String jwt = sanitize.makeSafe(request.getOrDefault("jwt", "").strip());
        String containerId = sanitize.makeSafe(request.getOrDefault("containerId", "").strip());
        if (jwt.length() == 0) {
            response.setSuccess(false);
            response.setError("Unauthorized!");
            response.setStatusCode(401);
            return response;
        }
        if (containerId.length() == 0) {
            response.setSuccess(false);
            response.setError("Missing fields!");
            response.setStatusCode(200);
            return response;
        }
        ResponseBody jwtVerifyResponse = jwtUtils.verifyToken(jwt);
        if (!jwtVerifyResponse.isSuccess())
            return jwtVerifyResponse;
        User loggedUser = userRepo.getByEmail(jwtVerifyResponse.getResponse());
        Instance runningInstance = instanceRepo.getByUserAndStackContainerId(loggedUser, containerId);
        if (runningInstance == null) {
            response.setSuccess(false);
            response.setStatusCode(403);
            response.setError("Invalid container!");
            return response;
        }
        dockerUtil.stopContainer(containerId);
        instanceRepo.delete(runningInstance);
        response.setSuccess(true);
        response.setStatusCode(200);
        response.setResponse("Stopped successfully");

        return response;
    }

    /// Api endpoint to get all the instances currently activated by a user.
    ///
    /// Required Data:
    /// jwt - Auth token of the current user
    @PostMapping(path = "/getUserInstances", consumes = "application/json")
    public ResponseBody getUserInstances(@RequestBody HashMap<String, String> request) {
        ResponseBody response = new ResponseBody();
        String jwt = sanitize.makeSafe(request.getOrDefault("jwt", "").strip());
        if (jwt.length() == 0) {
            response.setSuccess(false);
            response.setError("Missing fields!");
            response.setStatusCode(200);
            return response;
        }
        ResponseBody jwtVerifyResponse = jwtUtils.verifyToken(jwt);
        if (!jwtVerifyResponse.isSuccess()) {
            response.setSuccess(false);
            response.setError("Unauthorized!");
            response.setStatusCode(401);
            return response;
        }
        User loggedUser = userRepo.getByEmail(jwtVerifyResponse.getResponse());
        List<Instance> instances = instanceRepo.getUserInstances(loggedUser);
        JSONObject instancesObject = new JSONObject();
        instancesObject.put("instances", instances);
        response.setStatusCode(200);
        response.setSuccess(true);
        response.setResponse(instancesObject.toJSONString());
        return response;
    }
}
