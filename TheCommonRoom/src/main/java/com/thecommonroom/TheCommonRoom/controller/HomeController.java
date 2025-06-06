package com.thecommonroom.TheCommonRoom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String index() {
        return "index"; // templates/index.html
    }

    @GetMapping("/moviesheet")
    public String moviesheet() {
        return "moviesheet"; // templates/moviesheet.html
    }

    @GetMapping("/signin")
    public String signin() {
        return "signin"; // templates/signin.html
    }

}

