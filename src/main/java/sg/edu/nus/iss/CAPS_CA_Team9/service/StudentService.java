package sg.edu.nus.iss.CAPS_CA_Team9.service;

import java.util.List;
//import java.util.Optional;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;

//
public interface StudentService {

    List<Student> findAllStudents();

    Student findByStudentId(String stuId);

    Student createNewStudent(Student student);

    Student getLastStudent();

    void removeStudent(String id);

    public Student updateStudent(Student student);
    public List<Integer> calculateMonthlyStudentCount() ;
}
