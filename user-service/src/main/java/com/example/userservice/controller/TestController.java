package com.example.userservice.controller;

import com.example.userservice.constants.ApiConstants;
import com.example.userservice.context.UserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.BASIC_API_URL)
public class TestController {

//    @GetMapping("/test")
//    public String test() {
//        return "SECURED API WORKING";
//    }
    @GetMapping("/test")
    public String test(@RequestHeader("X-User") String username) {
        String userName= UserContext.getUser();
        return "Hello " + username + " SECURED API WORKING";
    }

    @GetMapping("/users")
    public String user(@RequestHeader String username) {
        String userName= UserContext.getUser();
        return "Hello this is user role " + username + " SECURED API WORKING";
    }

}
