package com.ftn.ts.controller;

import com.ftn.ts.dto.UserODDTO;
import com.ftn.ts.dto.UserPUPDTO;
import com.ftn.ts.model.UserOD;
import com.ftn.ts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountExpiredException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/login")
    public ResponseEntity<String> login(Principal principal) {
        var user = service.login(principal.getName());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (user.get() instanceof UserOD od) {
            if (!od.isActivated()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not activated");
            }
        }
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/registration/")
    public ResponseEntity<String> newUserOD(@RequestBody UserODDTO dto) {
        try {
            service.newUser(dto, "OD");
            return new ResponseEntity<>("Registered new user!", HttpStatus.OK);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/registration/pup")
    public ResponseEntity<String> newUserPUP(@RequestBody UserPUPDTO dto) {
        try {
            service.newUser(dto, "PUP");
            return new ResponseEntity<>("Registered new user!", HttpStatus.OK);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/registration/picture/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPicture(
            @PathVariable String email,
            @RequestParam("image") @Nullable MultipartFile file) {
        try{
            if(file != null && !file.isEmpty()){
                service.savePicture(file, email);
                return new ResponseEntity<String>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/registration/picture/pup/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPictures(
            @PathVariable String email,
            @RequestParam("image") @Nullable List<MultipartFile> fileList) {
        try{
            if(fileList != null && !fileList.isEmpty()){
                service.savePictures(fileList, email);
                return new ResponseEntity<String>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
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
