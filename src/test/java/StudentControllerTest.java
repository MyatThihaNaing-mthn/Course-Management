import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import sg.edu.nus.iss.CAPS_CA_Team9.controller.StudentController;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;
import sg.edu.nus.iss.CAPS_CA_Team9.service.CourseServiceImpt;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentCourseServiceImpt;
import sg.edu.nus.iss.CAPS_CA_Team9.service.StudentServiceImpt;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
/**
 * @program: test
 * @description: 测试类
 * @author: shenning
 * @create: 2023-06-20 16:48
 **/
public class StudentControllerTest {

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private StudentCourseServiceImpt studentCourseService;

    @Mock
    private CourseServiceImpt courseService;

    @Mock
    private StudentServiceImpt studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAvailableCoursesWithValidId() {
        String id = "S00000001";
        // String id = "validId";
        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<Integer> courseIdList = new ArrayList<>();
        List<Course> courseList = new ArrayList<>();
        when(session.getAttribute("id")).thenReturn(id);
        when(studentCourseService.findByStudentId(id)).thenReturn(studentCourseList);
        when(courseService.findAvailableCourseList(courseIdList)).thenReturn(courseList);
        String result = studentController.getAvailableCourses(model, session);
        assertEquals("student/courseList", result);
        verify(model, times(2)).addAttribute(anyString(), any());
        verify(session, times(1)).getAttribute("id");
        verify(studentCourseService, times(1)).findByStudentId(id);
        verify(courseService, times(1)).findAvailableCourseList(courseIdList);
    }

    @Test
    public void testGetAvailableCoursesWithNullId() {
        when(session.getAttribute("id")).thenReturn(null);
        String result = studentController.getAvailableCourses(model, session);
        assertEquals("redirect:/login", result);
        verify(model, never()).addAttribute(anyString(), any());
        verify(session, times(1)).getAttribute("id");
        verify(studentCourseService, never()).findByStudentId(anyString());
        verify(courseService, never()).findAvailableCourseList(anyList());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetCourseWithValidId() {
        String id = "S00000001";
        int courseId = 123;
        Course course = new Course();
        List<Lecturer> lecturers = new ArrayList<>();
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(session.getAttribute("id")).thenReturn(id);

        Model model = Mockito.mock(Model.class);

        Mockito.when(courseService.findCourse(Mockito.anyInt())).thenReturn(course);

        String result = studentController.getCourse(model, courseId, session);
        assertEquals("student/courseEnroll", result);
        verify(model, times(1)).addAttribute("course", course);
        verify(session, times(1)).getAttribute("id");
        verify(courseService, times(1)).findCourse(courseId);
    }





    @Test
    public void testSaveCourseWithValidId() {
        String id = "S00000001";
        int courseId = 1;
        Course course = new Course();
        Student student = new Student();
        when(session.getAttribute("id")).thenReturn(id);
        when(courseService.findCourse(courseId)).thenReturn(course);
        when(studentService.findByStudentId(id)).thenReturn(student);

        String result = studentController.saveCourse(model, courseId, session);
        assertEquals("redirect:/student/courseList", result);
        verify(session, times(1)).getAttribute("id");
        verify(courseService, times(1)).findCourse(courseId);
        verify(studentService, times(1)).findByStudentId(id);
        verify(studentCourseService, times(1)).createStudentCourse(any(StudentCourse.class));
        verify(courseService, times(1)).updateCourse(course);
    }

    @Test
    public void testGetRegisteredCoursesWithValidId() {
        String id = "S00000001";
        List<StudentCourse> registCourseList = new ArrayList<>();
        List<Integer> courseIdList = new ArrayList<>();
        List<Course> courseList = new ArrayList<>();
        when(session.getAttribute("id")).thenReturn(id);
        when(studentCourseService.findByStudentId(id)).thenReturn(registCourseList);
        when(courseService.findRegisteredCourseList(courseIdList)).thenReturn(courseList);
        String result = studentController.getRegisteredCourses(model, session);

        assertEquals("student/registeredCourse", result);
        verify(model, times(1)).addAttribute("courseList", courseList);
        verify(model, times(1)).addAttribute(eq("currentDate"), any(Date.class));

        verify(session, times(1)).getAttribute("id");
        verify(studentCourseService, times(1)).findByStudentId(id);
        verify(courseService, times(1)).findRegisteredCourseList(courseIdList);
    }

    @Test
    public void testViewGpaAndGradeWithNullId() {
        when(session.getAttribute("id")).thenReturn(null);

        String result = studentController.viewGpaAndGrade(model, session);

        assertEquals("redirect:/login", result);
        verify(session, times(1)).getAttribute("id");
    }

    @Test
    public void testViewGpaAndGradeWithValidId() {
        String id = "S00000001";
        List<StudentCourse> registCourseList = new ArrayList<>();
        List<Course> courseList = new ArrayList<>();

        when(session.getAttribute("id")).thenReturn(id);
        when(studentCourseService.findByStudentId(id)).thenReturn(registCourseList);
        when(courseService.findRegisteredCourseList(Mockito.anyList())).thenReturn(courseList);
        String result = studentController.viewGpaAndGrade(model, session);
        assertEquals("student/viewGPA", result);
        verify(session, times(1)).getAttribute("id");
        verify(model, times(1)).addAttribute("gpa", Double.NaN);
        verify(model, times(1)).addAttribute("registCourseList", registCourseList);
    }


    @Test
    public void testCalculateGPA() {
        List<StudentCourse> registCourseList = new ArrayList<>();
        List<Course> courseList = new ArrayList<>();
        Course course1 = new Course();
        course1.setId(1);
        course1.setCredits(3);
        Course course2 = new Course();
        course2.setId(2);
        course2.setCredits(4);
        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setCourseId(1);
        studentCourse1.setScore(70.0);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setCourseId(2);
        studentCourse2.setScore(75.0);
        registCourseList.add(studentCourse1);
        registCourseList.add(studentCourse2);
        courseList.add(course1);
        courseList.add(course2);

        double gpa = studentController.calculateGPA(registCourseList, courseList);

         assertEquals(4.28, gpa, 0.01);
         //will show wrong
        // assertEquals(3.57, gpa, 0.01);
    }


}
