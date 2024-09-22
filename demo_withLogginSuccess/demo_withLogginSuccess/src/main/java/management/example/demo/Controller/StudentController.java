package management.example.demo.Controller;

import jakarta.mail.MessagingException;
import management.example.demo.Model.EducationalQualification;
import management.example.demo.Model.FileMetadata;
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
            @RequestPart("student") Student student,
            @RequestParam("attachments") List<MultipartFile> attachments,
            @RequestPart("studentIdDocument") MultipartFile studentIdDocument,
            @RequestPart("birthCertificate") MultipartFile birthCertificate) throws MessagingException {

        System.out.println("Received student data: " + student);

        // Process the student data
        if (student.getEducationalQualifications() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Educational qualifications must not be null"));
        }
        List<EducationalQualification> qualifications = student.getEducationalQualifications();
        System.out.println(qualifications);

        int attachmentIndex = 0; // To track attachment file index

        // Iterate over each qualification and its corresponding attachments
        for (EducationalQualification qualification : qualifications) {
            List<FileMetadata> qualificationAttachments = new ArrayList<>();

            // Assuming that each qualification can have multiple attachments
            while (attachmentIndex < attachments.size()) {
                MultipartFile attachment = attachments.get(attachmentIndex);

                // Handle file upload for each attachment
                if (!attachment.isEmpty()) {
                    List<String> attachmentData = fileUploadService.uploadFile(attachment);

                    // Create a new FileMetadata object for each uploaded file
                    FileMetadata fileMetadata = new FileMetadata();
                    fileMetadata.setFileName(attachmentData.get(0)); // Uploaded file path
                    fileMetadata.setOriginalFileName(attachmentData.get(1)); // Original file name

                    qualificationAttachments.add(fileMetadata);
                }

                attachmentIndex++; // Move to the next file
            }

            // Set the uploaded attachments to the qualification
            qualification.setAttachments(qualificationAttachments);

            // Associate the qualification with the student
            qualification.setStudent(student);
        }

        // Handle Student ID Document
        if (!studentIdDocument.isEmpty()) {
            List<String> idDocData = fileUploadService.uploadFile(studentIdDocument);
            student.setStudentIdDocument(idDocData.get(0)); // File path
            student.setStudentIdDocumentOriginalName(idDocData.get(1)); // Original filename
        }

        // Handle Birth Certificate
        if (!birthCertificate.isEmpty()) {
            List<String> birthCertData = fileUploadService.uploadFile(birthCertificate);
            student.setBirthCertificate(birthCertData.get(0)); // File path
            student.setBirthCertificateOriginalName(birthCertData.get(1)); // Original filename
        }

        // Save the student with the associated qualifications and attachments
        studentService.saveStudent(student);

        // Send an email notification to the admin
        String toEmail = "admin@example.com";
        String subject = "New Student Enrollment";
        String body = "A new student has enrolled: " + student.getFullName();
        emailService.sendMail(toEmail, subject, body);

        // Send a notification to the admin user
        User adminUser = userRepository.findByUsername("e20197");
        notificationService.sendNotification(adminUser, subject, body);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Student enrolled successfully!");
        return ResponseEntity.ok(response);
    }
}

//    @PostMapping("/enroll")
//    public ResponseEntity<Map<String, String>> enrollStudent(
//            @ModelAttribute Student student,
//            @RequestParam("attachment") MultipartFile attachment) throws MessagingException {
//
//        // Handle file upload
//
//        List<String> attachemntData = new ArrayList<>();
//        if (!attachment.isEmpty()) {
//            attachemntData = fileUploadService.uploadFile(attachment);
//            student.setAttachementFile(attachemntData.get(0));
//            student.setAttachementFileOriginalName(attachemntData.get(1));
//        }
//
//        // Save the enrolled student
//        studentService.saveStudent(student);
//
//        // Email the administrator to inform the enrollment
//        String toEmail = "dasunikawya2001.1@gmail.com";
//        String subject = "A student Enrollment";
//        String body = "New student has enrolled. \n" +
//                "Name : " + student.getFullName() + "\n" +
//                "Address : " + student.getAddress() + "\n";
//        emailService.sendMail(toEmail, subject, body);
//        //Replace this with the ADMIN USERNAME
//        User user = userRepository.findByUsername("e20500");
//        notificationService.sendNotification(user, subject, body);
//
//        System.out.println("Successfully enrolled.");
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Student enrolled successfully!");
//        return ResponseEntity.ok(response);
//    }
