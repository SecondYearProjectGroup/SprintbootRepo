package management.example.demo.Controller;

import jakarta.mail.MessagingException;
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
    public ResponseEntity<String> confirmEnrollment(@PathVariable(name = "id") Long id, @RequestParam("action") String action) throws MessagingException {

        //Retrieve the student from the student entity using the provided id.
        Student student = enrolledStudentService.get(id);

        //Check the student exiting
        if (student == null) {
            return ResponseEntity.badRequest().body("Student not found.");
        }

        //Handle the APPROVED action
        if ("Approved".equalsIgnoreCase(action)) {
            //Set the status of the student as "Approved" in the student table
            student.setStatus("Approved");
            enrolledStudentService.confirm(student);
            enrolledStudentService.saveStudent(student);


            //To save the confirmed student in an another entity
            //confirmedStudentService.saveStudent();

            //Set the details to the send the email
            String toEmail = student.getEmail();
            String subject = "Your enrollment is confirmed";
            String body = "Your enrollment to the" + enrolledStudentService.get(id).getProgramOfStudy() + "is successfully confirmed." +"\n" +
                    "Your username = " + enrolledStudentService.get(id).getFullName() + "\n" +
                    "Your password = " + enrolledStudentService.get(id).getContactNumber();

            //Send the email
            //emailService.sendMail(toEmail, subject, body);
            //Send email with the attachment of application
            emailService.sendEmailWithAttachment(toEmail , subject, body);

            //Display the message
            return ResponseEntity.ok("Approval email sent successfully.");
        }

        //Handle the REJECT action
        else if ("rejected".equalsIgnoreCase(action)) {
            //Set the status of the student as "Rejected" in the student table
            student.setStatus("Rejected");
            String toEmail = student.getEmail();
            String subject = "Your enrollment is rejected";
            String body = "Your enrollment is rejected.";
            emailService.sendMail(toEmail, subject, body);
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
