package sg.edu.nus.iss.CAPS_CA_Team9.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class StudentCourseId implements Serializable{

    private String studentId;
    private Integer courseId;
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StudentCourseId other = (StudentCourseId) obj;
        if (studentId == null) {
            if (other.studentId != null)
                return false;
        } else if (!studentId.equals(other.studentId))
            return false;
        if (courseId != other.courseId)
            return false;
        return true;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((studentId == null) ? 0 : studentId.hashCode());
        result = prime * result + courseId;
        return result;
    }

}
