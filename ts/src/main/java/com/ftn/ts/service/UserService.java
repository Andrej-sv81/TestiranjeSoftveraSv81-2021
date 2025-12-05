package com.ftn.ts.service;

import com.ftn.ts.dto.UserDTOMapper;
import com.ftn.ts.dto.UserODDTO;
import com.ftn.ts.dto.UserPUPDTO;
import com.ftn.ts.exceptions.ActivationExpiredException;
import com.ftn.ts.exceptions.NotActivatedException;
import com.ftn.ts.model.*;
import com.ftn.ts.repository.AdminRepository;
import com.ftn.ts.repository.UserODRepository;
import com.ftn.ts.repository.UserPUPRepository;
import com.ftn.ts.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserODRepository userODRepository;
    @Autowired
    private UserPUPRepository userPUPRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private EmailService emailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public boolean hasRole(UserDetails userDetails, String role) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(role));
    }

    public UserOD getUserByEmailOD(String email){
        return userODRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(email));
    }

    public UserPUP getUserByEmailPUP(String email){
        return userPUPRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(email));
    }

    public Optional<? extends BaseUser> login(String name) throws NotActivatedException {
        UserDetails userDetails = loadUserByUsername(name);
        if (hasRole(userDetails, "ROLE_USER_OD")){
            var user = userODRepository.findByEmail(name);
            if(user.get().isActivated()){
                return user;
            }else{
                throw new NotActivatedException(user.get().getEmail());
            }
        }
        else if (hasRole(userDetails, "ROLE_USER_PUP")){
            return userPUPRepository.findByEmail(name);
        }
        return Optional.empty();
    }

    public void newUser(Object obj, String type) throws MessagingException, IOException {
       if(type.equals("OD")) {
           UserODDTO dto = (UserODDTO) obj;
           var user = UserDTOMapper.dtoToOD(dto);
           sendMail(dto, "Account verification.");
           user.setActivated(false);
           user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
           userODRepository.save(user);
       }
       else if (type.equals("PUP")) {
           UserPUPDTO dto = (UserPUPDTO) obj;
           var user = UserDTOMapper.dtoToPUP(dto);
           userPUPRepository.save(user);
       }
    }

    public void activateUser(String newUserMail) throws ActivationExpiredException {
        var user = getUserByEmailOD(newUserMail);
        var expires = user.getCreated().toLocalDateTime().plusDays(1);
        if (expires.isAfter(LocalDateTime.now())){
            user.setActivated(true);
            userODRepository.save(user);
        }else{
            throw new ActivationExpiredException("Activation expired");
        }
    }
    public void sendMail(UserODDTO dto, String subject) throws MessagingException, IOException {
        var mail = new EmailDetails();
        mail.setRecipient(dto.getEmail());
        mail.setSubject(subject);
        emailService.sendSimpleMail(mail, dto.getName());
        LOGGER.info("✅ Email sent successfully. At: " + dto.getEmail());
    }
    public void savePicture(MultipartFile file, String userEmail) throws IOException {
        var user = getUserByEmailOD(userEmail);
        var picture = new Picture();
        picture.setName(file.getOriginalFilename());
        picture.setData(file.getBytes());
        user.setPicture(picture);
        userODRepository.save(user);
    }

    public void savePictures(List<MultipartFile> fileList, String email) throws  IOException {
        var user = getUserByEmailPUP(email);
        List<Picture> pictures = new ArrayList<>();
        for (MultipartFile multipartFile : fileList) {
            Picture picture = convertToPicture(multipartFile);
            pictures.add(picture);
        }
        user.setPictures(pictures);
        userPUPRepository.save(user);
    }

    public Picture convertToPicture(MultipartFile file) throws IOException {
        Picture picture = new Picture();
        picture.setName(file.getOriginalFilename());
        picture.setData(file.getBytes());
        return picture;
    }

    public void unlockAdmin(String email) {
        var admin = adminRepository.getAdminByEmail(email);
        admin.unlock();
        adminRepository.save(admin);
    }

    public boolean isSuperAdminUnlocked(String email) {
        var admin = adminRepository.getAdminByEmail(email);
        return admin.isUnlocked();
    }
}
