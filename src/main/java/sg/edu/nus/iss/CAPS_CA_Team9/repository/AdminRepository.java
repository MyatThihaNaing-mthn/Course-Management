package sg.edu.nus.iss.CAPS_CA_Team9.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,String>{
    Admin findTopByOrderByIdDesc();
}
