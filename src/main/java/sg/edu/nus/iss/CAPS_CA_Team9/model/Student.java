package sg.edu.nus.iss.CAPS_CA_Team9.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
//import java.time.*;
import java.util.*;

@Entity
@Getter
@Setter
public class Student {
    @Id
    @Column(name = "studId")
    private String id;

    @Column(name = "stuFirstName")
    @NotBlank(message = "Name cannot be blank")
    private String firstName;

    @Column(name = "stuLastName")
    @NotBlank(message = "Name cannot be blank")
    private String lastName;

    @Column(name = "email")
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @Column(name = "stuGpa")
    private Double gpa;

    @Column(name = "stuPwd")
    private String password;

    @Column(name = "stuEnrollDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date enrollmentDate;

    @Column(name="stuDob")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @OneToMany(mappedBy = "student")
    private List<StudentCourse> enrollment; 
    
    public Student() {
    }

    public Student(String firstName, String lastName, String email, double gpa, String password,
            Date enrollmentDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gpa = gpa;
        this.password = password;
        this.enrollmentDate = enrollmentDate;
    }

}

