package com.andhraempower.dto;


import lombok.Data;

@Data
public class CommitteeMembersDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String fatherName;

    private String phoneNumber;

    private String email;

    private String address;

    private String recordType;

    private Integer villageId;
}
