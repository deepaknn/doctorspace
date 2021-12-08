package app.doctorspace.doctorspace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRegisterRequest {

    private String doctorId;
    private String regdNoValue;
}
