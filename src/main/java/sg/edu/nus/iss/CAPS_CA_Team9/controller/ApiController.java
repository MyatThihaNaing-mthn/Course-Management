package sg.edu.nus.iss.CAPS_CA_Team9.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;
import sg.edu.nus.iss.CAPS_CA_Team9.service.*;
//import sg.edu.nus.iss.CAPS_CA_Team9.util.GenerateUtil;

import java.util.Date;
import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiController {
    @Autowired
    CourseServiceImpt courseService;
    @Autowired
    LecturerServiceImpt lecturerService;
    @Autowired
    StudentServiceImpt studentService;

    @GetMapping("/calStudentMonthly")
    public List<Integer> calStudentMonthly() {
        return studentService.calculateMonthlyStudentCount();
    }

    @GetMapping("/calCourseByDate")
    public ResponseEntity<List<Course>> getCoursesByDateRange(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<Course> courses = courseService.findCoursesByDateRange(startDate, endDate);
        return ResponseEntity.ok(courses);
    }
}