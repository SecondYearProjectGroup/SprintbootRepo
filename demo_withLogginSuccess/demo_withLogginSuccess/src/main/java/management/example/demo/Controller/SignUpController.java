package management.example.demo.Controller;




import management.example.demo.Model.User;
import management.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class SignUpController {

    @Autowired
    private UserDetailsService userDetailsService;

    private UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String register(Model model, User userDto) {
        model.addAttribute("user", userDto);
        return "signup";
    }

    @PostMapping("/signup")
    public String registerSava(@ModelAttribute("user") User user_, Model model) {
        User user = userService.findByUsername(user_.getUsername());
        if (user != null) {
            model.addAttribute("Userexist", user);
            return "signup";
        }
        userService.save(user_);
        return "redirect:/login";
    }
}
//import management.example.demo.Model.User;
//import management.example.demo.Service.SignUpService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class SignUpController {
//
//    @Autowired
////    private SignUpService signUpService;
////    @Autowired
////    private PasswordEncoder passwordEncoder;
//
//
//    @GetMapping("/signup")
//    public String showSignUpPage(){
//        return "signup";
//    }
//
//    @PostMapping("/signup")
//    public String signUpStudent(User signUp) {
////        String encodedPassword = passwordEncoder.encode(signUp.getPwd());
////        System.out.println("Encoded Password at Registration: " + encodedPassword);
////        signUp.setPwd(encodedPassword);
//        signUpService.save(signUp);
//        return "redirect:/login";
//    }
//
//}



/*
    @PostMapping("/signup")
    public String signup(@ModelAttribute User user) {
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        UserRepository.save(user);
        return "redirect:/login";
    }*/