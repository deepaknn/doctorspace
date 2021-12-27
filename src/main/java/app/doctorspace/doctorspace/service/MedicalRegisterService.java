package app.doctorspace.doctorspace.service;

import app.doctorspace.doctorspace.entity.MedicalRegister;
import app.doctorspace.doctorspace.entity.MedicalRegisterRequest;
import app.doctorspace.doctorspace.repository.MedicalRegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MedicalRegisterService {

    @Autowired
    MedicalRegisterRepository medicalRegisterRepository;

    public void loadData(List<MedicalRegisterRequest> medicalRegisterRequestList) {
        List<MedicalRegister> medicalRegisterList = new ArrayList<>();
        medicalRegisterRequestList.parallelStream().forEach(medicalRegisterRequest -> {
            MedicalRegister medicalRegister = MedicalRegister.builder()
                    .doctorId(Long.valueOf(medicalRegisterRequest.getDoctorId()))
                    .registrationNo(medicalRegisterRequest.getRegdNoValue())
                    .build();
            medicalRegisterRepository.save(medicalRegister);
            medicalRegisterList.add(medicalRegister);
            log.info("Inserted record no : " + medicalRegisterList.size());
        });
//        for (int i = 0; i < 1000; i++) {
//            medicalRegisterList.add(MedicalRegister.builder()
//                    .doctorId(Long.valueOf(medicalRegisterRequestList.get(i).getDoctorId()))
//                    .registrationNo(medicalRegisterRequestList.get(i).getRegdNoValue())
//                    .build());
//            log.info("Inserted record no : " + medicalRegisterList.size());
//        }
//        medicalRegisterRepository.saveAll(medicalRegisterList);

    }

    public MedicalRegister fetchAndLoad(MedicalRegisterRequest medicalRegisterRequest){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MedicalRegisterRequest> medHttpReq = new HttpEntity<>(medicalRegisterRequest);
        MedicalRegister medicalRegister = null;
        try {
            medicalRegister = restTemplate.postForObject("https://www.nmc.org.in/MCIRest/open/getDataFromService?service=getDoctorDetailsByIdImr", medHttpReq, MedicalRegister.class);
            medicalRegister.setMedicalRegisterFetched(true);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                medicalRegister = restTemplate.postForObject("https://www.nmc.org.in/MCIRest/open/getDataFromService?service=getDoctorDetailsByIdImr", medHttpReq, MedicalRegister.class);
                medicalRegister.setMedicalRegisterFetched(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    medicalRegister = restTemplate.postForObject("https://www.nmc.org.in/MCIRest/open/getDataFromService?service=getDoctorDetailsByIdImr", medHttpReq, MedicalRegister.class);
                    medicalRegister.setMedicalRegisterFetched(true);
                } catch (Exception exc) {
                    exc.printStackTrace();
                    medicalRegister.setDoctorId(Long.getLong(medicalRegisterRequest.getDoctorId()));
                    medicalRegister.setRegistrationNo(medicalRegisterRequest.getRegdNoValue());
                }
            }
        }
        medicalRegisterRepository.save(medicalRegister);
        return medicalRegister;
    }
}
