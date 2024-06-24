package management.example.demo.Repository;

import management.example.demo.Model.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpRepository extends JpaRepository<SignUp, Long> {
    SignUp findByRegNumber (String regNumber);
}