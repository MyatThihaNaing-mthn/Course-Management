package sg.edu.nus.iss.CAPS_CA_Team9.validator;

import java.util.Calendar;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Admin;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Course;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Lecturer;
import sg.edu.nus.iss.CAPS_CA_Team9.model.Student;
import sg.edu.nus.iss.CAPS_CA_Team9.model.StudentCourse;

@Component
public class CommonValidator implements Validator {
    
    @Override
    public boolean supports(Class<?> clazz) {
        return Course.class.isAssignableFrom(clazz)
                || Student.class.isAssignableFrom(clazz)
                || Lecturer.class.isAssignableFrom(clazz)
                || StudentCourse.class.isAssignableFrom(clazz)
                || Admin.class.isAssignableFrom(clazz); // Added to use initbinder
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj instanceof Course) {
            validateCourse((Course) obj, errors);
        } else if (obj instanceof Student) {
            validateStudent((Student) obj, errors);
        } else if (obj instanceof Lecturer) {
            validateLecturer((Lecturer) obj, errors);
        }
    }

    private void validateCourse(Course course, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "error.startDate", "Start date is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "error.endDate", "End date is required");
        
        if (course.getStartDate() != null) {
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(course.getStartDate());

            Calendar minStartDate = Calendar.getInstance();
            minStartDate.add(Calendar.DAY_OF_YEAR, 7);

            if (startDate.before(Calendar.getInstance()) && startDate.before(minStartDate)) {
                errors.rejectValue("startDate", "error.dates",
                        "Start date must be at least 7 days after the current date");
            }
        }
        if ((course.getStartDate() != null && course.getEndDate() != null) &&
                (course.getStartDate().compareTo(course.getEndDate()) > 0)) {
            errors.rejectValue("endDate", "error.dates", "End Date must be later than Start Date");
        }
    }

    private void validateStudent(Student student, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dob", "error.dob", "Date of birth is required.");
    }

    private void validateLecturer(Lecturer lecturer, Errors errors) {
       ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dob", "error.dob", "Date of birth is required.");
    }

}
