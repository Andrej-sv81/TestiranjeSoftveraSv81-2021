package com.ftn.ts.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
