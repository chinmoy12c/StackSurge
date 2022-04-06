package com.stacksurge.StackSurge.controllers;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stacksurge.StackSurge.Models.ResponseBody;
import com.stacksurge.StackSurge.utility.JwtUtils;
import com.stacksurge.StackSurge.utility.Sanitize;
import com.stacksurge.StackSurge.utility.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.minidev.json.JSONObject;

@Controller
public class WebController {

    @Autowired
    Sanitize sanitize;
    @Autowired
    UserUtils userUtils;
    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/")
    public ModelAndView getHome(@CookieValue(name = "authToken", defaultValue = "") String authToken) {
        if (authToken.length() == 0) {
            return new ModelAndView("redirect:/login");
        }

        if (!jwtUtils.verifyToken(authToken).isSuccess()) {
            return new ModelAndView("redirect:/logout");
        }

        ModelAndView homeView = new ModelAndView("home");
        return homeView;
    }

    @GetMapping("/errorPage")
    public ModelAndView showError() {
        return new ModelAndView("errorPage");
    }

    @GetMapping("/login")
    public ModelAndView getLogin(@CookieValue(name = "authToken", defaultValue = "") String authToken) {
        if (authToken.length() != 0) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView loginView = new ModelAndView("login");
        return loginView;
    }

    @GetMapping("/logout") 
    public ModelAndView logout(HttpServletResponse response) {
        Cookie authToken = new Cookie("authToken", null);
        //TODO: set http only
        authToken.setMaxAge(0);
        response.addCookie(authToken);
        return new ModelAndView("redirect:/login");
    }

    @PostMapping("/initiateLogin")
    public ModelAndView initiateLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String loginUrl = baseUrl + "/processLogin";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", request.getParameter("email") == null ? "" : request.getParameter("email"));
        requestBody.put("password", request.getParameter("password") == null ? "" : request.getParameter("password"));
        ResponseBody loginData = restTemplate.postForObject(loginUrl, requestBody, ResponseBody.class);
        if (loginData.isSuccess()) {
            userUtils.setJwtCookie(response, loginData);
            return new ModelAndView("redirect:/");
        } else {
            ModelAndView errorPage = new ModelAndView("redirect:/errorPage");
            errorPage.addObject("errorData", loginData);
            return errorPage;
        }
    }
}
