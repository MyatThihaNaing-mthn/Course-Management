package sg.edu.nus.iss.CAPS_CA_Team9.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;
import sg.edu.nus.iss.CAPS_CA_Team9.repository.StudentCourseRepository;

@Service
public class StudentCourseServiceImpt implements StudentCourseService {
    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Override
    public List<StudentCourse> getAllCoursesByCourseId(Integer id) {
        List<StudentCourse> studentCourse = studentCourseRepository.findByCourseId(id);
        return studentCourse;
    }

    @Override
    public List<StudentCourse> findByStudentId(String id) {
        return studentCourseRepository.findByStudentId(id);
    }

    @Transactional
    @Override
    public StudentCourse createStudentCourse(StudentCourse studentCourse){
        return studentCourseRepository.saveAndFlush(studentCourse);
    }

    @Transactional
    @Override
    public StudentCourse updateStudentCourse(StudentCourse studentCourse) {
        StudentCourse existStudentCourse = studentCourseRepository.findByStudentCourseId(studentCourse.getStudentId(),studentCourse.getCourseId());
        existStudentCourse.setCourse(studentCourse.getCourse());
        existStudentCourse.setCourseId(studentCourse.getCourseId());
        existStudentCourse.setStudentId(studentCourse.getStudentId());
        existStudentCourse.setScore(studentCourse.getScore());
        existStudentCourse.setEnrollStatus(studentCourse.getEnrollStatus());
        return studentCourseRepository.save(existStudentCourse);
    }

    @Override
    public StudentCourse findByCourseId(int id) {
        return studentCourseRepository.findOneByCourseId(id);
    }

    @Override
    public StudentCourse findByStudentCourseId(String studentId, Integer courseId) {
        return studentCourseRepository.findByStudentCourseId(studentId,courseId);
    }

    @Override
    public List<StudentCourse> findByEnrollStatus(Integer status) {
        return studentCourseRepository.findByEnrollStatus(status);
    }

    @Override
    public List<StudentCourse> findByStudentIdWithoutRemoved(String id) {
        return studentCourseRepository.findByStudentIdWithoutRemoved(id);
    }

    @Override
    public void removeStudentCourse(StudentCourse studentCourse) {
        studentCourseRepository.delete(studentCourse);
    }

    @Override
    public List<Integer> getAllEnrolledCourseIds(String studentId) {
        return studentCourseRepository.findAllEnrolledCourseIds(studentId);
    }

}