package com.ftn.ts.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserODDTO {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String address;
    private String phone;
}
