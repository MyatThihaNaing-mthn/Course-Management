package sg.edu.nus.iss.CAPS_CA_Team9.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.CAPS_CA_Team9.exception.DivideByZeroException;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;
import sg.edu.nus.iss.CAPS_CA_Team9.service.CourseService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.EmailService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentCourseService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private EmailService mailService;

    @GetMapping("/index")
    public String index(Model model,HttpSession session){
        String id = (String)session.getAttribute("id");
        List<Course> allCourses = courseService.findall();
        List<StudentCourse> stdCourses = studentCourseService.findByStudentId(id);
        List<Integer> removedCourses = new ArrayList<>();
        List<Integer> regitCourses = new ArrayList<>();
        List<Integer> completeCourses = new ArrayList<>();
        for(StudentCourse s: stdCourses){
            if(s.getEnrollStatus() == 0 &&s.getScore() == null){
                regitCourses.add(s.getCourseId());
            }
            else if(s.getEnrollStatus() == 0 && s.getScore() != 0){
                completeCourses.add(s.getCourseId());
            }
            else if(s.getEnrollStatus() == 1){
                removedCourses.add(s.getCourseId());
            }
        }
        model.addAttribute("currentDate", Calendar.getInstance().getTime());
        model.addAttribute("courseList", allCourses);
        model.addAttribute("routePath", "All Courses");
        model.addAttribute("completeCourses", completeCourses);
        model.addAttribute("regitCourses", regitCourses);
        model.addAttribute("removedCourses", removedCourses);
        return "student/index";
    }
    
    // get all available list
    @GetMapping("/courseList")
    public String getAvailableCourses(Model model,HttpSession session){
        String id = (String)session.getAttribute("id");
        List<StudentCourse> studentCourse = studentCourseService.findByStudentId(id);
        Date dateNow = Calendar.getInstance().getTime();
        List<Course> courseList = new ArrayList<>();

        if(studentCourse != null && !studentCourse.isEmpty()){
            List<Integer> courseIdList = new ArrayList<>();
            for(StudentCourse stdCourse: studentCourse){
                courseIdList.add(stdCourse.getCourseId());
            }
            courseList = courseService.findAvailableCourseList(courseIdList).stream().filter(c -> c.getStartDate().after(dateNow) && c.getStartDate() != null && c.getCurrentCount() < c.getSize()).collect(Collectors.toList());
        }
        else{
            courseList = courseService.findall().stream().filter(c -> c.getStartDate().after(dateNow) && c.getStartDate() != null && c.getCurrentCount() < c.getSize()).collect(Collectors.toList());
        }
        
        model.addAttribute("studentCourse", studentCourse);
        model.addAttribute("courseList",courseList);
        model.addAttribute("routePath", "Available Courses");
        return "student/courseList";
        
    }

    @GetMapping("/enroll/{id}")
    public String getCourse(Model model,@PathVariable("id") int courseId, HttpSession session){
        String id = (String)session.getAttribute("id");
        Course course = courseService.findCourse(courseId);
        List<StudentCourse> stdCourses = studentCourseService.findByStudentId(id).stream().filter(s -> s.getCourseId() == courseId).collect(Collectors.toList());
        if(course != null){
            List<Lecturer> courseLecturers = course.getLecturers();
            model.addAttribute("course", course);
            model.addAttribute("courseLecturers",courseLecturers);
            model.addAttribute("studentCourses", stdCourses);
            model.addAttribute("currentDate", Calendar.getInstance().getTime());
            model.addAttribute("routePath", "Available Courses");
        }
        return "student/courseEnroll";
    }

    @PostMapping("/enroll/{id}")
    public String saveCourse(Model model,@PathVariable("id") int courseId, HttpSession session){
        String id = (String)session.getAttribute("id");
        Course course = courseService.findCourse(courseId);
        Student student = studentService.findByStudentId(id);
        if(course.getCurrentCount() >= course.getSize()){
        model.addAttribute("msg", "Failed to Enroll, Course is full!!!");
        model.addAttribute("routePath", "Available Courses");
        return "redirect:/student/courseList";
        }
        String lecture= "";
        for(Lecturer l:course.getLecturers()){
            lecture = l.getName()+"\n"+l.getEmail()+"\n";
        }
        String text = "Course Name:"+course.getName() + "\nDescription:"+course.getDescription()+"\nLectures:"+lecture+"\nYour enrolled date: "+Calendar.getInstance().getTime();
        mailService.sendMail(student.getEmail(), "", "Success Enrolled!!!!", text);
        if(student != null){
            StudentCourse stdCourse = new StudentCourse();
            stdCourse.setCourse(course);
            stdCourse.setStudent(student);
            stdCourse.setCourseId(courseId);
            stdCourse.setStudentId(id);
            // double score = 0.0;
            // stdCourse.setScore(score);
            Integer enrollStatus = 0;
            stdCourse.setEnrollStatus(enrollStatus);
            stdCourse.setEnrollDate(Calendar.getInstance().getTime());
            studentCourseService.createStudentCourse(stdCourse);
            course.setCurrentCount(course.getCurrentCount()+1);
            courseService.updateCourse(course);
        }
        model.addAttribute("msg", "Successfully Enrolled!!!");
        model.addAttribute("routePath", "Available Courses");
        return "redirect:/student/courseList";
        
    }

    @GetMapping("/registeredCourses")
    public String getRegisteredCourses(Model model, HttpSession session){
        String id = (String)session.getAttribute("id");
        List<StudentCourse> registCourseList = studentCourseService.findByStudentIdWithoutRemoved(id);
        List<Integer> courseIdList = new ArrayList<>();
        if(registCourseList != null){
            for(StudentCourse stdCourse: registCourseList){
                courseIdList.add(stdCourse.getCourseId());
            }
        }
        List<Course> courseList = courseService.findRegisteredCourseList(courseIdList);
        model.addAttribute("courseList",courseList);
        Date dateNow = Calendar.getInstance().getTime();
        model.addAttribute("currentDate", dateNow);
        model.addAttribute("routePath", "My Courses");
        return "student/registeredCourse";
        
    }

    @GetMapping("/grade")
    public String viewGpaAndGrade(Model model, HttpSession session){
        String id = (String)session.getAttribute("id");
        // only display courses the student is not kicked from. 
        List<StudentCourse> registCourseList = studentCourseService.findByStudentId(id).stream().filter(c -> c.getEnrollStatus() == 0).filter(c -> c.getScore()!= null && c.getScore() != 0).collect(Collectors.toList());
        List<Integer> courseIdList = new ArrayList<>();
        for(StudentCourse stdCourse: registCourseList){
            courseIdList.add(stdCourse.getCourseId());
        }
        List<Course> courseList = courseService.findRegisteredCourseList(courseIdList);
        double gpa = calculateGPA(registCourseList,courseList);
        // find the student and update his GPA accordingly
        Student student = studentService.findByStudentId(id);
        student.setGpa(gpa);
        // save it in the db
        studentService.updateStudent(student);
        model.addAttribute("gpa", gpa);
        model.addAttribute("registCourseList", registCourseList);
        model.addAttribute("routePath", "GPA & Grades");
        model.addAttribute("msg", "You haven't reveived any GPA and grades yet.");
        return "student/viewGPA";
    }

    @GetMapping("/removeCourses")
    public String viewRemovedCourse(Model model, HttpSession session){
        String id = (String)session.getAttribute("id");
        List<StudentCourse> removedCourses = studentCourseService.findByEnrollStatus(1).stream().filter(r -> r.getStudentId().equals(id)).collect(Collectors.toList());
        if(removedCourses != null && !removedCourses.isEmpty()){
            model.addAttribute("removedCourses", removedCourses);
            model.addAttribute("msg", "");
        }else{
            model.addAttribute("msg", "There is no removed courses.");
        }
        System.out.println();
        model.addAttribute("routePath", "My Courses");
        return "student/courseRemove";
        
    }

    public double calculateGPA(List<StudentCourse> registCourseList, List<Course> courseList){
        double gpa,total = 0.0,credit=0.0,gradePoint;
        try{
            for(Course c:courseList){
                List<StudentCourse> course = registCourseList.stream().filter(r -> r.getCourseId() == c.getId() && r.getScore() != 0).collect(Collectors.toList());
                if(!course.isEmpty()){
                    gradePoint = getGradePoint(course.get(0).getScore());
                    total += gradePoint * c.getCredits() ;
                    credit += c.getCredits();
                }
            }
            if(credit == 0){
                throw new DivideByZeroException();
            }else{
                gpa = total / credit;
            }
        }catch(DivideByZeroException e){
            gpa = 0.0;
        }
        
        return gpa;
    }

    public double getGradePoint(double score){
        if(score >= 80)
            return 5.0;
        else if(score >= 75)
            return 4.5;
        else if(score >= 70)
            return 4.0;
        else if(score >= 65)
            return 3.5;
        else if(score >= 60)
            return 3.0;
        else if(score >= 55)
            return 2.5;
        else if(score >= 50)
            return 2.0;
        else if(score >= 45)
            return 1.5;
        else if(score >= 40)
            return 1.0;
        else
            return 0;
    }

    // Added changing of password or other student details
    @GetMapping(value = "/accountDetails")
    public String viewAccountDetails(Model model, HttpSession session) {
        String studentId = (String) session.getAttribute("id");
        Student student = studentService.findByStudentId(studentId);
        model.addAttribute("student", student);
        model.addAttribute("routePath", "profile");
        return "student/accountDetails";
    }

    @PostMapping(value = "/changeAccountDetails")
    public String updateAccountDetails(Model model,@ModelAttribute("student") Student student) {
        studentService.updateStudent(student);
        model.addAttribute("routePath", "profile");
        return "student/accountDetails";
    }

}
