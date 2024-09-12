import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import sg.edu.nus.iss.CAPS_CA_Team9.controller.LecturerController;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;
//import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourseId;
import sg.edu.nus.iss.CAPS_CA_Team9.service.CourseServiceImpt;
import sg.edu.nus.iss.CAPS_CA_Team9.service.LecturerServiceImpt;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentCourseServiceImpt;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentServiceImpt;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * @program: test
 * @description: 测试类
 * @author: shenning
 * @create: 2023-06-20 21:13
 **/
public class LecturerControllerTest {
    @Mock
    private CourseServiceImpt courseService;
    @Mock
    private LecturerServiceImpt lecturerService;
    @InjectMocks
    private LecturerController lecturerController;
    @Mock
    private HttpSession session;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private StudentCourseServiceImpt studentCourseService;
    @Mock
    private StudentServiceImpt studentService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = mock(StudentServiceImpt.class);
    }

    @Test
    public void testGetAllCourses() {
        String lecturerId = "L00000001";
        when(session.getAttribute("id")).thenReturn("L00000001");
        List<Course> expectedCourses = new ArrayList<>();
        Course course1 = new Course();
        course1.setId(1);
        course1.setCredits(3);
        Course course2 = new Course();
        course2.setId(2);
        course2.setCredits(4);
        expectedCourses.add(course1);
        expectedCourses.add(course2);
        when(lecturerService.getCoursesByLecturerId(lecturerId)).thenReturn(expectedCourses);
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        when(model.getAttribute("courses")).thenReturn(expectedCourses);
        String viewName = lecturerController.getAllCourses(model, session);
        assertEquals("lecturer/LecturerViewCourse", viewName);
        List<Course> actualCourses = (List<Course>) model.getAttribute("courses");
        for (int i = 0; i < expectedCourses.size(); i++) {
            Course expectedCourse = expectedCourses.get(i);
            Course actualCourse = actualCourses.get(i);
            assertEquals(expectedCourse.getId(), actualCourse.getId());
            assertEquals(expectedCourse.getCredits(), actualCourse.getCredits());
        }
    }
//    @Test
//    void testGetStudentPerformance_WithValidId() {
//        String id = "S00000001";
//        List<StudentCourse> expectedStudentCourses = Collections.emptyList();
//        Student expectedStudent = new Student();
//        expectedStudent.setId(id);
//        Model model = mock(Model.class);
//        when(studentCourseService.findByStudentId(id)).thenReturn(new ArrayList<>());
//        when(studentService.findByStudentId(id)).thenReturn(expectedStudent);
//        String viewName = lecturerController.getStudentPerformance(id, model);
//        assertEquals("lecturer/LecturerViewStudentPerf", viewName);
//        Mockito.verify(model).addAttribute(eq("studentCourse"), eq(expectedStudentCourses));
//        Mockito.verify(model).addAttribute(eq("student"), eq(expectedStudent));
//        Mockito.verify(model, Mockito.never()).addAttribute(eq("errorMessage"), anyString());
//    }

    @Test
    void testGetStudentPerformance_WithInvalidId() {
        String id = "S00000001";
        List<StudentCourse> expectedStudentCourses = new ArrayList<>();
        Model model = mock(Model.class);
        when(studentCourseService.findByStudentId(id)).thenReturn(expectedStudentCourses);
        when(studentService.findByStudentId(id)).thenReturn(null);
        String viewName = lecturerController.getStudentPerformance(id, model);
        assertEquals("lecturer/LecturerViewStudentPerf", viewName);
        Mockito.verify(model).addAttribute("studentCourse", expectedStudentCourses);
        Mockito.verify(model).addAttribute("errorMessage", "Student not found");
    }

    @Test
    void testGetStudentPerformance_WithoutId() {
        Model model = mock(Model.class);
        String viewName = lecturerController.getStudentPerformance(null, model);
        assertEquals("lecturer/LecturerViewStudentPerf", viewName);
        Mockito.verify(model).addAttribute("studentCourse", new ArrayList<>());
    }

    @Test
    void testSelectCourses() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        String lecturerId = "L00000001";
        List<Course> expectedCourses = new ArrayList<>();
        when(session.getAttribute("id")).thenReturn(lecturerId);
        when(lecturerService.getCoursesByLecturerId(lecturerId)).thenReturn(expectedCourses);
        String viewName = lecturerController.displayCourses(model, session);
        assertEquals("lecturer/LecturerSelectCourse", viewName);
        Mockito.verify(model).addAttribute("courses", expectedCourses);
    }

    @Test
    void testGradeCourses() {
        int courseId = 123;
        List<StudentCourse> expectedStudentCourses = new ArrayList<>();
        Course expectedCourse = new Course();
        expectedCourse.setId(courseId);
        Model model = mock(Model.class);
        when(studentCourseService.getAllCoursesByCourseId(courseId)).thenReturn(expectedStudentCourses);
        when(courseService.findCourse(courseId)).thenReturn(expectedCourse);
        String viewName = lecturerController.displayCourses(courseId, model);
        assertEquals("lecturer/LecturerGradeACourse", viewName);
        Mockito.verify(model).addAttribute(eq("course"), eq(expectedCourse));
        Mockito.verify(model).addAttribute(eq("studentcourses"), eq(expectedStudentCourses));
    }

//    @Test
//    void testSaveStudentScore() {
//        // Arrange
//        double score = 85.5;
//        StudentCourse studentCourse = new StudentCourse();
//        studentCourse=new StudentCourse("S00000001", 1);
//        StudentCourseId studentCourseId =new StudentCourseId("S00000001", 1);
//        studentCourse.setScore(0.0);
//        when(studentCourseService.findByCourseId(studentCourseId.getCourseId())).thenReturn(studentCourse);
//
//        // Act
//        String viewName = lecturerController.saveStudentScore(studentCourseId.get, score);
//
//        // Assert
//        assertEquals("redirect:/lecturer/gradeCourse?courseId=" + studentCourseId, viewName);
//        assertEquals(score, studentCourse.getScore());
//        verify(studentCourseService).updateStudentCourse(studentCourse);
//    }

    @Test
    void testGetCourseEnroll() {
        String lecturerId = "L00000001";
        List<Course> expectedCourses = new ArrayList<>();
        List<StudentCourse> expectedStudentCourses = new ArrayList<>();
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("id")).thenReturn(lecturerId);
        when(lecturerService.getCoursesByLecturerId(lecturerId)).thenReturn(expectedCourses);
        String viewName = lecturerController.getCourseSelection(model, session);
        assertEquals("lecturer/LecturerViewEnrolment", viewName);
        Mockito.verify(session).getAttribute("id");
        Mockito.verify(lecturerService).getCoursesByLecturerId(lecturerId);
        Mockito.verify(model).addAttribute("selectedCourse", null);
        Mockito.verify(model).addAttribute("lecturerCourses", expectedCourses);
        Mockito.verify(model).addAttribute("studentCourses", expectedStudentCourses);
    }


}
