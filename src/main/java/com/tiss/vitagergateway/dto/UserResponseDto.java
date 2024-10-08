package com.tiss.vitagergateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String businessName;
    private long postalCode;
    private String nationalCode;
    private String nationalId ;
    private String mobile;
    private Boolean realLegal ;
    private String address;
    public int created_user ;
    private Date date;
    private Boolean success;
}
