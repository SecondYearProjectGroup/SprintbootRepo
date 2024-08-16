package management.example.demo.Repository;

import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConfirmedStudentRepository extends JpaRepository<ConfirmedStudent, Long> {
    User save(User user);

//    Optional<ConfirmedStudent> findTopByOrderByIdDesc();

    @Query("SELECT COUNT(cs) FROM ConfirmedStudent cs WHERE YEAR(cs.createdDate) = :currentYear")
    long countByCurrentYear(@Param("currentYear") int currentYear);

}
