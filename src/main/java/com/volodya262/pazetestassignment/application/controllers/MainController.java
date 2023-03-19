package com.volodya262.pazetestassignment.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("")
    public String getMain() {
        return "redirect:/checkout";
    }
}
