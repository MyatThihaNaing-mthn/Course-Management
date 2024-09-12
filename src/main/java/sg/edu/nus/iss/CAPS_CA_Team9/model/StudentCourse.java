package sg.edu.nus.iss.CAPS_CA_Team9.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "studId", "courseId" }) })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(StudentCourseId.class)
public class StudentCourse {

    @Id
    @Column(name = "studId")
    private String studentId;

    @Id
    @Column(name = "courseId")
    private Integer courseId;
    
    @ManyToOne
    @JoinColumn(name = "studId",referencedColumnName = "studId",insertable = false,updatable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "courseId",referencedColumnName = "courseId",insertable = false,updatable = false)
    private Course course;

    @Column(name = "enrollStatus")
    private Integer enrollStatus;
    
    private Double score;

    @Column(name = "enrollDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollDate;

}
