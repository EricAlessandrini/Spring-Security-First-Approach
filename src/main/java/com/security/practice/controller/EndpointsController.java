package com.security.practice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/endpoint")
public class EndpointsController {

    @GetMapping("/client")
    public ResponseEntity<String> helloClient() {
        return new ResponseEntity<>("If you are seeing this message is because you have READ permission", HttpStatus.OK);
    }

    @GetMapping("/employee")
    public ResponseEntity<String> helloEmployee() {
        return new ResponseEntity<>("If you are seeing this message is because you have UPDATE permission", HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<String> helloOwner() {
        return new ResponseEntity<>("If you seeing this message is because you have DELETE permission", HttpStatus.OK);
    }
}
