package management.example.demo.Service;

import management.example.demo.Model.SignUp;
import management.example.demo.Repository.SignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private SignUpRepository signUpRepository;

    public SignUp saveSignUp(SignUp signUp) {
        return signUpRepository.save(signUp);
    }
}
