package sg.edu.nus.iss.CAPS_CA_Team9.service;

import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;

public interface CourseService {
    
    public List<Course> findall();
    
    public Course createCourse(Course course);

    public void removeCourse(Course course);
  
    public Course findCourse(Integer cid);

    public List<Course> findCourseByName(String name);

    public List<Course> findCourseByDescription(String description);

    public int getCurrentEnrollmentCount(List<StudentCourse>studentCourses);

    public Course updateCourse(Course course);

    public List<Course> findAvailableCourseList(List<Integer> courseList);

    public List<Course> findRegisteredCourseList(List<Integer> courseList);

    public List<Course> findCoursesByDateRange(Date startDate, Date endDate);
}
