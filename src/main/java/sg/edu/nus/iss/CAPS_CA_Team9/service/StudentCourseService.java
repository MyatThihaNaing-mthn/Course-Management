package sg.edu.nus.iss.CAPS_CA_Team9.service;

import java.util.List;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;

public interface StudentCourseService {
    public List<StudentCourse> getAllCoursesByCourseId(Integer id);
    public List<StudentCourse> findByStudentId(String id);
    public StudentCourse updateStudentCourse(StudentCourse studentCourse);
    public StudentCourse createStudentCourse(StudentCourse studentCourse);
    public StudentCourse findByCourseId(int id);
    public StudentCourse findByStudentCourseId(String studentId, Integer courseId);
    public List<StudentCourse> findByEnrollStatus(Integer status);
    public List<StudentCourse> findByStudentIdWithoutRemoved(String id);
    //added by TH
    public void removeStudentCourse(StudentCourse studentCourse);

    public List<Integer> getAllEnrolledCourseIds(String studentId);

}