package sg.edu.nus.iss.CAPS_CA_Team9.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;
import sg.edu.nus.iss.CAPS_CA_Team9.repository.CourseRepository;


@Service
public class CourseServiceImpt implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    
    @Override
    public List<Course> findall() {
        List<Course> courses = courseRepository.findAll();
        return courses;
    }

    @Transactional
    @Override
    public Course createCourse(Course course) {
        return courseRepository.saveAndFlush(course);
    }

    @Transactional
    @Override
    public void removeCourse(Course course) {
        courseRepository.delete(course);
    }

    @Override
    public Course findCourse(Integer cid) {
        return courseRepository.findById(cid).orElse(null);
    }

    @Override
    public List<Course> findCourseByName(String name) {
        return courseRepository.findByName(name);
    }

    @Override
    public List<Course> findCourseByDescription(String description){
        return courseRepository.findByDescription(description);
    }

    @Override
    public int getCurrentEnrollmentCount(List<StudentCourse>studentCourses) {
        return studentCourses.size();
    }

    @Override
    public List<Course> findAvailableCourseList(List<Integer> courseList) {
        return courseRepository.findAvailableCourses(courseList);
    }

    @Override
    public List<Course> findRegisteredCourseList(List<Integer> courseList) {
        return courseRepository.findRegisteredCourses(courseList);
    }
    
    @Transactional
    @Override
    public Course updateCourse(Course course) {
        Course existingCourse = courseRepository.findById(course.getId()).orElse(null);
        existingCourse.setName(course.getName());
        existingCourse.setCredits(course.getCredits());
        existingCourse.setCurrentCount(course.getCurrentCount());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setLecturers(course.getLecturers());
        existingCourse.setSchedule(course.getSchedule());
        existingCourse.setSize(course.getSize());
        existingCourse.setStartDate(course.getStartDate());
        existingCourse.setEndDate(course.getEndDate());
        return courseRepository.saveAndFlush(existingCourse);
    }

    public List<Course> findCoursesByDateRange(Date startDate, Date endDate) {
        return courseRepository.findCoursesByDateRange(startDate, endDate);
    }
    
}
