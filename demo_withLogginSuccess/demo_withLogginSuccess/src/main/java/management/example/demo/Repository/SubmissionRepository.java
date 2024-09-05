package management.example.demo.Repository;

import jakarta.transaction.Transactional;
import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Model.Submission;
import management.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    Optional<Submission> findById(Long id);

    List<Submission> findByConfirmedStudent(ConfirmedStudent confirmedStudent);

    User save(User user);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM submission_examiner WHERE submission_id = :submissionId AND examiner_id = :examinerId", nativeQuery = true)
    void removeExaminerFromSubmission(@Param("submissionId") Long submissionId, @Param("examinerId") Long examinerId);


}
