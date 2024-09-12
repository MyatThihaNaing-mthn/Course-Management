package sg.edu.nus.iss.CAPS_CA_Team9.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Admin;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;

@Service
public class LoginServiceImpl implements LoginService, UserDetailsService {

    @Autowired
    private AdminService adminService;
    @Autowired
    private LecturerService lecturerService;
    @Autowired
    private StudentService studentService;
    //@Autowired
    //private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Object findUserByUsername(String username) {
        char initial = Character.toUpperCase(username.charAt(0)); // Convert all to uppercase for switch case

        switch (initial) {
            case 'L':
                Lecturer lecturer = lecturerService.getLecturerById(username);
                if (lecturer != null) {
                    return lecturer;
                }
                break;
            case 'S':
                Student student = studentService.findByStudentId(username);
                if (student != null) {
                    return student;
                }
                break;
            case 'A':
                Admin admin = adminService.getAdminById(username);
                if (admin != null) {
                    return admin;
                }
                break;
            default:
                return null;
        }

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        char initial = Character.toUpperCase(username.charAt(0));

        switch (initial) {
            case 'L':
                Lecturer lecturer = lecturerService.getLecturerById(username);
                if (lecturer != null) {
                    return User.builder()
                            .username(lecturer.getId())
                            .password(lecturer.getPassword())
                            .authorities("Lecturer")
                            .build();
                }
                break;
            case 'S':
                Student student = studentService.findByStudentId(username);
                if (student != null) {
                    return User.builder()
                            .username(student.getId())
                            .password(student.getPassword())
                            .authorities("Student")
                            .build();
                }
                break;
            case 'A':
                Admin admin = adminService.getAdminById(username);
                if (admin != null) {
                    return User.builder()
                            .username(admin.getId())
                            .password(admin.getAdminPwd())
                            .authorities("Admin")
                            .build();
                }
                break;
            default:
                throw new UsernameNotFoundException("User not found: " + username);
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }

}