package com.ftn.ts.controller;

import com.ftn.ts.dto.UserODDTO;
import com.ftn.ts.dto.UserPUPDTO;
import com.ftn.ts.exceptions.ActivationExpiredException;
import com.ftn.ts.exceptions.NotActivatedException;
import com.ftn.ts.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/login")
    public ResponseEntity<String> login(Principal principal) throws NotActivatedException {
        var user = service.login(principal.getName());
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/registration/")
    public ResponseEntity<String> newUserOD(@Valid @RequestBody UserODDTO dto) throws MessagingException, IOException {
        service.newUser(dto, "OD");
        return new ResponseEntity<>("Registered new user!", HttpStatus.OK);
    }

    @PostMapping("/registration/pup")
    public ResponseEntity<String> newUserPUP(@Valid @RequestBody UserPUPDTO dto) throws MessagingException, IOException {
        service.newUser(dto, "PUP");
        return new ResponseEntity<>("Registered new user!", HttpStatus.OK);
    }

    @PostMapping(value = "/registration/picture/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPicture(
            @PathVariable String email,
            @RequestParam("image") @Nullable MultipartFile file) throws IOException {
        if(file != null && !file.isEmpty()){
            service.savePicture(file, email);
            return new ResponseEntity<String>("Picture saved successfully!",HttpStatus.OK);
        }else{
            throw new FileUploadException("File upload failed!");
        }
    }

    @PostMapping(value = "/registration/picture/pup/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPictures(
            @PathVariable String email,
            @RequestParam("images") @Nullable List<MultipartFile> fileList) throws IOException {
        if(fileList != null && !fileList.isEmpty()){
            service.savePictures(fileList, email);
            return new ResponseEntity<String>("Pictures saved successfully!", HttpStatus.OK);
        }else{
            throw new FileUploadException("File upload failed!");
        }
    }

    @GetMapping("/registration/verification/{newUserMail}")
    public ResponseEntity<String> confirm(@PathVariable String newUserMail) throws ActivationExpiredException {
        service.activateUser(newUserMail);
        var message = "Account with email " + newUserMail + " activated. Proceed to our page and login.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/unlockAdmin/{email}")
    public ResponseEntity<String> unlockSuperAdmin(@PathVariable String email) {
        service.unlockAdmin(email);
        return new ResponseEntity<>("Admin unlocked!", HttpStatus.OK);
    }

    @GetMapping("/login/firstTime/{email}")
    public ResponseEntity<Boolean> isSuperAdminUnlocked(@PathVariable String email) {
        return new ResponseEntity<>(service.isSuperAdminUnlocked(email), HttpStatus.OK);
    }

}
