package sg.edu.nus.iss.CAPS_CA_Team9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourseId;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {
    public List<StudentCourse> findByCourseId(Integer id);
    public List<StudentCourse> findByStudentId(String studentId);
    public StudentCourse findOneByCourseId(Integer id);
    @Query("SELECT sc FROM StudentCourse sc WHERE sc.studentId = :studentId AND sc.courseId = :courseId")
    public StudentCourse findByStudentCourseId(@Param("studentId") String studentId, @Param("courseId") Integer courseId);
    public List<StudentCourse> findByEnrollStatus(Integer status);

    @Query("SELECT s FROM StudentCourse s WHERE s.enrollStatus != 1 and s.studentId = :studentId")
    public List<StudentCourse> findByStudentIdWithoutRemoved(String studentId);

    @Query("SELECT sc.courseId FROM StudentCourse sc WHERE sc.studentId = :studentId")
    public List<Integer> findAllEnrolledCourseIds(@Param("studentId") String studentId);

}
