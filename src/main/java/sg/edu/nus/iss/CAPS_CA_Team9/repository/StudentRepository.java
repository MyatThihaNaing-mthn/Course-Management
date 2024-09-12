package sg.edu.nus.iss.CAPS_CA_Team9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;

//import java.time.LocalDateTime;
import java.util.Date;


public interface StudentRepository extends JpaRepository<Student,String> {
    Student findTopByOrderByIdDesc();

    int countByEnrollmentDateBetween(Date atStartOfDay, Date atTime);
}
