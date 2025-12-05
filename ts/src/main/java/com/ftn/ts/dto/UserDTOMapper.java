package com.ftn.ts.dto;

import com.ftn.ts.model.UserOD;
import com.ftn.ts.model.UserPUP;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserDTOMapper {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static UserOD dtoToOD(UserODDTO dto) {

        var user = new UserOD();

        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setPicture(null);
        return user;
    }

    public static UserPUP dtoToPUP(UserPUPDTO dto) {
        var user = new UserPUP();

        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setDescription(dto.getDescription());
        user.setPictures(null);
        return user;
    }
}