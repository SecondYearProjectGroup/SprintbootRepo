package management.example.demo.Service;

import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Model.Examiner;
import management.example.demo.Model.Submission;
import management.example.demo.Model.Supervisor;
import management.example.demo.Repository.ConfirmedStudentRepository;
import management.example.demo.Repository.ExaminerRepository;
import management.example.demo.Repository.SubmissionRepository;
import management.example.demo.Repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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


    public ConfirmedStudent get(long id){
        return confirmedStudentRepository.findById(id).get();
    }

    //Assign Supervisor
    public Supervisor assignSupervisor(Long id, Long supervisorId){
        //Find the supervisor
        Optional<Supervisor> supervisorOpt = supervisorRepository.findById(supervisorId);
        if (supervisorOpt.isPresent()){
            Supervisor supervisor = supervisorOpt.get();
            ConfirmedStudent confirmedStudent = get(id);
            confirmedStudent.setSupervisor(supervisor);
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
                }
            }
            return submission.getExaminers();
        }
        // Handle the case where the submission is not found.
        return Collections.emptyList();
    }

}
