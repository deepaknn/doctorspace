package app.doctorspace.doctorspace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medical_register")
public class MedicalRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_register_seq_generator")
    @SequenceGenerator(name = "medical_register_seq_generator", sequenceName = "medical_register_seq")
    private Long id;

    private Integer yearInfo;
    private String regDate;
    private Long doctorId;
    private String salutation;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNo;
    private String emailId;
    private String gender;
    private String bloodGroup;
    private String parentName;
    private String birthDate;
    private Boolean isNewDoctor;
    private Boolean checkExistingUser;
    private String birthDateStr;
    private String birthPlace;
    private String nationality;
    private String eligbleToVote;
    private String adharNo;
    private String uprnNo;
    private String doctorEducationId;
    private String college;
    private String doctorDegree;
    private String university;
    private String otherSubject;
    private String monthOfPass;
    private String yearOfPassing;
    private String smcId;
    private String registrationDate;
    private String registrationNo;
    private String smcName;
    private String homeAddress;
    private String officeAddress;
    private String address;
    private String addressLine1;
    private String addressLine2;
    private String economicStatus;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String photos;
    private String doctRegistrationNo;
    private String universityId_view;
    private String universityId;
    private String monthandyearOfPass;
    private String passoutCollege;
    private String collegeId;
    private String stateId;
    private String catagory;
    private String catagory_view;
    private String role;
    private String registrationDatePrevious;
    private String registrationNoPrevious;
    private String smcNamePrevious;
    private String uprnNoPrevious;
    private Boolean removedStatus;
    private String removedOn;
    private Boolean restoredStatus;
    private String restoredOn;
    private String remarks;
    private String regnNo;
    private String smcIds;
    private String trasanctionStatus;
    private String addlqual1;
    private String addlqualyear1;
    private String addlqualuniv1;
    private String addlqual2;
    private String addlqualyear2;
    private String addlqualuniv2;
    private String addlqual3;
    private String addlqualyear3;
    private String addlqualuniv3;
    private String patientfirstName;
    private String patientmiddleName;
    private String patientlastName;
    private String patientphoneNo;
    private String patientemailId;
    private String appealBy;
    private String altphone;
    private String landLineNo;
    private String patientlandLineNo;
    private String patientaltphone;
    private String picName;
    private String signatureName;
    private String stateMedicalCouncil;
    private String countryType;
    private String dateOfBirth;
    private String universityName;
    private String qualification;
    private String imrNumber;

}
