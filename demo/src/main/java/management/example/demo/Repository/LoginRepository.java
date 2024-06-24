package management.example.demo.Repository;

import management.example.demo.Model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository <Login , String> {
    Login findByRegNumber (String regNumber);
}
