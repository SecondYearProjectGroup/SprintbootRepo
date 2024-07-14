package management.example.demo.Controller;

import management.example.demo.Model.Student;
import management.example.demo.Service.EmailService;
import management.example.demo.Service.EnrolledStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class EnrolledStudentController {

    @Autowired
    private EnrolledStudentService enrolledStudentService;

    @Autowired
    private EmailService emailService;

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditStudentPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("new");
        Student student = enrolledStudentService.get(id);
        mav.addObject("student", student);
        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deletestudent(@PathVariable(name = "id") int id) {
        enrolledStudentService.delete(id);
        return "redirect:/";
    }

    @PostMapping("/handleApproval/{id}")
    //ResponseEntity<String> is used to represent an HTTP response, including status codes, headers, and body
    public ResponseEntity<String> confirmEnrollment(@PathVariable(name = "id") Long id, @RequestParam("action") String action) {

        //Retrieve the student from the student entity using the provided id.
        Student student = enrolledStudentService.get(id);

        //Check the student exiting
        if (student == null) {
            return ResponseEntity.badRequest().body("Student not found.");
        }

        //Handle the APPROVED action
        if ("Approved".equalsIgnoreCase(action)) {
            enrolledStudentService.confirm(student);
            String toEmail = student.getEmail();
            String subject = "Your enrollment is confirmed";
            String body = "Rejected enrollment to the" + enrolledStudentService.get(id).getProgramOfStudy() + "\n" +
                    "Your username = " + enrolledStudentService.get(id).getFullName() + "\n" +
                    "Your password = " + enrolledStudentService.get(id).getContactNumber();
            //emailService.sendMail(toEmail, subject, body);
            return ResponseEntity.ok("Approval email sent successfully.");
        }
        //Handle the REJECT action
        else if ("rejected".equalsIgnoreCase(action)) {
            String toEmail = student.getEmail();
            String subject = "Your enrollment is rejected";
            String body = "Your enrollment is rejected.";
            //emailService.sendMail(toEmail, subject, body);
            return ResponseEntity.ok("Rejection email sent successfully.");
        }
        return ResponseEntity.badRequest().body("Invalid action.");
    }

    @GetMapping("/enrolledstu")
    public String viewHomePage(Model model) {
        List<Student> liststudent = enrolledStudentService.listAll();
        model.addAttribute("liststudent", liststudent);
        System.out.print("Get / ");
        return "enrolledstu";
    }
}
