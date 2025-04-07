package com.andhraempower.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name="images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Lob
    private String data; // Base64 encoded image data

    private Timestamp createdAt;

    private Boolean deleted = false; // Soft delete flag

}