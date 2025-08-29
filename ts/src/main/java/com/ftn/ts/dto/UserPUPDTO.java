package com.ftn.ts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPUPDTO {
    private String email;
    private String password;
    private String name;
    private String address;
    private String phone;
    private String description;
}
