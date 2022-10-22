package com.stacksurge.StackSurge.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.text.MessageFormat;

import com.stacksurge.StackSurge.Models.ResponseBody;
import com.stacksurge.StackSurge.Models.TechStack;
import com.stacksurge.StackSurge.dao.TechStackRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
public class DockerUtil {

    @Autowired
    private TechStackRepo techStackRepo;

    /// Processes raw command on the system.
    private ResponseBody process(String command) {
        ResponseBody response = new ResponseBody();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder out = new StringBuilder();
            StringBuilder error = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null)
                error.append(line);
            while ((line = outputReader.readLine()) != null)
                out.append(line);

            if (error.length() != 0) {
                response.setSuccess(false);
                response.setStatusCode(500);
                response.setError(error.toString());
            } else {
                response.setSuccess(true);
                response.setStatusCode(200);
                response.setResponse(out.toString());
            }
            return response;
        } catch (IOException e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setError(e.getMessage());
            e.printStackTrace();
            return response;
        }
    }

    /// Runs a docker container.
    public ResponseBody runContainer(String volume, String containerTag, String username, String password,
        String port, String containerName) {
        String caddyPassword = Base64Utils.encodeToString(password.getBytes());
        String command = MessageFormat.format(
                "docker run --detach --volume={0}:/home/student/studentData --net={1} --name={2} --env=CADDY_USERNAME={3} --env=CADDY_PASSWORD={4} --publish={5}:5902 --shm-size=4G {6}",
                volume, Constants.DOCKER_NETWORK, containerTag, username, caddyPassword, port, containerName);
        return process(command);
    }

    /// Creates initial volume to be used by a user.
    public ResponseBody createVolume(String volume) {
        String command = MessageFormat.format("docker volume create {0}", volume);
        return process(command);
    }

    /// Stops a running container.
    public ResponseBody stopContainer(String instance) {
        String command = MessageFormat.format("docker stop {0}", instance);
        return process(command);
    }

    /// Finds a free port on the system to attach an instance to.
    public ResponseBody getFreePort() {
        ResponseBody response = new ResponseBody();
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            response.setSuccess(true);
            response.setStatusCode(200);
            response.setResponse(String.valueOf(serverSocket.getLocalPort()));
        } catch (IOException e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    /// Resolves instance image name from id.
    public ResponseBody resolveContainer(String codename) {
        TechStack techStack = techStackRepo.getByCodename(codename);
        ResponseBody response = new ResponseBody();
        if (techStack != null) {
            response.setResponse(techStack.getName());
            response.setStatusCode(200);
            response.setSuccess(true);
        }
        else {
            response.setError("Invalid container code!");
            response.setStatusCode(200);
            response.setSuccess(false);
        }
        return response;
    }
}
