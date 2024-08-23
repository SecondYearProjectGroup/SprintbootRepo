package management.example.demo.Controller;

import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Service.ConfirmedStudentService;
import management.example.demo.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfirmedStudentController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ConfirmedStudentService confirmedStudentService;

    //To get the current logging student register number
    //For students - username is their reg number
    @GetMapping("/profile-student")
    public ConfirmedStudent getRebNumber(@RequestHeader ("Authorization") String token){
        String jwtToken = token.substring(7);
        String regNumber = jwtUtil.extractUsername(jwtToken);
        System.out.println(regNumber);
        return confirmedStudentService.get(regNumber);
    }
}
