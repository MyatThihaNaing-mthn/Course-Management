package sg.edu.nus.iss.CAPS_CA_Team9.model;

//import java.time.*;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;


@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "courseId")
    private Integer id;
    
    @Column (name = "courseName")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    
    @Column (name = "courseStartDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    
    @Column (name = "courseEndDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column (name = "courseTotalCount")
    private int currentCount; // Current enrollment size

    @Column (name = "courseSize")
    @NotNull(message = "Course size cannot be blank")
    @Min(value = 1, message = "Value must be greater than 5")
    private int size;

    @Column (name = "courseCredits")
    @Max(5)
    @Min(value=1, message = "Credit must be between 1-5")
    private double credits;

    @Column (name = "courseDescription")
    @NotBlank(message = "Course description cannot be blank")
    private String description;

    @Column (name = "courseSchedule")
    private String schedule; // Which term is the course offered in

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private List<Lecturer> lecturers;

    public Course() {
    }

    public Course(String name, Date startDate, Date endDate, int size, double credits, String description,
            String schedule) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.size = size;
        this.credits = credits;
        this.description = description;
        this.schedule = schedule;
    }
    
}