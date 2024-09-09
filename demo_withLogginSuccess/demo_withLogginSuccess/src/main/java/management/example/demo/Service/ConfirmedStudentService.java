package management.example.demo.Service;

import management.example.demo.DTO.StudentSupervisorDto;
import management.example.demo.Model.*;
import management.example.demo.Repository.ConfirmedStudentRepository;
import management.example.demo.Repository.ExaminerRepository;
import management.example.demo.Repository.SubmissionRepository;
import management.example.demo.Repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfirmedStudentService {

    @Autowired
    private ConfirmedStudentRepository confirmedStudentRepository;

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private ExaminerRepository examinerRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;


    public List<ConfirmedStudent> listAll() {
        return confirmedStudentRepository.findAll();
    }

    public ConfirmedStudent get(String regNumber){
        return confirmedStudentRepository.findById(regNumber).get();
    }

    //Get all the submissions of a student
    public List<Submission> getAllSubmissions(String regNumber){
        ConfirmedStudent confirmedStudent = get(regNumber);
        return submissionRepository.findByConfirmedStudent(confirmedStudent);
    }

    //Assign Supervisor
    public Supervisor assignSupervisor(String  regNumber, Long supervisorId){
        //Find the supervisor
        Optional<Supervisor> supervisorOpt = supervisorRepository.findById(supervisorId);
        if (supervisorOpt.isPresent()){

            //Get the supervisor from the database
            //Get the student from the database
            Supervisor supervisor = supervisorOpt.get();
            ConfirmedStudent confirmedStudent = get(regNumber);

            //Get the current supervisor
            Supervisor currentSupervisor = confirmedStudent.getSupervisor();

            if (currentSupervisor != null && !currentSupervisor.getId().equals(supervisorId)){
                // Decrement the number of supervisees for the current supervisor
                currentSupervisor.setNoOfSupervisees(currentSupervisor.getNoOfSupervisees() - 1);
                System.out.println(currentSupervisor.getId()+" \n" +  currentSupervisor.getFullName());
                supervisorRepository.save(currentSupervisor); // Save the updated supervisor

                // Remove the student from the old supervisor's supervisee list
                currentSupervisor.getSupervisees().remove(confirmedStudent);
                supervisorRepository.save(currentSupervisor); // Save after removing the student

                // Send a notification to the old supervisor
                String currentSupervisorEmail = currentSupervisor.getEmail();
                String currentSupervisorSubject = "Student Reassignment Notification";
                String currentSupervisorBody = "Dear " + currentSupervisor.getFullName() + ",\n\n" +
                        "The student " + confirmedStudent.getFullName() + " (ID: " + confirmedStudent.getRegNumber() +
                        ") has been reassigned to another supervisor.\n\n" +
                        "Thank you for your support.\n\n" +
                        "Best regards,\n" +
                        "Post Graduate Studies,\n" +
                        "Department of Computer Engineering, UOP";
                emailService.sendMail(currentSupervisorEmail, currentSupervisorSubject, currentSupervisorBody);
            }

            // Ensure the student is not already in the new supervisor's list
            if (!supervisor.getSupervisees().contains(confirmedStudent)) {
                //Set the selected supervisor
                //Again save the student
                confirmedStudent.setSupervisor(supervisor);
                confirmedStudentRepository.save(confirmedStudent);
                // Add the student to the new supervisor's supervise list
                List<ConfirmedStudent> supervisees = supervisor.getSupervisees();
                supervisees.add(confirmedStudent);
                //Increment the number of supervises
                supervisor.setNoOfSupervisees(supervisor.getNoOfSupervisees() + 1);
                supervisor.setSupervisees(supervisees);

                // Save the updated supervisor
                supervisorRepository.save(supervisor);

                // Send a notification to the new supervisor
                String newSupervisorEmail = supervisor.getEmail();
                String subject = "New Student Assignment Notification";
                String body = "Dear " + supervisor.getFullName() + ",\n\n" +
                        "You have been assigned to a new student.\n\n" +
                        "Student ID: " + confirmedStudent.getRegNumber() + "\n" +
                        "Student Name: " + confirmedStudent.getFullName() + "\n" +
                        "Course/Program: " + confirmedStudent.getProgramOfStudy() + "\n" +
                        "Please reach out to the student to introduce yourself and outline the next steps.\n\n" +
                        "Student's Contact Details:\n\n" +
                        "Email: " + confirmedStudent.getEmail() + "\n" +
                        "Phone: " + confirmedStudent.getContactNumber() + "\n" +
                        "Thank you for your continued support.\n\n" +
                        "Best regards,\n" +
                        "Post Graduate Studies,\n" +
                        "Department of Computer Engineering, UOP";
                emailService.sendMail(newSupervisorEmail, subject, body);
                Optional<User> userSupervisor =  userService.findById(supervisorId);
                User user = userSupervisor.get();
                notificationService.sendNotification(user, subject, "You have assigned to new student");
            }
            return supervisor;
        }
        else {
            return null;
        }
    }


    //Assign Examiners to each student's report submissions
    public List<Examiner> assignExaminers(Long submissionId, List<Long> examinerIds) {
        Optional<Submission> submissionOpt = submissionRepository.findById(submissionId);

        if (submissionOpt.isPresent()) {
            Submission submission = submissionOpt.get();

            //Iterate over the list of examiner ids, find each examiner and assign them
            for (Long examinerId : examinerIds) {
                Optional<Examiner> examinerOpt = examinerRepository.findById(examinerId);
                if (examinerOpt.isPresent()) {
                    Examiner examiner = examinerOpt.get();
                    submission.getExaminers().add(examiner);

                    //Get the list of submissions assigned to the examiners
                    //Add the newly added submission to the existing list of submissions
                    //Save them
                    List<Submission> submissions = examiner.getSubmissions();
                    submissions.add(submission);
                    examinerRepository.save(examiner);
                }
            }
            return submission.getExaminers();
        }
        // Handle the case where the submission is not found.
        return Collections.emptyList();
    }

    //Find the students by the submission id
    public ConfirmedStudent findConfirmedStudentBySubmissionID(Long submissionId){
        return confirmedStudentRepository.findBySubmissions_Id(submissionId);
    }

    //Edit details
    public ConfirmedStudent editDetails(String regNumber){
        return confirmedStudentRepository.save(get(regNumber));
    }

    public ConfirmedStudent save(ConfirmedStudent confirmedStudent){
        return confirmedStudentRepository.save(confirmedStudent);
    }

    public List<StudentSupervisorDto> getStudentRegNumbersAndSupervisorNames() {
        List<ConfirmedStudent> students = confirmedStudentRepository.findAllBy();

        return students.stream()
                .map(confirmedStudent -> new StudentSupervisorDto(
                        confirmedStudent.getRegNumber(),
                        confirmedStudent.getRegistrationNumber(),
                        confirmedStudent.getNameWithInitials(),
                        confirmedStudent.getSupervisor() != null ? confirmedStudent.getSupervisor().getFullName() : "No Supervisor"))
                .collect(Collectors.toList());
    }


}
