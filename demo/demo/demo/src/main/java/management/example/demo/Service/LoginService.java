package management.example.demo.Service;

import management.example.demo.Model.SignUp;
import management.example.demo.Repository.SignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginDetailService {

    @Autowired
    private SignUpRepository signUpRepository;


    @Override
    public UserDetails loadLoginByRegNumber(String regNumber) throws UsernameNotFoundException {
        SignUp signUp = signUpRepository.findByRegNumber(regNumber);
        if (signUp == null) {
            throw new UsernameNotFoundException("User Not Found.");
        }
        //"UserBuilder" Static nested class inside the "org.springframework.security.core.userdetails.User" class
        //"User.withUsername" is a method in that class and it initializes the builder with username(regNumber)
        User.UserBuilder builder = User.withUsername(signUp.getRegNumber());
        //Set the password for the user
        builder.password(signUp.getPwd());
        //Set the role for the user
        builder.roles("STUDENT");
        //Construct and return the created "USER - STUDENT" object
        return builder.build();
    }
}
