package sg.edu.nus.iss.CAPS_CA_Team9.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.userdetails.UserDetails;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Admin;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;
//import sg.edu.nus.iss.CAPS_CA_Team9.service.LoginServiceImpl;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private LoginServiceImpl loginService;

    public CustomAuthenticationSuccessHandler(LoginServiceImpl loginService) {
        this.loginService = loginService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        Object user = loginService.findUserByUsername(username);

        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            session.setAttribute("role", "Admin");
            session.setAttribute("username", admin.getAdminName());
            session.setAttribute("id", username);
            response.sendRedirect("/admin");
        } else if (user instanceof Lecturer) {
            Lecturer lecturer = (Lecturer) user;
            session.setAttribute("role", "Lecturer");
            session.setAttribute("username", lecturer.getName());
            session.setAttribute("id", username);
            response.sendRedirect("/lecturer/modules");
        } else if (user instanceof Student) {
            Student student = (Student) user;
            session.setAttribute("role", "Student");
            session.setAttribute("username", (student.getFirstName() + " " + student.getLastName()));
            session.setAttribute("id", username);
            response.sendRedirect("/student/index");
            System.out.println(username);
        } else {
            //unknown type...
            session.invalidate();
            response.sendRedirect("/login");
        }
    }
}
