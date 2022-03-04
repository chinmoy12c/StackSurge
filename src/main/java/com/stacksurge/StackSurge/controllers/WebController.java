package com.stacksurge.StackSurge.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {
    
    @GetMapping("/")
    public ModelAndView getHome() {
        ModelAndView mView = new ModelAndView("index");
        return mView;
    }
}
