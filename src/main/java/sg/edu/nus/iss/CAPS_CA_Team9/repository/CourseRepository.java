package sg.edu.nus.iss.CAPS_CA_Team9.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;


public interface CourseRepository extends JpaRepository<Course,Integer> {
    public List<Course> findByName(String name);
    public List<Course> findByDescription(String description);

    @Query("SELECT c FROM Course c WHERE c.id NOT IN(:courseList)")
    List<Course> findAvailableCourses(@Param("courseList") List<Integer> courseList);

    @Query("SELECT c FROM Course c WHERE c.id IN (:courseList)")
    List<Course> findRegisteredCourses(@Param("courseList") List<Integer> courseList);
    
    @Query("SELECT c FROM Course c WHERE c.startDate >= :startDate AND c.endDate <= :endDate ORDER BY c.currentCount DESC")
    List<Course> findCoursesByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
