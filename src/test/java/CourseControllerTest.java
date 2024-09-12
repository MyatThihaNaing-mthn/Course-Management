import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import sg.edu.nus.iss.CAPS_CA_Team9.controller.CourseController;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.service.CourseService;
import sg.edu.nus.iss.CAPS_CA_Team9.service.LecturerService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
/**
 * @program: test
 * @description: 测试类
 * @author: shenning
 * @create: 2023-06-20 19:15
 **/
class CourseControllerTest {
    @Mock
    private CourseService courseService;
    @Mock
    private LecturerService lecturerService;
    @InjectMocks
    private CourseController courseController;
    @Mock
    private Model model;
    @Mock
    private BindingResult bindingResult;

    @Captor
    private ArgumentCaptor<List<Course>> courseListCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCourse() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course());
        when(courseService.findall()).thenReturn(courses);
        String viewName = courseController.getAllCourse(model);
        assertEquals("CourseView", viewName);
        verify(model).addAttribute(eq("list"), courseListCaptor.capture());
        assertEquals(courses, courseListCaptor.getValue());
    }

    @Test
    void testAddCourse() {
        List<Lecturer> lecturers = new ArrayList<>();
        lecturers.add(new Lecturer());
        when(lecturerService.findAllLecturers()).thenReturn(lecturers);
        String viewName = courseController.addCourse(model);
        assertEquals("AddNewCourse", viewName);
        verify(model).addAttribute(eq("lecturers"), eq(lecturers));
        verify(model).addAttribute(eq("courseform"), any(Course.class));
    }

    @Test
    void testSaveCourse() {
        Course course = new Course();
        String lecturerId = "L00000001";
        Lecturer lecturer = new Lecturer();
        lecturer.setCourses(new ArrayList<>());
        when(courseService.createCourse(course)).thenReturn(course);
        when(lecturerService.getLecturerById(lecturerId)).thenReturn(lecturer);
        String viewName = courseController.saveCourse(course, lecturerId, bindingResult);
        assertEquals("redirect:/course/list", viewName);
        verify(lecturerService).getLecturerById(lecturerId);
        verify(courseService).createCourse(course);
        verify(model, never()).addAttribute(anyString(), any());
        assertEquals(1, lecturer.getCourses().size());
        assertEquals(course, lecturer.getCourses().get(0));
    }
}
