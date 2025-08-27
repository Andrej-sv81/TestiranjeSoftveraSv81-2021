package com.ftn.ts.controller;

import com.ftn.ts.dto.UserODDTO;
import com.ftn.ts.model.UserOD;
import com.ftn.ts.service.UserODService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountExpiredException;
import java.security.Principal;
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserODService service;

    @GetMapping("/login")
    public ResponseEntity<String> login(Principal principal) {
        UserOD user = service.getUserByEmail(principal.getName());
        if (user == null) {
            throw new EntityNotFoundException("User does not exist!");
        }
        return ResponseEntity.ok(user.getEmail());
    }

    @PostMapping("/registration/")
    public ResponseEntity<UserODDTO> newUser(@RequestBody UserODDTO dto) {

        try {
            var newUserDTO = service.newUser(dto);
            return new ResponseEntity<>(newUserDTO, HttpStatus.OK);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/registration/verification/{newUserMail}")
    public ResponseEntity<String> confirm(@PathVariable String newUserMail) {
        try {
            service.activateUser(newUserMail);
            var message = "Account with email " + newUserMail + " activated. Proceed to our page and login.";
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch (UsernameNotFoundException e) {
            var message = "Email not found. Try to register again.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        catch (AccountExpiredException e) {
            var message = "Activation expired. Try to register again.";
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

}
