package com.coop.emailGateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSend {
    private String name;
    private String header;
    private String email;
    private String subject;
    private String body;


}
