package sg.edu.nus.iss.CAPS_CA_Team9.controller;

import java.util.ArrayList;
import java.util.List;

//import javax.persistence.Id;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;
//import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourseId;
import sg.edu.nus.iss.CAPS_CA_Team9.service.CourseService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.LecturerService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentCourseService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentService;

@Controller
@RequestMapping(path = "/lecturer")
public class LecturerController {
    @Autowired
    private LecturerService lecturerService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentCourseService studentCourseService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/modules")
    public String getAllCourses(Model model, HttpSession session) {
        String lecturerId = (String) session.getAttribute("id");
        List<Course> course = lecturerService.getCoursesByLecturerId(lecturerId); 
        model.addAttribute("courses",course);
        model.addAttribute("activeTab", "modules");
        return "lecturer/LecturerViewCourse";   
    }

    @GetMapping("/viewStudentPerformance")
    public String getStudentPerformance(@RequestParam(value="id", required=false) String id, Model model) {
        List<StudentCourse> sc = new ArrayList<>();

        if(id != null && !id.isEmpty()) {
            sc = studentCourseService.findByStudentId(id);
            Student student = studentService.findByStudentId(id);
            if(student != null) {
                model.addAttribute("student", student);
            } else {
                model.addAttribute("errorMessage", "Student not found");
            }
        }
        
        model.addAttribute("studentCourse", sc);
        model.addAttribute("activeTab", "studentPerformance");
        return "lecturer/LecturerViewStudentPerf";
    }


    @GetMapping("/selectCourse")
    public String displayCourses(Model model, HttpSession session) {
        String lecturerId = (String) session.getAttribute("id");
        List<Course> courses = lecturerService.getCoursesByLecturerId(lecturerId);
        model.addAttribute("courses", courses);
        model.addAttribute("activeTab", "gradeCourse");
        return "lecturer/LecturerSelectCourse";
    }

    @RequestMapping(value = "/gradeCourse", method = {RequestMethod.GET, RequestMethod.POST})
    public String displayCourses(@RequestParam("courseId") int id ,Model model) {
        List<StudentCourse> studentcourses = studentCourseService.getAllCoursesByCourseId(id);
        Course course = courseService.findCourse(id);
        model.addAttribute("course", course);
        model.addAttribute("studentcourses", studentcourses);
        model.addAttribute("activeTab", "gradeCourse");
        return "lecturer/LecturerGradeACourse";
    }

    @PostMapping("/saveStudentCourses")
    public String saveStudentScore(Model model, @RequestParam("studentId") String studentId, @RequestParam("courseId") Integer courseId, @RequestParam("score") Double score) {
        StudentCourse studentCourse = studentCourseService.findByStudentCourseId(studentId,courseId);
        studentCourse.setScore(score);
        studentCourseService.createStudentCourse(studentCourse);
        model.addAttribute("activeTab", "gradeCourse");
        return "redirect:/lecturer/gradeCourse?courseId=" + courseId;
    }

    @GetMapping(value = "/viewCourseEnrolment")
    public String getCourseSelection(Model model, HttpSession session) {

        String lecturerId = (String) session.getAttribute("id");

        List<Course> courses = lecturerService.getCoursesByLecturerId(lecturerId);

        List<StudentCourse> studentCourses = new ArrayList<>();

        model.addAttribute("selectedCourse", null);
        model.addAttribute("lecturerCourses", courses);
        model.addAttribute("studentCourses", studentCourses);
        model.addAttribute("activeTab", "courseEnroll");

        return "lecturer/LecturerViewEnrolment"; 
    }


    @PostMapping(value = "/viewCourseEnrolment")
    public String viewCourseEnrolment(@RequestParam(value="id", required=true) Integer courseId, Model model, HttpSession session) {

        String lecturerId = (String) session.getAttribute("id");

        Course selectedCourse = courseService.findCourse(courseId);

        // Remove courses that student has been removed by Admins
        List<StudentCourse> studentCourses = studentCourseService.getAllCoursesByCourseId(courseId).stream().filter(s -> s.getEnrollStatus()==0).toList();

        List<Course> courses = lecturerService.getCoursesByLecturerId(lecturerId);

        model.addAttribute("selectedCourse", selectedCourse);
        model.addAttribute("lecturerCourses", courses);
        model.addAttribute("studentCourses", studentCourses);
        model.addAttribute("activeTab", "courseEnroll");

        return "lecturer/LecturerViewEnrolment"; 
    }

    @GetMapping(value = "/accountDetails")
    public String viewAccountDetails(Model model, HttpSession session) {
        String lecturerId = (String) session.getAttribute("id");
        Lecturer lecturer = lecturerService.getLecturerById(lecturerId);
        model.addAttribute("lecturer", lecturer);
        model.addAttribute("activeTab", "accountDetails");

        return "lecturer/accountDetails";
    }

    @PostMapping(value = "/changeAccountDetails")
    public String updateAccountDetails(Model model,@ModelAttribute("lecturer") Lecturer lecturer) {
        lecturerService.updateLecturer(lecturer);
        model.addAttribute("activeTab", "accountDetails");
        return "lecturer/accountDetails";
    }

    
}
