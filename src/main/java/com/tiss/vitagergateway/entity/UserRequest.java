package com.tiss.vitagergateway.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "user-request")
public class UserRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "businessName", nullable = false)
    private String businessName;

    @Column(name = "postalcode", nullable = false)
    private long postalCode;

    @Column(name = "nationalcode", nullable = false)
    private String nationalCode;

    @Column(name = "nationalId", nullable = true)
    private String nationalId ;

    @Column(name = "mobile", nullable = false)
    private String mobile;

    @Column(name = "realLegal", nullable = false)
    private Boolean realLegal ;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "created_user", nullable = true)
    public int created_user ;

    @Column(name = "date", unique = true, nullable = false)
    private Date date;

    @Column(name = "success", nullable = false)
    private Boolean success;
}
