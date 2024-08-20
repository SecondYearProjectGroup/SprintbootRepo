package management.example.demo.Controller;

import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @GetMapping("/students/{supervisorId}")
    public List<ConfirmedStudent> getConfirmedStudents(@PathVariable Long supervisorId) {
        return supervisorService.getConfirmedStudentsBySupervisorId(supervisorId);
    }

//    @GetMapping("/studentsToSupervisor")
//    public List<ConfirmedStudent> getConfirmedStudents(Principal principal) {
//        // Assuming the username is the supervisorId
//        Long supervisorId = Long.parseLong(principal.getName());
//        return supervisorService.getConfirmedStudentsBySupervisorId(supervisorId);
//    }

}
