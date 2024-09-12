package sg.edu.nus.iss.CAPS_CA_Team9.repository;

import java.util.List;

//import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;


public interface LecturerRepository extends JpaRepository<Lecturer,String> {
    
    public Lecturer findByName(String name);
    @Query("Select c From Lecturer l Join l.courses c Where l.name =:name")
    public List<Course> findCourseByLecturerName(@Param("name") String name);
    @Query("Select c From Lecturer l Join l.courses c Where l.id =:id")
    public List<Course> findCourseByLecturerId(@Param("id") String id);
    public Lecturer findTopByOrderByIdDesc();

    @Query("Select l From Lecturer l Join l.courses lc where lc.id =:id")
    public List<Lecturer> findLecturersByCourseId(@Param("id") Integer id);

}

