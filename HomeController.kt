package com.example.scscollision

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @RequestMapping("/")
    fun home(): String {
        return "Welcome to SCS Collision Application"
    }

}