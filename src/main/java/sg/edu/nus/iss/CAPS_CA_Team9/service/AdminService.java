package sg.edu.nus.iss.CAPS_CA_Team9.service;

//import org.springframework.stereotype.Service;
import java.util.List;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Admin;


public interface AdminService {
   public List<Admin> findAll();
   public Admin createAdmin(Admin admin);
   public Admin updateAdmin(Admin admin);
   public void deleteAdmin(Admin admin);
   public Admin getLastAdmin();
   public Admin getAdminById(String id);
   
}
