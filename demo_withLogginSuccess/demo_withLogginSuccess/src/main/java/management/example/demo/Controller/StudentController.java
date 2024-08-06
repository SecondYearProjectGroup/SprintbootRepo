package management.example.demo.Controller;

import jakarta.mail.MessagingException;
import management.example.demo.Model.Student;
import management.example.demo.Service.EmailService;
import management.example.demo.Service.FileUploadService;
import management.example.demo.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping()
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/enroll")
    public String showEnrollmentForm() {
        return "enroll";  // enroll.html should be your form page
    }

    @PostMapping("/enroll")
    public String enrollStudent(Student student, @RequestParam("attachment") MultipartFile attachment, Model model) throws MessagingException {

        // Handle file upload
        String attachemntFileName = "";
        if (!attachment.isEmpty()) {
            attachemntFileName = fileUploadService.uploadFile(attachment);
            student.setAttachementFile(attachemntFileName);
        }

        //Save the enrolled student
        studentService.saveStudent(student);
        model.addAttribute("Student enrolled successfully!");
        //Email the administrator to inform the enrollment
        String toEmail = "dasunikawya2001.1@gmail.com";
        String subject = "A student Enrollment";
        String body = "New student has enrolled. \n" + "Name : "
                + student.getFullName() + "\n" + "Address : "
                + student.getAddress() + "\n";
        //Email the administrator informing student enrollment
        //emailService.sendMail(toEmail , subject, body);

        return "enroll";
    }

//    @PostMapping("/enroll")
//    public ResponseEntity<String> enrollStudent(@RequestBody Student student) {
//        try {
//            // Save the enrolled student
//            studentService.saveStudent(student);
//
//            // Email the administrator to inform the enrollment
//            String toEmail = "dasunikawya2001.1@gmail.com";
//            String subject = "A student Enrollment";
//            String body = "New student has enrolled. \n" + "Name : "
//                    + student.getFullName() + "\n" + "Address : "
//                    + student.getAddress() + "\n";
//            //Email the administrator informing student enrollment
//            //emailService.sendMail(toEmail, subject, body);
//
//            System.out.println("Successful");
//            return ResponseEntity.ok("Student enrolled successfully!");
//        } catch (Exception e) {
//            System.out.println("Failed");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Enrollment failed.");
//        }
//    }

}