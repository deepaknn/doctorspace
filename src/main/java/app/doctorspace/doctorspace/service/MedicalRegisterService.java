package app.doctorspace.doctorspace.service;

import app.doctorspace.doctorspace.entity.MedicalRegister;
import app.doctorspace.doctorspace.entity.MedicalRegisterRequest;
import app.doctorspace.doctorspace.repository.MedicalRegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@EnableScheduling
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
                }
            }
        }
        return medicalRegister;
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 300000)
    public void loadRecords(){
        List<MedicalRegister> medicalRegisterList = medicalRegisterRepository.findTop5ByMedicalRegisterFetched(false);
        List<MedicalRegisterRequest> medicalRegisterRequestList = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now();
        medicalRegisterList.parallelStream().forEach(medicalRegister -> {
            try {
                MedicalRegisterRequest medicalRegisterRequest = MedicalRegisterRequest.builder()
                        .doctorId(String.valueOf(medicalRegister.getDoctorId()))
                        .regdNoValue(medicalRegister.getRegistrationNo())
                        .build();
                MedicalRegister medicalRegisterFetched = fetchAndLoad(medicalRegisterRequest);
                medicalRegisterFetched.setId(medicalRegister.getId());
                medicalRegisterRepository.save(medicalRegisterFetched);
                medicalRegisterRequestList.add(medicalRegisterRequest);
                System.out.println("***** Started fetch for record no : " + medicalRegisterRequestList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("Completed at : " + start + " ---> " + LocalDateTime.now());
    }
}
