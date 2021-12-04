package app.doctorspace.doctorspace.repository;

import app.doctorspace.doctorspace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
