package sg.edu.nus.iss.CAPS_CA_Team9.service;

import java.util.List;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;

public interface LecturerService {

    public Lecturer createLecturer(Lecturer lecturer);

    public Lecturer findLecturerByName(String name);

    public List<Lecturer> findAllLecturers();

    public Lecturer getLecturerById(String id);

    public List<Course> getCoursesByLecturerId(String id);

    public List<Course> getCoursesByLecturerName(String name);

    public Lecturer updateLecturer(Lecturer lecturer);
    
    public void deleteLecturer(Lecturer lecturer);

    public Lecturer getLastLecturer();

    public List<Lecturer> getLecturersByCourseId(Integer id);

}   
