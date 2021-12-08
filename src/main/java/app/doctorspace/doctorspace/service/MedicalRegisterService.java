package app.doctorspace.doctorspace.service;

import app.doctorspace.doctorspace.entity.MedicalRegister;
import app.doctorspace.doctorspace.entity.MedicalRegisterRequest;
import app.doctorspace.doctorspace.repository.MedicalRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MedicalRegisterService {

    @Autowired
    MedicalRegisterRepository medicalRegisterRepository;

    public void loadData(List<MedicalRegister> medicalRegisterList) {
        medicalRegisterRepository.saveAll(medicalRegisterList);
    }

    public MedicalRegister fetchAndLoad(MedicalRegisterRequest medicalRegisterRequest){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MedicalRegisterRequest> medHttpReq = new HttpEntity<>(medicalRegisterRequest);
        MedicalRegister medicalRegister = restTemplate.postForObject("https://www.nmc.org.in/MCIRest/open/getDataFromService?service=getDoctorDetailsByIdImr", medHttpReq, MedicalRegister.class);
        return medicalRegister;
    }
}
