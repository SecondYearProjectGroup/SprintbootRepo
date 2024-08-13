package management.example.demo.Controller;

import jakarta.mail.MessagingException;
import management.example.demo.Model.*;
import management.example.demo.Repository.ExaminerRepository;
import management.example.demo.Repository.SupervisorRepository;
import management.example.demo.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:4200")
//@Controller
@RestController
public class AdminController {

    @Autowired
    private EnrolledStudentService enrolledStudentService;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private ConfirmedStudentService confirmedStudentService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private ExaminerRepository examinerRepository;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private ExaminerService examinerService;

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

    //Show list of enrolled students page to the admin
//    @GetMapping("/enrolledstu")
//    public String viewHomePage(Model model) {
//        List<Student> liststudent = enrolledStudentService.listAll();
//        model.addAttribute("liststudent", liststudent);
//        System.out.print("Get / ");
//        return "enrolledstu";
//    }

    //Handle the confirmation of enrolled
    //@PreAuthorize("hasRole('ADMIN')")
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
            String body = "Your enrollment to the" + enrolledStudentService.get(id).getProgramOfStudy() + "is successfully confirmed." + "\n" +
                    "Your username = " + enrolledStudentService.get(id).getFullName() + "\n" +
                    "Your password = " + enrolledStudentService.get(id).getContactNumber();

            //Send the email
            //emailService.sendMail(toEmail, subject, body);
            //Send email with the attachment of application
            emailService.sendEmailWithAttachment(toEmail, subject, body);

            //Display the message
            return ResponseEntity.ok("Approval email sent successfully.");
        }

        //Handle the REJECT action
        else if ("Rejected".equalsIgnoreCase(action)) {
            //Set the status of the student as "Rejected" in the student table
            student.setStatus("Rejected");
            String toEmail = student.getEmail();
            String subject = "Your enrollment is rejected";
            String body = "Your enrollment is rejected.";
            emailService.sendMail(toEmail, subject, body);
            enrolledStudentService.saveStudent(student);
            return ResponseEntity.ok("Rejection email sent successfully.");
        }
        return ResponseEntity.badRequest().body("Invalid action.");
    }


    /* */
//    @PostMapping("/assignSupervisor/{id}")
//    public ResponseEntity<String> assignSupervisor(@PathVariable(name = "id") Long id, @RequestParam Long supervisorId) {
//        try {
//            // Retrieve the student from the student entity using the provided id.
//            ConfirmedStudent confirmedStudent = confirmedStudentService.get(id);
//
//            if (confirmedStudent == null) {
//                System.out.println("ConfirmedStudent not found for ID: " + id);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
//            }
//
//            // Find the supervisor
//            Optional<Supervisor> supervisorOpt = supervisorRepository.findById(supervisorId);
//
//            if (supervisorOpt.isPresent()) {
//                Supervisor supervisor = confirmedStudentService.assignSupervisor(id, supervisorId);
//
//                // Send the email to the supervisor informing the student's details
//                String toEmail = supervisor.getEmail();
//                String subject = "New Student Assignment Notification ";
//                String body = "Dear " + supervisor.getNameWithInitials() + ",\n\n" +
//                        "You have been assigned a new student.\n\n" +
//                        "Student ID: " + confirmedStudent.getRegNumber() + "\n" +
//                        "Student Name: " + confirmedStudent.getFullName() + "\n" +
//                        "Course/Program: " + confirmedStudent.getProgramOfStudy() + "\n" +
//                        "Please reach out to the student to introduce yourself and outline the next steps.\n\n" +
//                        "Students Contact Details:\n\n" +
//                        "Email: " + confirmedStudent.getEmail() + "\n" +
//                        "Phone: " + confirmedStudent.getContactNumber() + "\n" +
//                        "Thank you for your continued support.\n\n" +
//                        "Best regards,\n" +
//                        "Post Graduate Studies,\n" +
//                        "Department of Computer Engineering,UOP\n" +
//                        "[Your Institution/Organization]";
//
//                emailService.sendMail(toEmail, subject, body);
//                System.out.println("Successfully assigned.");
//                return ResponseEntity.ok("Supervisor assigned successfully.");
//            } else {
//                System.out.println("Supervisor not found for ID: " + supervisorId);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supervisor not found.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while assigning the supervisor.");
//        }
//    }
/* */

    //Assign the Supervisors
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assignSupervisor/{id}")
    public ResponseEntity<String> assignSupervisor(@PathVariable(name = "id") String id, @RequestParam Long supervisorId) {

        //Retrieve the student from the student entity using the provided id.
        ConfirmedStudent confirmedStudent = confirmedStudentService.get(id);

        //
        //System.out.println(confirmedStudent.getRegNumber());

        //Find the supervisor
        //This can be clear after adding "assignSupervisor submit button" click to the if condition
        Optional<Supervisor> supervisorOpt = supervisorRepository.findById(supervisorId);

        //For this if condition button click should be added.
        if (supervisorOpt.isPresent()) {
            Supervisor supervisor = confirmedStudentService.assignSupervisor(id, supervisorId);

            //Send the email to the supervisor informing the student's details
            String toEmail = supervisor.getEmail();
            String subject = "New Student Assignment Notification ";
            String body = "Dear " + supervisor.getFullName() + ",\n\n" +
                    "You have been assigned a new student.\n\n" +
                    //"Student ID: " + confirmedStudent.getRegNumber() + "\n" +
                    "Student Name: " + confirmedStudent.getFullName() + "\n" +
                    "Course/Program: " + confirmedStudent.getProgramOfStudy() + "\n" +
                    "Please reach out to the student to introduce yourself and outline the next steps.\n\n" +
                    "Students Contact Details:\n\n" +
                    "Email: " + confirmedStudent.getEmail() + "\n" +
                    "Phone: " + confirmedStudent.getContactNumber() + "\n" +
                    "Thank you for your continued support.\n\n" +
                    "Best regards,\n" +
                    "Post Graduate Studies,\n" +
                    "Department of Computer Engineering,UOP\n" +
                    "Post Graduate Studies,\n" +
                    "[Your Institution/Organization]";
            System.out.println("Successfully assigned.");
            return ResponseEntity.ok("Supervisor assigned successfully.");
        } else {
            System.out.println("Failed");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student or Supervisor not found.");
        }
    }


    //Assign the Examiners to submissions
    //For each report examiners have to be assigned.
    //For url, report id should be added.
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assignExaminers/{stuId}/{id}")
    public ResponseEntity<String> assignExaminer(@PathVariable(name = "stuId") String stuId, @PathVariable(name = "id") Long id, @RequestParam List<Long> examinerIds) {

        //Retrieve the student from the student entity using the provided id.
        ConfirmedStudent confirmedStudent = confirmedStudentService.get(stuId);
        //Retrieve the submission from the submission entity using the provided id.
        Submission submission = submissionService.get(id);

        List<Examiner> examiners = confirmedStudentService.assignExaminers(id, examinerIds);

        //Send mails to the examiners to informing the submission assignment
        for (Examiner examiner : examiners) {
            String toEmail = examiner.getEmail();
            String subject = "New Report Submission Assignment Notification ";
            String body = String.format(
                    "Dear %s,\n\n" +
                            "We are pleased to inform you that you have been assigned as an examiner for a new submission in our system. The details of the submission are as follows:\n\n" +
                            "Submission Title: %s\n" +
                            "Submission ID: %d\n" +
                            "Student RegNumber: %s\n" +
                            "Student Name: %s\n\n" +
                            "As an examiner, your role will involve reviewing the submission and providing your evaluation and feedback. We greatly value your expertise and look forward to your insights.\n\n" +
                            "Please access the submission through the system at your earliest convenience. Your timely feedback is crucial for the student's progress and will be highly appreciated.\n\n" +
                            "Should you have any questions or need further information, please do not hesitate to contact us.\n\n" +
                            "Best regards,\n" +
                            "Post Graduate Studies,\n" +
                            "Department of Computer Engineering,UOP\n",
                    examiner.getFullName(),
                    submission.getTitle(),
                    submission.getId(),
                    //confirmedStudent.getRegNumber(),
                    confirmedStudent.getFullName()
            );

        }
        return ResponseEntity.ok("Examiners assigned successfully.");
    }


    //Set deadlines for submissions
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/setDeadline/{stuId}/{id}")
    public ResponseEntity<String> setDeadline(@PathVariable(name = "stuId") Long stuId, @PathVariable(name = "id") Long id, @RequestParam Date deadline) {
        Submission submission = submissionService.get(id);
        submission.setDeadline(deadline);
        submissionService.saveSubmissionsParameters(submissionService.get(id));
        System.out.println("Deadline has set successfully.");
        return ResponseEntity.ok("Deadline has set successfully.");
    }

    //Add section to submit the reports to the students
    @PostMapping("/addSubmitSection/{stuId}")
    public void addSectionToSubmit(Submission submission, @RequestParam String title){
        submissionService.addSubmissionField(submission,title);
    }

    //List all enrolledStudents to admin
    @GetMapping("/enrolledstu")
    public List<Student> getAllEnrolledStudents(){
        return enrolledStudentService.listAll();
    }

    //List all supervisors to admin
    @GetMapping("/supervisors")
    public List<Supervisor> getAllSupervisors(){
        return supervisorService.listAll();
    }

    @GetMapping("/examiners")
    public List<Examiner> getAllExaminers(){
        return examinerService.listAll();
    }

}

    //To download as an excel
//    @GetMapping("/download/excel")
//    public void downloadExcel(HttpServletResponse response) throws IOException {
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", "attachment; filename=students.xlsx");
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Students");
//
//        // Fetch the data from your database
//        List<Student> students = enrolledStudentService.listAll(); // Replace with your method to fetch data
//
//        workbook.write(response.getOutputStream());
//        workbook.close();
//    }






//public ResponseEntity<String> assignSupervisor(@PathVariable(name = "id") Long id, @RequestParam Long supervisorId) {
//
//    //Retrieve the student from the student entity using the provided id.
//    ConfirmedStudent confirmedStudent = confirmedStudentService.get(id);
//
//    //Find the supervisor
//    Optional<Supervisor> supervisorOpt = supervisorRepository.findById(supervisorId);
//
//    if (supervisorOpt.isPresent()) {
//        Supervisor supervisor = supervisorOpt.get();
//        confirmedStudent.setSupervisor(supervisor);
//
//        //Send the email to the supervisor informing the student's details
//        String toEmail = supervisor.getEmail();
//        String subject = "New Student Assignment Notification ";
//        String body = "Dear " + supervisor.getNameWithInitials() + ",\n\n" +
//                "You have been assigned a new student.\n\n" +
//                "Student ID: " + confirmedStudent.getRegNumber() + "\n" +
//                "Student Name: " + confirmedStudent.getFullName() + "\n" +
//                "Course/Program: " + confirmedStudent.getProgramOfStudy() + "\n" +
//                "Please reach out to the student to introduce yourself and outline the next steps.\n\n" +
//                "Students Contact Details:\n\n" +
//                "Email: " + confirmedStudent.getEmail() + "\n" +
//                "Phone: " + confirmedStudent.getContactNumber() + "\n" +
//                "Thank you for your continued support.\n\n" +
//                "Best regards,\n" +
//                "Post Graduate Studies,\n" +
//                "Department of Computer Engineering,UOP\n" +
//                "Post Graduate Studies,\n" +
//                "[Your Institution/Organization]";
//
//
//        return ResponseEntity.ok("Supervisor assigned successfully.");
//    } else {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student or Supervisor not found.");
//    }
//}
