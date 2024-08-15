package management.example.demo.Controller;

import jakarta.mail.MessagingException;
import management.example.demo.Model.Student;
import management.example.demo.Model.User;
import management.example.demo.Repository.UserRepository;
import management.example.demo.Service.EmailService;
import management.example.demo.Service.FileUploadService;
import management.example.demo.Service.NotificationService;
import management.example.demo.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping()
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/enroll")
    public String showEnrollmentForm() {
        return "enroll";  // enroll.html should be your form page
    }

//    @PostMapping("/enroll")
//    public String enrollStudent(Student student, @RequestParam("attachment") MultipartFile attachment, Model model) throws MessagingException {
//
//        // Handle file upload
//        String attachemntFileName = "";
//        if (!attachment.isEmpty()) {
//            attachemntFileName = fileUploadService.uploadFile(attachment);
//            student.setAttachementFile(attachemntFileName);
//        }
//
//        //Save the enrolled student
//        studentService.saveStudent(student);
//        model.addAttribute("Student enrolled successfully!");
//        //Email the administrator to inform the enrollment
//        String toEmail = "dasunikawya2001.1@gmail.com";
//        String subject = "A student Enrollment";
//        String body = "New student has enrolled. \n" + "Name : "
//                + student.getFullName() + "\n" + "Address : "
//                + student.getAddress() + "\n";
//        //Email the administrator informing student enrollment
//        //emailService.sendMail(toEmail , subject, body);
//
//        return "enroll";
//    }

    @PostMapping("/enroll")
    public ResponseEntity<Map<String, String>> enrollStudent(
            @ModelAttribute Student student,
            @RequestParam("attachment") MultipartFile attachment) throws MessagingException {

        // Handle file upload
        String attachemntFileName = "";
        if (!attachment.isEmpty()) {
            attachemntFileName = fileUploadService.uploadFile(attachment);
            student.setAttachementFile(attachemntFileName);
        }

        // Save the enrolled student
        studentService.saveStudent(student);

        // Email the administrator to inform the enrollment
        String toEmail = "dasunikawya2001.1@gmail.com";
        String subject = "A student Enrollment";
        String body = "New student has enrolled. \n" +
                "Name : " + student.getFullName() + "\n" +
                "Address : " + student.getAddress() + "\n";
        emailService.sendMail(toEmail, subject, body);
        User user = userRepository.findByUsername("e20197");
        notificationService.sendNotification(user, subject, body);

        System.out.println("Successfully enrolled.");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student enrolled successfully!");
        return ResponseEntity.ok(response);
    }




}