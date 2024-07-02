package management.example.demo.Controller;

import ch.qos.logback.core.model.Model;
import management.example.demo.Model.SignUp;
import management.example.demo.Service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @GetMapping("/signup")
    public String showSignUpPage(){
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpStudent(SignUp signUp, Model model) {
        signUpService.saveSignUp(signUp);
        model.addText("Student signed up successfully!");
        return "redirect:/login";
    }
}
