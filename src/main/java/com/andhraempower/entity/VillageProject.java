package com.andhraempower.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "village_project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VillageProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_category_id", nullable = false)
    private CategoryLookup projectCategory;

    @ManyToOne
    @JoinColumn(name = "project_type", nullable = false)
    private ProjectTypeLookup projectTypeLookup;

    @Column(name = "location")
    private String location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "project_estimation")
    private Double projectEstimation;

    @Column(name = "government_share")
    private Double governmentShare;

    @Column(name = "public_share")
    private Double publicShare;

    @Column(name = "is_new")
    private Boolean isNew;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "last_updated_by", nullable = false)
    private String lastUpdatedBy;

    @Column(name = "STATUS_CODE", nullable = false)
    private String statusCode;

    @ManyToOne
    @JoinColumn(name = "village_id", nullable = false)
    private VillageLookup village;

    @Column(name = "is_committee_formed")
    private boolean committeeAdded;

    @Column(name = "is_bank_account_added")
    private boolean bankAccountAdded;

    @Column(name = "is_estimation_completed")
    private boolean estimationCompleted;

    @Column(name = "estimate_start_date")
    private LocalDate estimateStartDate;

    @Column(name = "estimate_end_date")
    private LocalDate estimateEndDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] projectImage;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    @OneToMany
    @JoinColumn(name = "village_project_id")
    @Lazy
    private List<VillageProjectDonar> villageProjectDonars;

    @Column(name = "last_updated_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastUpdatedDate;
}
