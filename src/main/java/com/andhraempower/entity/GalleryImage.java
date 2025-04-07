package com.andhraempower.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="gallery_images")
public class GalleryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(name = "deleted")
    private Boolean deleted = false; // Soft delete flag

}