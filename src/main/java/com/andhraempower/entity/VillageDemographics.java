package com.andhraempower.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "village_demographics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VillageDemographics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "village_id")
    private Integer villageId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "village_id")
    private List<PopulationVillage> populations = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "village_id")
    private List<UnemployeeYouthVillage> unEmployedYouthVillage = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "village_id")
    private List<EmployeeYouthVillage> employedYouthVillage = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "village_id")
    private List<OccupationVillage> occupations = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "village_id")
    private List<LandUtilizationVillage> landUtilizationVillage = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "village_id")
    private List<CultivationCropsVillage> cultivationCropsVillage = new ArrayList<>();


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "village_id")
    private List<LiveStockVillage> liveStockVillages = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "village_id")
    private List<InstitutionsVillage> institutionsVillages = new ArrayList<>();


    @Column(name = "no_of_houses")
    private Integer noOfHouses;

    @Column(name = "total_population")
    private Integer totalPopulation;

    @Column(name = "adult_male_population")
    private Integer adultMalePopulation;

    @Column(name = "adult_female_population")
    private Integer adultFemalePopulation;

    @Column(name = "child_male_population")
    private Integer childMalePopulation;

    @Column(name = "child_female_population")
    private Integer childFemalePopulation;

    @Column(name = "sc_male")
    private Integer scMale;

    @Column(name = "sc_female")
    private Integer scFemale;

    @Column(name = "st_male")
    private Integer stMale;

    @Column(name = "st_female")
    private Integer stFemale;

    @Column(name = "bc_male")
    private Integer bcMale;

    @Column(name = "bc_female")
    private Integer bcFemale;

    @Column(name = "oc_male")
    private Integer ocMale;

    @Column(name = "oc_female")
    private Integer ocFemale;

    @Column(name = "other_male")
    private Integer otherMale;

    @Column(name = "other_female")
    private Integer otherFemale;

    @Column(name = "area")
    private Float area;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "pin_code")
    private Integer pinCode;

    @Column(name = "boundaries_village")
    private String boundariesVillage;

    @Column(name = "above60_male")
    private Integer above60Male;

    @Column(name = "above60_female")
    private Integer above60Female;

    @Column(name = "geographical_area")
    private String geographicalArea;

}