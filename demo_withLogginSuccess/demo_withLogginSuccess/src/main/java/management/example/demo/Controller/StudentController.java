package management.example.demo.Controller;

import jakarta.mail.MessagingException;
import management.example.demo.Model.Student;
import management.example.demo.Model.User;
import management.example.demo.Repository.UserRepository;
import management.example.demo.Service.EmailService;
import management.example.demo.Service.FileService;
import management.example.demo.Service.NotificationService;
import management.example.demo.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping()
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private FileService fileUploadService;

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

    @PostMapping("/enroll")
    public ResponseEntity<Map<String, String>> enrollStudent(
            @ModelAttribute Student student,
            @RequestParam("attachment") MultipartFile attachment) throws MessagingException {

        // Handle file upload

        List<String> attachemntData = new ArrayList<>();
        if (!attachment.isEmpty()) {
            attachemntData = fileUploadService.uploadFile(attachment);
            student.setAttachementFile(attachemntData.get(0));
            student.setAttachementFileOriginalName(attachemntData.get(1));
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
        //Replace this with the ADMIN USERNAME
        User user = userRepository.findByUsername("e20500");
        notificationService.sendNotification(user, subject, body);

        System.out.println("Successfully enrolled.");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student enrolled successfully!");
        return ResponseEntity.ok(response);
    }
}