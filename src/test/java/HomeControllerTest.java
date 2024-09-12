import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sg.edu.nus.iss.CAPS_CA_Team9.controller.HomeController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * @program: test
 * @description: 测试类
 * @author: shenning
 * @create: 2023-06-20 16:01
 **/
public class HomeControllerTest {

    @Test
    public void testHome() throws Exception {
        HomeController homeController = new HomeController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/home"));
    }
}
