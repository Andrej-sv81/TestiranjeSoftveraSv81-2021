package com.ftn.ts.service;

import com.ftn.ts.dto.UserDTOMapper;
import com.ftn.ts.dto.UserODDTO;
import com.ftn.ts.model.EmailDetails;
import com.ftn.ts.model.UserOD;
import com.ftn.ts.repository.UserODRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountExpiredException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class UserODService implements UserDetailsService {
    @Autowired
    UserODRepository userODRepository;
    @Autowired
    EmailService emailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserODService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userODRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException(username));

    }
    public UserOD getUserByEmail(String email){
        return userODRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(email));
    }

    public UserODDTO newUser(UserODDTO dto) {
        var user = UserDTOMapper.dtoToUser(dto);
        sendMail(dto, "Account verification.");
        user.setActivated(false);
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        userODRepository.save(user);
        return dto;
    }

    public void activateUser(String newUserMail) throws AccountExpiredException{
        var user = getUserByEmail(newUserMail);
        var expires = user.getCreated().toLocalDateTime().plusDays(1);
        if (expires.isAfter(LocalDateTime.now())){
            user.setActivated(true);
            userODRepository.save(user);
        }else{
            throw new AccountExpiredException();
        }
    }

    public void sendMail(UserODDTO dto, String subject) {
        var mail = new EmailDetails();
        mail.setRecipient(dto.getEmail());
        mail.setSubject(subject);
        emailService.sendSimpleMail(mail, dto.getName());
        LOGGER.info("✅ Email sent successfully. At: " + dto.getEmail());
    }
}
