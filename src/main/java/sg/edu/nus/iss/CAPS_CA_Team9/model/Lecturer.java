package sg.edu.nus.iss.CAPS_CA_Team9.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
public class Lecturer {
    @Id
    @Column(name = "lecturerId")
    private String id;

    @Column(name ="lecturerName")
    @NotBlank(message = "Name cannot be blank") 
    private String name;

    @Column(name ="lecturerTitle")
    @NotBlank(message = "Title cannot be blank") 
    private String title;

    @Column(name ="lecturerPwd")
    private String password;

    @Column(name ="lecturerEmail")
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @Column(name="lecturerDob")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @ManyToMany
    @JoinTable(name = "lecturer_course",
               joinColumns = @JoinColumn(name = "lecturer_id"),
               inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;
    
    public Lecturer() {
    }

    public Lecturer(String name, String title, String password) {
        this.name = name;
        this.title = title;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Lecturer [id=" + id + ", name=" + name + ", title=" + title + ", password=" + password + ", email="
                + email + ", dob=" + dob + "]";
    }
    
    
}