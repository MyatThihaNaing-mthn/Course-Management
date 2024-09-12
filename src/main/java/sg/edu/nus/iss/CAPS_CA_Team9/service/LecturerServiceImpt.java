package sg.edu.nus.iss.CAPS_CA_Team9.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
import java.util.List;
//import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.repository.LecturerRepository;

@Service
public class LecturerServiceImpt implements LecturerService {
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
    
    @Transactional
    @Override
    public Lecturer createLecturer(Lecturer lecturer) {
        LocalDate dobLocalDate = lecturer.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String dobAsPassword = dobLocalDate.format(formatter);
        String hashedPassword = bCryptPasswordEncoder.encode(dobAsPassword);
        lecturer.setPassword(hashedPassword);
        return lecturerRepository.saveAndFlush(lecturer);
    }

    @Override
    public Lecturer findLecturerByName(String name) {
        return lecturerRepository.findByName(name);
    }

    @Override
    public List<Lecturer> findAllLecturers() {
        return lecturerRepository.findAll();
    }

    @Override
    public Lecturer getLecturerById(String id) {
        Lecturer found = lecturerRepository.findById(id).orElse(null);
        return found;
    }

    @Override
    public List<Course> getCoursesByLecturerId(String id) {
        List<Course> courses = lecturerRepository.findCourseByLecturerId(id);
        return courses;
    }

    @Override
    public List<Course> getCoursesByLecturerName(String name) {
        List<Course> courses = lecturerRepository.findCourseByLecturerName(name);
        return courses;
    }
    
    @Transactional
    @Override
    public Lecturer updateLecturer(Lecturer lecturer) {
        Lecturer existingLecturer = lecturerRepository.findById(lecturer.getId()).orElse(null);
        existingLecturer.setTitle(lecturer.getTitle());
        existingLecturer.setName(lecturer.getName());
        existingLecturer.setEmail(lecturer.getEmail());
        existingLecturer.setCourses(lecturer.getCourses());
        existingLecturer.setDob(lecturer.getDob());
        if(lecturer.getPassword()!=null) {
            String hashedPassword = bCryptPasswordEncoder.encode(lecturer.getPassword());
            existingLecturer.setPassword(hashedPassword);
        }
        return lecturerRepository.saveAndFlush(existingLecturer);
    }

    @Override
    public Lecturer getLastLecturer(){
        return lecturerRepository.findTopByOrderByIdDesc();
    }

    @Transactional
    @Override
    public void deleteLecturer(Lecturer lecturer) {
        lecturerRepository.delete(lecturer);
    }

    @Override
    public List<Lecturer> getLecturersByCourseId(Integer id) {
        return lecturerRepository.findLecturersByCourseId(id);
    }

}
