package com.stacksurge.StackSurge.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.text.MessageFormat;

import org.springframework.stereotype.Component;

@Component
public class DockerUtil {

    class ContainerType {
        static final String TOMCAT = "tomstack";
        static final String CODESTACK = "codestack";
        static final String MERNSTACK = "mernstack";
    }

    public ResponseBody process(String command) {
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
                response.setError(error.toString());
            } else {
                response.setSuccess(true);
                response.setResponse(out.toString());
            }
            return response;
        } catch (IOException e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
            e.printStackTrace();
            return response;
        }
    }

    public ResponseBody createCaddyHash(String password) {
        String command = MessageFormat.format("docker exec {0} caddy hash-password -plaintext {1}",
                Constants.CADDY_PASS_GEN_CONTAINER, password);
        return process(command);
    }

    public ResponseBody runContainer(String volume, String containerTag, String containerName) {
        String command = MessageFormat.format(
                "docker run --detach --volume={0}:/home/student/studentData --net={1} --name={2} {3}",
                volume, Constants.DOCKER_NETWORK, containerTag, containerName);
        return process(command);
    }

    public ResponseBody runCaddy(String username, String password, String volume, String port, String attachTo,
            String name) {
        String command = MessageFormat.format(
                "docker run --detach --restart=always --volume={0}:/home/student/studentData --name={1} --net={2} --env=APP_USERNAME={3} --env=APP_PASSWORD_HASH={4} --env=ATTACH_TO={5} --publish={6}:8080 caddy",
                volume, name, Constants.DOCKER_NETWORK, username, password, attachTo, port);
        return process(command);
    }

    public ResponseBody createVolume(String volume) {
        String command = MessageFormat.format("docker volume create {0}", volume);
        return process(command);
    }

    public ResponseBody stopContainer(String instance, String caddy) {
        String command = MessageFormat.format("docker stop {0} {1}", instance, caddy);
        return process(command);
    }

    public ResponseBody getFreePort() {
        ResponseBody response = new ResponseBody();
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            response.setSuccess(true);
            response.setResponse(String.valueOf(serverSocket.getLocalPort()));
        } catch (IOException e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ResponseBody resolveContainer(String id) {
        ResponseBody response = new ResponseBody();
        switch (id) {
            case "TOM":
                response.setResponse(ContainerType.TOMCAT);
                break;
            case "CODE":
                response.setResponse(ContainerType.CODESTACK);
                break;
            case "MERN":
                response.setResponse(ContainerType.MERNSTACK);
                break;
            default:
                response.setSuccess(false);
                response.setError("No matching container!");
                return response;
        }
        response.setSuccess(true);
        return response;
    }
}
