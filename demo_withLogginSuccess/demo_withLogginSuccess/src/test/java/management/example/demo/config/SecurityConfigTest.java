//package management.example.demo.config;
//
//import management.example.demo.Model.User;
//import management.example.demo.Repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class SecurityConfigTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @BeforeEach
//    public void setUp() {
//        userRepository.deleteAll();
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPwd(passwordEncoder.encode("password"));
//        userRepository.save(user);
//    }
//
//    @Test
//    public void login_ShouldAuthenticateUser() throws Exception {
//        // First, obtain a CSRF token
//        ResultActions resultActions = mockMvc.perform(get("/login").with(csrf()));
//        String csrfToken = resultActions.andReturn().getRequest().getAttribute("_csrf").toString();
//
//        // Perform login request with CSRF token
//        mockMvc.perform(post("/login")
//                        .param("username", "testuser")
//                        .param("password", "password")
//                        .param("_csrf", csrfToken))  // Add CSRF token
//                .andExpect(status().isFound())
//                .andExpect(header().string("Location", "/home"));
//    }
//
//    @Test
//    public void login_ShouldReturnError_WhenCredentialsAreInvalid() throws Exception {
//        // First, obtain a CSRF token
//        ResultActions resultActions = mockMvc.perform(get("/login").with(csrf()));
//        String csrfToken = resultActions.andReturn().getRequest().getAttribute("_csrf").toString();
//
//        // Perform login request with invalid credentials and CSRF token
//        mockMvc.perform(post("/login")
//                        .param("username", "testuser")
//                        .param("password", "wrongpassword")
//                        .param("_csrf", csrfToken))  // Add CSRF token
//                .andExpect(status().isFound())
//                .andExpect(header().string("Location", "/login?error"));
//    }
//}
