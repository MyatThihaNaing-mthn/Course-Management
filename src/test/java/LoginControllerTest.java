import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import sg.edu.nus.iss.CAPS_CA_Team9.controller.LoginController;
import sg.edu.nus.iss.CAPS_CA_Team9.model.LoginForm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
/**
 * @program: test
 * @description: 测试类
 * @author: shenning
 * @create: 2023-06-20 15:44
 **/
class LoginControllerTest {

    @Test
    void testLogin() {
        Model model = mock(Model.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginController loginController = new LoginController();
        String viewName = loginController.login(model);
        assertEquals("redirect:/", viewName);
        Mockito.verify(model).addAttribute(eq("user"), any(LoginForm.class));
    }

    @Test
    void testLogin_WithAnonymousAuthentication() {
        Model model = mock(Model.class);
        SecurityContextHolder.getContext().setAuthentication(null);
        LoginController loginController = new LoginController();
        String viewName = loginController.login(model);
        assertEquals("login", viewName);
        Mockito.verify(model).addAttribute(eq("user"), any(LoginForm.class));
    }
}
