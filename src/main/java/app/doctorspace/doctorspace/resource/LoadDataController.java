package app.doctorspace.doctorspace.resource;

import app.doctorspace.doctorspace.entity.MedicalRegister;
import app.doctorspace.doctorspace.entity.MedicalRegisterRequest;
import app.doctorspace.doctorspace.repository.MedicalRegisterRepository;
import app.doctorspace.doctorspace.service.MedicalRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class LoadDataController {

    @Autowired
    MedicalRegisterService medicalRegisterService;

    @Autowired
    MedicalRegisterRepository medicalRegisterRepository;

    @PostMapping("/load/data")
    public ResponseEntity<?> loadData(@RequestBody List<MedicalRegister> medicalRegisterList) {
        medicalRegisterService.loadData(medicalRegisterList);
        return ResponseEntity.ok("Data loaded successfully.");
    }

    @PostMapping("/fetch/medical-register")
    public ResponseEntity<?> fetchMedicalRegister(@RequestBody List<MedicalRegisterRequest> medicalRegisterRequestList) {
        asyncFetchMedicalRegister(medicalRegisterRequestList);
        return ResponseEntity.ok("Fetch Success");
    }

    @Async
    public void asyncFetchMedicalRegister(List<MedicalRegisterRequest> medicalRegisterRequestList) {
        LocalDateTime start = LocalDateTime.now();
        List<MedicalRegister> medicalRegisterList = new ArrayList<>();
        medicalRegisterRequestList.parallelStream().forEach(medicalRegisterRequest -> {
            medicalRegisterList.add(medicalRegisterService.fetchAndLoad(medicalRegisterRequest));
            System.out.println("***** Started fetch for record no : " + medicalRegisterList.size() + " : " + medicalRegisterRequest.getDoctorId() + " : " + medicalRegisterRequest.getRegdNoValue());
        });
        //medicalRegisterRepository.saveAll(medicalRegisterList);
        System.out.println("Completed at : " + start + " ---> " + LocalDateTime.now());
    }
}
