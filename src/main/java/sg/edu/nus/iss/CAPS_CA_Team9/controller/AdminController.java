package sg.edu.nus.iss.CAPS_CA_Team9.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Admin;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;
import sg.edu.nus.iss.CAPS_CA_Team9.service.AdminService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.CourseService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.EmailService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.LecturerService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentCourseService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentService;
import sg.edu.nus.iss.CAPS_CA_Team9.util.GenerateUtil;
import sg.edu.nus.iss.CAPS_CA_Team9.validator.CommonValidator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

//import javax.servlet.http.HttpSession;
import javax.validation.Valid;



@Controller
@RequestMapping("/admin")
public class AdminController {

   @Autowired
   private CourseService courseService;
   @Autowired
   private LecturerService lecturerService;
   @Autowired
   private StudentService studentService;
   @Autowired
   private GenerateUtil generateUtil;
   @Autowired
   private AdminService adminService;
   @Autowired
   private StudentCourseService studentCourseService;
   @Autowired
   private EmailService mailService;

   @Autowired
   private CommonValidator commonValidator;

   private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
  
   @InitBinder
   private void initCourseBinder(WebDataBinder binder) {    
      binder.addValidators(commonValidator);
   }

   @GetMapping("")
   public String index() {
      return "redirect:/admin/courses";
   }

   @GetMapping("/courses")
   public String getAllCourse(Model model) {
      List<Course> course = courseService.findall();
      model.addAttribute("list", course);
      // to make tab active
      model.addAttribute("activeTab", "courses");
      return "admin/courses";
   }

   @GetMapping("/enrollments")
   public String indexEnrollment(Model model){
      Student student = new Student();
      model.addAttribute("student", student);
      model.addAttribute("activeTab", "enrollments");
      return "admin/enrollmentIndex";
   }


   @GetMapping("/students")
   public String getAllStudent(Model model) {
      List<Student> students = studentService.findAllStudents();
      model.addAttribute("students", students);
      model.addAttribute("activeTab", "students");
      return "admin/students";
   }

   @GetMapping("/admins")
   public String getAllAdmins(Model model) {
      List<Admin> admins = adminService.findAll();
      model.addAttribute("admins", admins);
      model.addAttribute("activeTab", "admins");
      return "admin/admins";
   }

   @GetMapping("/lecturers")
   public String getAllLecturers(Model model) {
      List<Lecturer> lecturers = lecturerService.findAllLecturers();
      model.addAttribute("lecturers", lecturers);
      model.addAttribute("activeTab", "lecturers");
      return "admin/lecturers";
   }

   @GetMapping("/newcourse")
   public String createNewCourse(Model model) {
      Course course = new Course();
      model.addAttribute("newCourseForm", course);
      model.addAttribute("activeTab", "courses");
      return "admin/addNewCourse";
   }

   // display form to add new student
   @GetMapping("/newstudent")
   public String createNewStudent(Model model) {
      Student student = new Student();
      model.addAttribute("studentform", student);
      model.addAttribute("activeTab", "students");
      return "admin/addNewStudent";
   }

   @GetMapping("/newlecturer")
   public String createNewLecturer(Model model) {
      Lecturer lecturer = new Lecturer();
      model.addAttribute("lecturerform", lecturer);
      model.addAttribute("courses", courseService.findall());
      model.addAttribute("activeTab", "lecturers");
      return "admin/addNewLecturer";
   }

   @GetMapping("/newadmin")
   public String createNewAdmin(Model model){
      Admin admin = new Admin();
      model.addAttribute("adminForm", admin);
      model.addAttribute("activeTab", "admins");
      return "admin/addNewAdmin";
   }

   @PostMapping("/newadmin")
   public String createAdmin(@ModelAttribute("adminForm")Admin admin, BindingResult bindingResult, Model model){
      model.addAttribute("activeTab", "admins");
      if(bindingResult.hasErrors()){
         return "admin/newadmin";
      }
      admin.setId("A"+generateUtil.generateAdminId());
      adminService.createAdmin(admin);
      return "redirect:/admin/admins";
   }


   // add new student
   @PostMapping("/newstudent")
   public String createStudent(@Valid @ModelAttribute("studentform") Student student, BindingResult bindingResult, Model model) {
      model.addAttribute("activeTab", "students");
      if (bindingResult.hasErrors()) {
         return "admin/addNewstudent";
      }
      student.setId("S" + generateUtil.generateStuId());
      studentService.createNewStudent(student);
      LocalDate dobLocalDate = student.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      String dobAsPassword = dobLocalDate.format(formatter);
      String text = "Your password is in the following format: ddMMYYYY" + "\n" + "In this case it is: " + dobAsPassword + "\nPlease login and change it ASAP.";
      mailService.sendMail(student.getEmail(), "", "This is your auto generated password", text);
      return "redirect:/admin/students";
   }


   @PostMapping("/newlecturer")
   public String createLecturer(@Valid @ModelAttribute("lecturerform") Lecturer lecturer, BindingResult bindingResult,Model model) {
      model.addAttribute("activeTab", "lecturers");
      if (bindingResult.hasErrors()) {
         return "admin/addNewLecturer";
      }
      // Lecturer object creation
      Lecturer newLecturer = new Lecturer();
      newLecturer.setId("L" + generateUtil.generateLectId());
      newLecturer.setName(lecturer.getName());
      newLecturer.setTitle(lecturer.getTitle());
      newLecturer.setEmail(lecturer.getEmail());
      newLecturer.setDob(lecturer.getDob());
      Lecturer l = lecturerService.createLecturer(newLecturer);
      // Sending email for the auto generated password 
      LocalDate dobLocalDate = lecturer.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      String dobAsPassword = dobLocalDate.format(formatter);
      String text = "Your password is in the following format: ddMMYYYY" + "\n" + "In this case it is: " + dobAsPassword + "\nPlease login and change it ASAP.";
      mailService.sendMail(lecturer.getEmail(), "", "This is your auto generated password", text);
      
      List<Course> updateCoureList = new ArrayList<>();
      if (lecturer.getCourses() != null) {
         for (Course course : lecturer.getCourses()) {
            if (course.getId() != null) {
               Course originalCourse = courseService.findCourse(course.getId());
               // Add the lecturer to the course first
               List<Lecturer> lecturerList = originalCourse.getLecturers();
               lecturerList.add(l);
               originalCourse.setLecturers(lecturerList);
               courseService.updateCourse(originalCourse);
               // Course now has 3 lecturers/
               updateCoureList.add(originalCourse);
               
            }
            l.setCourses(updateCoureList);
            lecturerService.updateLecturer(l);
         }
      }
      return "redirect:/admin/lecturers";
   }

   @PostMapping("/newcourse")
   public String createNewCourse(@Valid @ModelAttribute("newCourseForm") Course course,BindingResult bindingResult, Model model) {
      model.addAttribute("activeTab", "courses");
      if (bindingResult.hasErrors()) {
         
         return "admin/addNewCourse";
      }
      courseService.createCourse(course);
      return "redirect:/admin/courses";
   }

   // delete student
   @GetMapping("delete_student/{id}")
   public String deleteStudent(@PathVariable("id") String id) {
      try {
         Student student = studentService.findByStudentId(id);
         for (StudentCourse studentCourse : student.getEnrollment()) {
            Course course = courseService.findCourse(studentCourse.getCourse().getId());
            course.setCurrentCount(course.getCurrentCount()-1);
            courseService.updateCourse(course);
            studentCourseService.removeStudentCourse(studentCourse);
         }
         studentService.removeStudent(id);
      } catch (Exception e) {
         return "redirect:/admin/students";
      }
      return "redirect:/admin/students";
   }

   @GetMapping("delete_admin/{id}")
   public String deleteAdmin(@PathVariable("id")String id){
      Admin admin = adminService.getAdminById(id);
      if(admin.getId().equals("A00000004")){
         return "error deleting admin";
      }else{
         adminService.deleteAdmin(admin);
         return "redirect:/admin/admins";
      }
   }



   @GetMapping("delete_course/{id}")
   public String deleteCourse(Model model,@PathVariable("id") int id) {
      Course course = courseService.findCourse(id);
      List<StudentCourse> studentCourses = studentCourseService.getAllCoursesByCourseId(id);
      if(studentCourses.isEmpty()){
         if(course.getLecturers().isEmpty()){
            courseService.removeCourse(course);
         }
         else {
            model.addAttribute("error", "Error: Please unassign the lecturer to the course before deleting.");
         return "/admin/courses-error";
         }
      }
      else {
         model.addAttribute("error", "Error: Cannot delete course with enrolled students.");
         return "/admin/courses-error";
      }

      return "redirect:/admin/courses";
   }

   @GetMapping("delete_lecturer/{id}")
   public String deleteLecturer(@PathVariable("id") String id) {
      Lecturer lecturer = lecturerService.getLecturerById(id);
      if (lecturer != null) {
         List<Course> courseList = lecturer.getCourses();
         if (courseList != null) {
            for (Course course : courseList) {
               List<Lecturer> lecturerList = course.getLecturers();
               lecturerList.remove(lecturer);
               // update the lecturer list
               course.setLecturers(lecturerList);
               courseService.updateCourse(course);
            }
         }
      }
      lecturerService.deleteLecturer(lecturer);
      return "redirect:/admin/lecturers";
   }

   // edit
   @GetMapping("edit_course/{id}")
   public String getCourseDetails(Model model, @PathVariable("id") int id) {
      Course course = courseService.findCourse(id);
      model.addAttribute("course", course);
      model.addAttribute("activeTab", "courses");
      model.addAttribute("availableLecturers", lecturerService.findAllLecturers());
      //System.out.println(course);
      return "admin/courseDetails";
   }


   @GetMapping("edit_admin/{id}")
   public String getAdminDetails(Model model, @PathVariable("id")String id){
      Admin admin = adminService.getAdminById(id);
      model.addAttribute("admin", admin);
      model.addAttribute("activeTab", "admins");
      return "admin/adminDetails";
   }

   @GetMapping("edit_lecturer/{id}")
   public String getLecturerDetails(Model model, @PathVariable("id") String id) {
      Lecturer lecturer = lecturerService.getLecturerById(id);
      List<Course> courses = new ArrayList<>();
      courses = courseService.findall();
      model.addAttribute("lecturer", lecturer);
      model.addAttribute("availableCourses", courseService.findall());
      model.addAttribute("activeTab", "lecturers");
      return "admin/lecturerDetails";
   }

   @GetMapping("edit_student/{id}")
   public String getStudentDetails(Model model, @PathVariable("id") String id) {
      Student student = studentService.findByStudentId(id);
      List<StudentCourse> studentCourses = studentCourseService.findByStudentId(id);

      if (student != null) {
         model.addAttribute("student", student);
         model.addAttribute("activeTab", "students");
         model.addAttribute("studentCourses", studentCourses);
         return "admin/studentDetails";
      } else {
         return "redirect:/admin/students";
      }
   }

   @PostMapping("update_course/{id}")
   public String updateCourse(@ModelAttribute("course") Course course, @PathVariable("id") int id, Model model,
         BindingResult result) {
      if (result.hasErrors()) {
         return "course/edit";
      }
      Course existingCourse = courseService.findCourse(id);

      List<Lecturer> updateLecturers = new ArrayList<>();
      // check null lecturers
      if (course.getLecturers() != null) {
         for (Lecturer lecturer : course.getLecturers()) {
            if (lecturer.getId() != null) {
               Lecturer lect = lecturerService.getLecturerById(lecturer.getId());
               updateLecturers.add(lect);
               Lecturer assignedLecturer = lecturerService.getLecturerById(lecturer.getId());
               // get the course list of the assigned lecturer
               List<Course> courseList = assignedLecturer.getCourses();
               // check course is already in the list
               if (!courseList.contains(existingCourse)) {
                  courseList.add(existingCourse);
               }
               assignedLecturer.setCourses(courseList);
               lecturerService.updateLecturer(assignedLecturer);
            }
         }
         // remove the course from all lecturers that are not in updateLecturers list
         for (Lecturer notInLecturer : lecturerService.findAllLecturers()) {
            if (!updateLecturers.contains(notInLecturer)) {
               List<Course> updCourseList = notInLecturer.getCourses();
               updCourseList.remove(existingCourse);
               notInLecturer.setCourses(updCourseList);
               lecturerService.updateLecturer(notInLecturer);
            }
         }

      } else {// if lecture list is null , remove all lecturer
         for (Lecturer lecturer : lecturerService.findAllLecturers()) {
            for (Course lCourse : lecturer.getCourses()) {
               if (lCourse.getId() == existingCourse.getId()) {
                  List courseList = lecturer.getCourses();
                  courseList.remove(existingCourse);
                  lecturer.setCourses(courseList);
                  lecturerService.updateLecturer(lecturer);
                  break;
               }
            }
         }
      }

      existingCourse.setCredits(course.getCredits());
      existingCourse.setName(course.getName());
      existingCourse.setDescription(course.getDescription());
      existingCourse.setSize(course.getSize());
      existingCourse.setLecturers(updateLecturers);
      courseService.updateCourse(existingCourse);

      return "redirect:/admin/courses";
   }

   @PostMapping("update_lecturer/{id}")
   public String updateLecturer(@ModelAttribute("lecturer") Lecturer lecturer, @PathVariable("id") String id,
         Model model, BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
         return "lecturer/edit";
      }
      // assigning lecturer to all courses
      Lecturer existingLecturer = lecturerService.getLecturerById(id);

      // checking null courses
      List<Course> updateCoureList = new ArrayList<>();
      if (lecturer.getCourses() != null) {
         for (Course course : lecturer.getCourses()) {
            // need alternative solution
            if (course.getId() != null) {
               updateCoureList.add(course);
               Course assignedCourse = courseService.findCourse(course.getId());
               List<Lecturer> lecturerList = new ArrayList<>();
               lecturerList.add(existingLecturer);
               assignedCourse.setLecturers(lecturerList);
               courseService.updateCourse(assignedCourse);
            }

         }
      } else {// if course list is null, remove lecturers from all courses
         for (Course course : courseService.findall()) {
            // need to find better solution
            for (Lecturer lect : course.getLecturers()) {
               if (lect.getId() == existingLecturer.getId()) {
                  List lectList = course.getLecturers();
                  lectList.remove(existingLecturer);
                  course.setLecturers(lectList);
                  courseService.updateCourse(course);
                  break;
               }
            }

         }
      }
      existingLecturer.setCourses(updateCoureList);
      lecturerService.updateLecturer(lecturer);
      return "redirect:/admin/lecturers";
   }

   @PostMapping("update_student/{id}")
   public String updateStudent(@ModelAttribute("student") Student student, @PathVariable("id") String id,
         @ModelAttribute("studentCourses") StudentCourse studentCourseList,
         Model model, BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
         return "student/edit";
      }
      // get active courses
      // subtract removed courses from active course
      // update active course
      Student existingStudent = studentService.findByStudentId(id);
      if (existingStudent != null) {
         studentService.updateStudent(student);
         return "redirect:/admin/students";
      } else {
         return "edit_student";
      }
   }

   @PostMapping("update_admin/{id}")
   public String updateAdmin(@ModelAttribute("admin")Admin admin, @PathVariable("id")String id,
   Model model, BindingResult bindingResult){
      if(bindingResult.hasErrors()){
         return "edit_admin";
      }
      Admin existingAdmin = adminService.getAdminById(id);
      if(existingAdmin != null){
         adminService.updateAdmin(admin);
         return "redirect:/admin/admins";
      }else{
         return "edit_admin";
      }
   }

   @PostMapping("/enrollments")
   public String getEnrollments(@ModelAttribute("student")Student student,Model model, BindingResult bindingResult){
      List<StudentCourse> enrollments = studentCourseService.findByStudentId(student.getId());
      model.addAttribute("enrollments", enrollments);
      return "admin/enrollments";
   }

   @GetMapping("/manage_enrollment")
   public String manageEnrollment(@RequestParam("stuId") String stuId, @RequestParam("courseId") Integer courseId, Model model){
      StudentCourse studentCourse = studentCourseService.findByStudentCourseId(stuId, courseId);
      model.addAttribute("activeTab", "enrollments");
      model.addAttribute("studentCourse", studentCourse);
      return "admin/manageEnrollment";
   }

   @RequestMapping(value = "/addEnrollment/{studentid}", method = {RequestMethod.GET, RequestMethod.POST})
   public String AddEnrollment(Model model, @PathVariable("studentid") String studentid){
      List<Course> courses = courseService.findall();
      List<Integer> enrolledCourseIds = studentCourseService.getAllEnrolledCourseIds(studentid);
      List<Course> availableCourses = courses.stream()
         .filter(course -> !enrolledCourseIds.contains(course.getId()))
         .collect(Collectors.toList());
      model.addAttribute("studentid", studentid);
      model.addAttribute("courses", availableCourses);
      return "admin/AddEnrollment";
   }

   @PostMapping("/remove_student/{studentId}/{courseId}")
   public String removeEnrollment(@PathVariable("studentId") String studentId, @PathVariable("courseId") Integer courseId, Model model){
      StudentCourse studentCourse = studentCourseService.findByStudentCourseId(studentId, courseId);
      Course course = courseService.findCourse(courseId);
      
      if(studentCourse.getEnrollStatus()==0) {
         // Remove the student by setting status to 1
         studentCourse.setEnrollStatus(1);
         course.setCurrentCount(course.getCurrentCount()-1);
         // Save the update
         studentCourseService.updateStudentCourse(studentCourse);
      }
      else {
         studentCourse.setEnrollStatus(0);
         course.setCurrentCount(course.getCurrentCount()+1);
         studentCourseService.updateStudentCourse(studentCourse);
      }
      courseService.updateCourse(course);
      String redirectUrl = "redirect:/admin/manage_enrollment?stuId=" + studentId + "&courseId=" + courseId;
      return redirectUrl;
     
   }


   //Enrolling student

   @GetMapping("/enroll/{id}/{studentid}")
    public String saveCourse(Model model,@PathVariable("id") int courseId, @PathVariable("studentid") String studentid){
        
        Course course = courseService.findCourse(courseId);
        Student student = studentService.findByStudentId(studentid);
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
            stdCourse.setStudentId(studentid);
            Integer enrollStatus = 0;
            stdCourse.setEnrollStatus(enrollStatus);
            stdCourse.setEnrollDate(Calendar.getInstance().getTime());
            studentCourseService.createStudentCourse(stdCourse);
            course.setCurrentCount(course.getCurrentCount()+1);
            courseService.updateCourse(course);
        }
        return "redirect:/admin/addEnrollment/" +studentid;
        
    }

}