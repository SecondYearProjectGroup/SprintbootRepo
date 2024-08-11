package management.example.demo.Controller;


import management.example.demo.Model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    //Show the logging page
    @GetMapping("/login")
    public String showLoginPage(Model model, User user){
        model.addAttribute("user" , user);
        return "login";
    }


//    @PostMapping("/login")
//    public ResponseEntity<String> login(Authentication authentication) {
//        if (authentication != null) {
//            // Get roles from Authentication object
//            String roles = authentication.getAuthorities().stream()
//                    .map(grantedAuthority -> grantedAuthority.getAuthority())
//                    .collect(Collectors.joining(","));
//
//            return ResponseEntity.ok(roles);
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
//    }


}
