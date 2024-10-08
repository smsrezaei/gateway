package com.tiss.vitagergateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String businessName;
    private long postalCode;
    private String nationalCode;
    private String nationalId ;
    private String mobile;
    private Boolean realLegal ;
    private String address;
}
