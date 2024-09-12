package sg.edu.nus.iss.CAPS_CA_Team9.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ActivityInterceptor implements HandlerInterceptor {
    
    private static final Logger activityLogger = LoggerFactory.getLogger("activityLogger");

    //@Autowired
    //private HttpServletRequest request;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true; 
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // get the user's identity
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // get the operation details
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // log the audit information only for grade update
        if ("/lecturer/saveStudentCourses".equals(uri) && "POST".equals(method)) {
            String studentId = request.getParameter("studentId");
            String courseId = request.getParameter("courseId");
            String score = request.getParameter("score");
            // removed score logging from here as it should be logged after updating
            activityLogger.info("User {} updated the grade for student {}, in course {} to {}.", username, studentId, courseId, score);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // do nothing
    }
}
