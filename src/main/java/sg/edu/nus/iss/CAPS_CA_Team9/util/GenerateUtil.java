package sg.edu.nus.iss.CAPS_CA_Team9.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Admin;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.service.AdminService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.LecturerService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentService;

@NoArgsConstructor
@Component
public class GenerateUtil {

    @Autowired
    StudentService studentService;
    @Autowired
    LecturerService lecturerService;
    @Autowired
    AdminService adminService;

    public String generateStuId() {

      String lastId = studentService.getLastStudent().getId();
      if (lastId == null) {
         return "00000001";
      }
      int number = Integer.parseInt(lastId.substring(1));
      number++; // Increment the number
      String incrementedNumber = String.format("%08d", number);
      return incrementedNumber;
   }

   public String generateLectId() {

      Lecturer lecturer = lecturerService.getLastLecturer();

      if (lecturer == null) {
         return "00000001";
      }
      String lastId = lecturer.getId();
      int number = Integer.parseInt(lastId.substring(1));
      number++; // Increment the number
      String incrementedNumber = String.format("%08d", number);
      return incrementedNumber;
   }

   public String generateAdminId(){
      Admin admin = adminService.getLastAdmin();
      
      if(admin == null){
         return "00000001";
      }
      String lastId = admin.getId();
      int number = Integer.parseInt(lastId.substring(1));
      number++;
      String incrementedNumber = String.format("%08d", number);
      return incrementedNumber;

   }
}