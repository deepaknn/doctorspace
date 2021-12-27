package app.doctorspace.doctorspace.repository;

import app.doctorspace.doctorspace.entity.MedicalRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRegisterRepository extends JpaRepository<MedicalRegister, Long> {

    List<MedicalRegister> findTop10000ByMedicalRegisterFetched(Boolean medicalRegisterFetched);
}
