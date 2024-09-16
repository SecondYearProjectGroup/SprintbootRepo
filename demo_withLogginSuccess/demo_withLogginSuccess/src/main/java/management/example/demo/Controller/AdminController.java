package management.example.demo.Controller;

import jakarta.mail.MessagingException;
import management.example.demo.DTO.StudentSubmissionExaminerDto;
import management.example.demo.DTO.StudentSupervisorDto;
import management.example.demo.Model.*;
import management.example.demo.Repository.ExaminerRepository;
import management.example.demo.Repository.StudentRepository;
import management.example.demo.Repository.SupervisorRepository;
import management.example.demo.Service.*;
import management.example.demo.Util.JwtUtil;
import management.example.demo.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
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
    
    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private  TileService tileService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private VivaService vivaService;
    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditStudentPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("new");
        Student student = enrolledStudentService.get(id);
        mav.addObject("student", student);
        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteStudent(@PathVariable(name = "id") int id) {
        enrolledStudentService.delete(id);
        return "redirect:/";
    }

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
            ConfirmedStudent confirmedStudent = enrolledStudentService.saveStudent(student);

            //Set the details to the send the email
            String toEmail = student.getEmail();
            String subject = "Your enrollment has confirmed";
            String body = "Your enrollment to the " + confirmedStudent.getProgramOfStudy() + " is successfully confirmed." + "\n" +
                    "Reg Number : " + confirmedStudent.getRegNumber() + "\n" +
//                    "Username : " + confirmedStudent.getUsername() + "\n" +
                    "Password : " + confirmedStudent.getContactNumber();

            //Send the email
            //emailService.sendMail(toEmail, subject, body);
            //Send email with the attachment of application
            emailService.sendEmailWithAttachment(toEmail, subject, body);

            //Set the status of the student as "Approved" in the student table
            student.setStatus("Approved");
            studentRepository.save(student);

            //Display the message
            return ResponseEntity.ok("Approval email sent successfully.");
        }

        //Handle the REJECT action
        else if ("Rejected".equalsIgnoreCase(action)) {
            //Set the status of the student as "Rejected" in the student table
            student.setStatus("Rejected");
            studentRepository.save(student);
            String toEmail = student.getEmail();
            String subject = "Your enrollment is rejected";
            String body = "Your enrollment is rejected.";
            emailService.sendMail(toEmail, subject, body);

            return ResponseEntity.ok("Rejection email sent successfully.");
        }
        return ResponseEntity.badRequest().body("Invalid action.");
    }


    @PostMapping("/addStaffMembers")
    public void addStaffMembers(@RequestParam String name, @RequestParam String email, @RequestParam List<String> role) throws Exception {

        // Convert the list of strings to the set of Role enums
        Set<Role> roles = role.stream()
                .map(roleId -> Role.valueOf(roleId.toUpperCase()))
                .collect(Collectors.toSet());

        adminService.addStaff(name, email, roles);
    }

    // Controller endpoint to assign or change supervisor
    @PostMapping("/assignSupervisor/{regNumber}")
    public ResponseEntity<String> assignSupervisor(@PathVariable(name = "regNumber") String regNumber, @RequestParam Long supervisorId) {

        // Retrieve the student using the provided registration number
        ConfirmedStudent confirmedStudent = confirmedStudentService.get(regNumber);

        // Find the new supervisor by ID and reassign supervisor
        Supervisor newSupervisor = confirmedStudentService.assignSupervisor(regNumber, supervisorId);

        if (newSupervisor != null) {
            return ResponseEntity.ok("Supervisor reassigned successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("New supervisor not found.");
        }
    }


    //Assign the Examiners to submissions
    //For each report examiners have to be assigned.
    //For url, report id should be added
    // Here whenever the examiners are assigned feedback forms has to create.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assignExaminers/{SubmissionId}")
    public ResponseEntity<String> assignExaminer( @PathVariable(name = "SubmissionId") Long submissionId, @RequestParam List<Long> examinerIds) {

        //Retrieve the submission from the submission entity using the provided id.
        Submission submission = submissionService.get(submissionId);

        List<Examiner> examiners = confirmedStudentService.assignExaminers(submissionId, examinerIds);

        //Send mails to the examiners to informing the submission assignment
        for (Examiner examiner : examiners) {
            String toEmail = examiner.getEmail();
            String subject = "You have been assigned as an examiner for a new submission";
            String body = String.format(
                    "Dear %s,\n\n" +
                            "We are pleased to inform you that you have been assigned as an examiner for a new submission in our system. The details of the submission are as follows:\n\n" +
                            "Submission Title: %s\n" +
                            "Submission ID: %d\n" +
//                            "Student RegNumber: %s\n" +
//                            "Student Name: %s\n\n"  +
                            "Please access the submission through the system at your earliest convenience. Your timely feedback is crucial for the student's progress and will be highly appreciated.\n\n" +
                            "Best regards,\n" +
                            "Post Graduate Studies,\n" +
                            "Department of Computer Engineering,UOP\n",
                    examiner.getFullName(),
                    submission.getTitle(),
                    submission.getTile().getId()
                    //confirmedStudent.getRegNumber(),
                    //confirmedStudent.getFullName()
            );

            //Send the emails to examiners
            emailService.sendMail(toEmail,subject,body);
            //Push the  notifications
            String notificationBody = "You have been assigned as an examiner for a new submission";
            Optional<User> userExaminer =  userService.findById(examiner.getId());
            User user = userExaminer.get();
            notificationService.sendNotification(user, subject, notificationBody);

            //Forming the feedback forms
            Feedback feedback = new Feedback();
            feedback.setSubmission(submission);
            feedback.setType("final");
            feedback.setExaminer(examiner);
            feedbackService.saveForum(feedback);

        }
        return ResponseEntity.ok("Examiners assigned successfully.");
    }


    //Set deadlines for submissions
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/setDeadline/{tileId}")
    public ResponseEntity<String> setDeadline( @PathVariable(name = "tileId") Long  tileId,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime opendate) {
        Submission submission = submissionService.get(tileId);
        if (submission == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Submission not found");
        }
        submission.setDeadline(deadline);
        // Set the openDate to the current date and time
        submission.setOpenDate(opendate);
        submissionService.saveSubmissionsParameters(submissionService.get(tileId));

        ////////////////////////////////
        //Generate the Email Notifications for the students
        ConfirmedStudent confirmedStudent = submission.getConfirmedStudent();
        String toEmail = confirmedStudent.getEmail();
        String subject = "Deadline Set for Submission of Progress Reports";
        String body = String.format(
                "This is a reminder that the deadline for submitting your progress reports has been set.\n\n" +
                        "Deadline: %s \n \n" +
                        "Please ensure that your reports are submitted by the specified deadline. " +
                        "Late submissions may not be accepted or could result in a penalty," +
                        "Complete and upload your reports on time." +
                        "If you have any questions or need further clarification, please don't hesitate to reach out your supervisor."
                        , submission.getDeadline()
        );
        emailService.sendMail(toEmail, subject, body);
        /////////////////////////////////
        //Generate the Notifications for the students
        //Get the userId from the student registration number
        User user = userService.findByUsername(confirmedStudent.getRegNumber());
        String notificationBody = "Deadline for submitting your progress reports has been set.";
        notificationService.sendNotification(user, subject, notificationBody);
        /////////////////////////////////

        System.out.println("Deadline has set successfully.");
        return ResponseEntity.ok("Deadline has set successfully.");
    }

    //Upload the assignment task

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

    //List all confirmedStudents to admin
    @GetMapping("/students")
    public List<ConfirmedStudent> getAllConfirmedStudents(){
        return confirmedStudentService.listAll();
    }

    //List all supervisors to admin
    @GetMapping("/supervisorsToAdmin")
    public List<Supervisor> getAllSupervisors(){
        List<Supervisor> list = supervisorService.listAll();
        return list;
    }

    //List supervisors removing Selected Supervisors
    @GetMapping("/supervisors/{regNumber}")
    public List<Supervisor> getNotAssignedSupervisors(@PathVariable(name = "regNumber") String regNumber){
        Supervisor assignedSupervisor = supervisorService.getByStudentRegNumber(regNumber);
        List<Supervisor> list = supervisorService.listAll();
        list.remove(assignedSupervisor);
        return list;
    }

    // List all examiners to admin
    @GetMapping("/examinersToAdmin")
    public List<Examiner> getAllExaminers(){
        return examinerService.listAll(); // All examiners
    }

    // List examiners removing Selected Examiners
//    @GetMapping("/examiners/{submissionId}")
//    public List<Examiner> getNotAssignedExaminers(@PathVariable(name = "submissionId") Long submissionId){
//        List<Examiner> list1 = examinerService.findBySubmissionId(submissionId); // Examiners assigned to the submission
//        List<Examiner> list2 = examinerService.listAll(); // All examiners
//        // Remove all examiners in list1 from list2
//        list2.removeAll(list1);
//        return list2; // Return remaining examiners in list2 not in list1
//    }

//    @GetMapping("/examiners/{submissionId}")
//    public List<Examiner> getNotAssignedExaminers(@PathVariable(name = "submissionId") Long submissionId) {
//        // Retrieve the examiner related to the supervisor of the confirmed student from the tile
//        Optional<Examiner> supervisorExaminer = tileService.getTile(submissionId)  // Get Tile by submissionId
//                .map(Tile::getConfirmedStudent)   // Extract ConfirmedStudent from Tile
//                .map(ConfirmedStudent::getSupervisor)  // Extract Supervisor from ConfirmedStudent
//                .map(Supervisor::getId)  // Extract Supervisor's userId
//                .flatMap(userId -> examinerService.getExaminer(userId));  // Find Examiner by userId if Supervisor is an Examiner
//
//        // Get list of examiners already assigned to the submission
//        List<Examiner> assignedExaminers = examinerService.findBySubmissionId(submissionId);
//
//        // Get list of all examiners in the system
//        List<Examiner> allExaminers = examinerService.listAll();
//
//        // Remove assigned examiners from the list of all examiners
//        allExaminers.removeAll(assignedExaminers);
//
//        // Remove supervisor (if found and they are an examiner) from the list of all examiners
//        supervisorExaminer.ifPresent(allExaminers::remove);
//
//        // Return the remaining examiners who are not assigned and not the supervisor
//        return allExaminers;
//    }

    @GetMapping("/examiners/{submissionId}")
    public List<Examiner> getNotAssignedExaminers(@PathVariable(name = "submissionId") Long submissionId) {
        // Step 1: Get the Tile by submissionId
        Optional<Tile> tile = tileService.getTile(submissionId);
        tile.ifPresentOrElse(
                t -> System.out.println("Tile found: " + t),
                () -> System.out.println("No Tile found for submissionId: " + submissionId)
        );

        // Step 2: Extract ConfirmedStudent from the Tile
        Optional<ConfirmedStudent> confirmedStudent = tile.map(Tile::getConfirmedStudent);
        confirmedStudent.ifPresentOrElse(
                cs -> System.out.println("ConfirmedStudent found: " + cs),
                () -> System.out.println("No ConfirmedStudent found in the Tile.")
        );

        // Step 3: Extract Supervisor from the ConfirmedStudent
        Optional<Supervisor> supervisor = confirmedStudent.map(ConfirmedStudent::getSupervisor);
        supervisor.ifPresentOrElse(
                s -> System.out.println("Supervisor found: " + s),
                () -> System.out.println("No Supervisor found for the ConfirmedStudent.")
        );

        // Step 4: Extract Supervisor's userId
        Optional<Long> supervisorId = supervisor.map(Supervisor::getId);
        supervisorId.ifPresentOrElse(
                id -> System.out.println("Supervisor's userId: " + id),
                () -> System.out.println("No userId found for the Supervisor.")
        );

        // Step 5: Find Examiner by Supervisor's userId
        Optional<Examiner> supervisorExaminer = supervisorId.flatMap(userId -> examinerService.getExaminer(userId));
        supervisorExaminer.ifPresentOrElse(
                examiner -> System.out.println("Examiner found for Supervisor: " + examiner),
                () -> System.out.println("No Examiner found for Supervisor's userId.")
        );

        // Continue with the rest of your logic...
        // Get list of examiners assigned to the submission
        List<Examiner> assignedExaminers = examinerService.findBySubmissionId(submissionId);

        // Get list of all examiners
        List<Examiner> allExaminers = examinerService.listAll();

        // Remove all assigned examiners from the list of all examiners
        allExaminers.removeAll(assignedExaminers);

        // If the examiner related to the userId is present, remove them as well
        supervisorExaminer.ifPresent(allExaminers::remove);

        // Return the remaining examiners
        return allExaminers;
    }



    @GetMapping("/studentProfileForAdmin/{regNumber}")
    public ConfirmedStudent getConfirmedStudentByRegNumber(@RequestHeader("Authorization") String token,
                                                           @PathVariable(name = "regNumber") String regNumber) {
        String decodedRegNumber = java.net.URLDecoder.decode(regNumber, StandardCharsets.UTF_8);
        String jwtToken = token.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);
        System.out.println("Received regNumber: " + decodedRegNumber);
        return confirmedStudentService.get(decodedRegNumber);
    }

    //Upload the assighment tasks
    @PostMapping("/")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") List<MultipartFile> files,
                                               @PathVariable(name = "tileId") Long tileId){
        try {
            // Upload files and get the metadata
            List<FileMetadata> uploadResults = fileService.uploadFiles_(files);

            // Retrieve the existing submission
            Submission submission = submissionService.get(tileId);

            if (submission == null) {
                return new ResponseEntity<>("Submission not found", HttpStatus.NOT_FOUND);
            }


            //Set the assignment task here (Unique name and the original name)
            //submission.setAssignmentTask();

            // Save the updated submission
            submissionService.saveSubmissionsParameters(submission);

            return new ResponseEntity<>("Files uploaded and submission updated successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Edit student details
    @PostMapping(value = "/editDetailsByAdmin/{regNumber}", consumes = "application/json")
    public ResponseEntity<String> editStudentDetails(
            @PathVariable(name = "regNumber") String regNumber,
            @RequestBody ConfirmedStudent updatedStudent) {

        // Find existing student by registration number
        ConfirmedStudent existingStudent = confirmedStudentService.get(regNumber);

        // Update the necessary fields
        if (updatedStudent.getStatus() != null) {
            //System.out.println(updatedStudent.getStatus());
            existingStudent.setStatus(updatedStudent.getStatus());
        }
        if (updatedStudent.getRegisteredDate() != null) {
            //System.out.println(updatedStudent.getRegisteredDate());
            existingStudent.setRegisteredDate(updatedStudent.getRegisteredDate());
        }
        if (updatedStudent.getRegistrationNumber() != null) {
            //System.out.println(updatedStudent.getRegNumber());
            existingStudent.setRegistrationNumber(updatedStudent.getRegistrationNumber());
        }


        confirmedStudentService.editDetails(regNumber);
        // Create a response map
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student details updated successfully.");

        return ResponseEntity.ok(response.toString());
    }

    //Get assigned supervisors
    @GetMapping("/assignedSupervisors")
    public List<StudentSupervisorDto> getAssignedSupervisors() {
        return confirmedStudentService.getStudentRegNumbersAndSupervisorNames();
    }

    @GetMapping("/report-submissions-examiners")
    public ResponseEntity<List<StudentSubmissionExaminerDto>> getAllStudentSubmissions() {
        List<StudentSubmissionExaminerDto> submissions = submissionService.getAllStudentSubmissions();
        return ResponseEntity.ok(submissions);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/setVivaDate/{tileId}")
    public ResponseEntity<String> setVivaDate( @PathVariable(name = "tileId") Long  tileId,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline) {
        Viva viva = vivaService.get(tileId);
        if ( viva == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viva not found");
        }
        viva.setVivaDate(deadline);
        vivaService.saveViva(viva);

        ////////////////////////////////
        //Generate the Email Notifications for the students
        ConfirmedStudent confirmedStudent = viva.getConfirmedStudent();
        String toEmail = confirmedStudent.getEmail();
        String subject = "Year-End Evaluation Viva Scheduled";
        String body = String.format(
                "This is a reminder that your year end evaluations viva has been scheduled.\n\n" +
                        "Date: %s \n \n" +
                        "Please make sure to be prepared and arrive on time. " +
                        "If you have any questions or need further information, feel free to reach out. "
                , viva.getVivaDate()
        );
        emailService.sendMail(toEmail, subject, body);
        /////////////////////////////////
        //Generate the Notifications for the students
        //Get the userId from the student registration number
        User user = userService.findByUsername(confirmedStudent.getRegNumber());
        String notificationBody = "Your year end evaluations viva has been scheduled";
        notificationService.sendNotification(user, subject, notificationBody);
        /////////////////////////////////

        System.out.println("Year end evaluation viva date has set successfully.");
        return ResponseEntity.ok("Year end evaluation viva date has set successfully.");
    }

    //Set deadlines for submission reviewing for examiners
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/setDeadlineToReview/{tileId}")
    public ResponseEntity<String> setDeadlineToReview( @PathVariable(name = "tileId") Long  tileId,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline) {
        Submission submission = submissionService.get(tileId);
        if (submission == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Submission not found");
        }
        submission.setDeadlineToReview(deadline);
        submissionService.saveSubmissionsParameters(submissionService.get(tileId));

        ////////////////////////////////
        //Generate the Email Notifications for the students
        List<Examiner> examiners = submission.getExaminers();
        for (Examiner examiner: examiners){
            String toEmail = examiner.getEmail();
            String subject = "Reminder to Review Assigned Student Reports";
            String body = String.format(
                    "Please be kind enough to provide feedback or examine the assigned reports " +
                            "within the given time period. The deadline for submission is  %s \n \n"
                    , submission.getDeadlineToReview()
            );
            emailService.sendMail(toEmail, subject, body);
            /////////////////////////////////
            //Generate the Notifications for the students
            //Get the userId from the student registration number
            Optional<User> userOpt = userService.findById(examiner.getId());
            User user = userOpt.get();
            String notificationBody  = String.format(
                    "Examine the assigned reports " +
                            "within the given time period. The deadline for submission is  %s \n \n"
                    , submission.getDeadlineToReview()
            );
            notificationService.sendNotification(user, subject, notificationBody);
            /////////////////////////////////
        }
        System.out.println("Deadline has set to review the report submissions successfully.");
        return ResponseEntity.ok("Deadline has set successfully.");
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