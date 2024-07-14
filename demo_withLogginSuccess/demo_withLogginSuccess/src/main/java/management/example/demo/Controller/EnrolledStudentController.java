package management.example.demo.Controller;

import management.example.demo.Model.Student;
import management.example.demo.Service.EmailService;
import management.example.demo.Service.EnrolledStudentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void confirmEnrollment(@PathVariable(name = "id") Long id , @RequestParam("action") String action) {
        if ("Approved".equals(action)) {
            Student student = enrolledStudentService.get(id);
            enrolledStudentService.confirm(student);
            //Send an email to the student that informing the confirmation
            String toEmail = enrolledStudentService.get(id).getEmail();
            System.out.println(toEmail);
            String subject = "Your enrollment is confirmed";
            String body = "Successfully enrolled to the " + enrolledStudentService.get(id).getProgramOfStudy();
            emailService.sendMail(toEmail, subject, body);
        }
        else if ("rejected".equals(action)) {
            //Send an email to the student that informing the confirmation
            String toEmail = enrolledStudentService.get(id).getEmail();
            String subject = "Your enrollment is rejected";
            String body = "Rejected enrollment to the" + enrolledStudentService.get(id).getProgramOfStudy() + "\n" +
                    "Your username = " + enrolledStudentService.get(id).getFullName() + "/n" +
                    "Your password = " + enrolledStudentService.get(id).getContactNumber();
            emailService.sendMail(toEmail, subject, body);
        }
    }

    @GetMapping("/enrolledstu")
    public String viewHomePage(Model model) {
        List<Student> liststudent = enrolledStudentService.listAll();
        model.addAttribute("liststudent", liststudent);
        System.out.print("Get / ");
        return "enrolledstu";
    }
}
