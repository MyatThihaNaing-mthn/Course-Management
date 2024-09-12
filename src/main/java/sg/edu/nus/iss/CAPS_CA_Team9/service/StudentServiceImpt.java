package sg.edu.nus.iss.CAPS_CA_Team9.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;
import sg.edu.nus.iss.CAPS_CA_Team9.repository.StudentRepository;

//
@Service
@Transactional(readOnly = true)
public class StudentServiceImpt implements StudentService{
     @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

    @Override
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student findByStudentId(String stuId) {
        return studentRepository.findById(stuId).orElse(null);
    }


    @Transactional(readOnly = false)
    @Override
    public Student createNewStudent(Student student) {
        LocalDate dobLocalDate = student.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String dobAsPassword = dobLocalDate.format(formatter);
        String hashedPassword = bCryptPasswordEncoder.encode(dobAsPassword);
        student.setPassword(hashedPassword);
        student.setEnrollmentDate(Calendar.getInstance().getTime());
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    @Override
    public Student getLastStudent(){
        return studentRepository.findTopByOrderByIdDesc();
    }

    @Transactional(readOnly = false)
    @Override
    public void removeStudent(String id){
        studentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Student updateStudent(Student student){
       Student existingStudent = studentRepository.findById(student.getId()).orElse(null);
        
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        if(student.getEnrollment()!=null){
            existingStudent.setEnrollment(student.getEnrollment());
        }
        existingStudent.setGpa(student.getGpa());
        if(student.getPassword()!=null) {
            String hashedPassword = bCryptPasswordEncoder.encode(student.getPassword());
            existingStudent.setPassword(hashedPassword);
        }
        return studentRepository.saveAndFlush(existingStudent);
    }
    @Transactional
    @Override
    public List<Integer> calculateMonthlyStudentCount() {
        List<Integer> monthlyCounts = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.now().withMonth(month);
            LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

            int count = studentRepository.countByEnrollmentDateBetween(startDate, endDate);
            monthlyCounts.add(count);
        }

        return monthlyCounts;
    }
}
