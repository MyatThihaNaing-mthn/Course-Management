package sg.edu.nus.iss.CAPS_CA_Team9.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.service.CourseService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.LecturerService;

@Controller
@RequestMapping(path = "/course")
public class CourseController {
    @Autowired 
    CourseService courseService;
    @Autowired 
    LecturerService lecturerService;
 
    @RequestMapping(path = "/list")
    public String getAllCourse(Model model) {
        List<Course> course = courseService.findall();
        model.addAttribute("list", course);
        return "CourseView";
    }
    @GetMapping("/addnew")
    public String addCourse(Model model) {
        Course course = new Course();
        List<Lecturer> lecturers = lecturerService.findAllLecturers();
        model.addAttribute("lecturers", lecturers);
        model.addAttribute("courseform", course);
        return "AddNewCourse";
    }
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute("courseform") Course course,@RequestParam("lecturerId") String lecturerId, BindingResult result) {
        Course thiscourse = courseService.createCourse(course);
        Lecturer selectedLecturer = lecturerService.getLecturerById(lecturerId);
        // Given the required SQL Constraint we need to fill up the Ids for both, and here is where we assign the course to a lecturer.
        List<Course> courses = selectedLecturer.getCourses();
        courses.add(thiscourse);
        selectedLecturer.setCourses(courses);
        return "redirect:/course/list";
    }
}
