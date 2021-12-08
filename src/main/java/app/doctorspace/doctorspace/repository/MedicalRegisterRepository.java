package app.doctorspace.doctorspace.repository;

import app.doctorspace.doctorspace.entity.MedicalRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRegisterRepository extends JpaRepository<MedicalRegister, Long> {
}
