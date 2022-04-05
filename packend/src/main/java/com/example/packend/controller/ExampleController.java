package com.example.packend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class ExampleController {

    @GetMapping("/get")
    public String getMe(){
        System.out.println("Im in the controller!");
        return "This works!";
    }
}
