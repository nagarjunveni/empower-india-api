package com.andhraempower.dto;
import com.andhraempower.entity.Donar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonarDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private Double amount;
    private String category;
    private Long villageId;
    private String villageName;
    private Long mandalId;
    private String mandalName;
    private Long districtId;
    private String districtName;
    private String memoryOf;
    private String modeOfPayment;
    private String description;
    private String imageUrl;

    public DonarDto(Long id, String firstName, String lastName, String phoneNumber, String email, String description,
                    String address,
                    Long villageId, String villageName,
                    Long mandalId, String mandalName,
                    Long districtId, String districtName,
                    Double amount, String category) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.description = description;
        this.address = address;
        this.villageId = villageId;
        this.villageName = villageName;
        this.mandalId = mandalId;
        this.mandalName = mandalName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.amount = amount;
        this.category = category;
    }

    public Donar fromDto(){
        Donar donar = new Donar();
        donar.setId(this.id);
        donar.setFirstName(this.firstName);
        donar.setLastName(this.lastName);
        donar.setPhoneNumber(this.phoneNumber);
        donar.setEmail(this.email);
        donar.setAddress(this.address);
        donar.setDescription(this.description);
        return donar;
    }

}

