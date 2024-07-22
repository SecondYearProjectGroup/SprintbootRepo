package management.example.demo.Service;



import management.example.demo.Model.User;
import management.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class LoginService implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    //Find the user by the id
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //Save users, overriding the save method in the user service
    @Override
    public User save(User user_) {
        //Set username, firstname, lastname, email and the  encoded password
        User user = new User(   user_.getUsername(),
                                user_.getFirstName(),
                                user_.getLastName(),
                                user_.getEmail(),
                                passwordEncoder.encode(user_.getPassword()));
        //Save user in the database through the repository layer
        return userRepository.save(user);
    }

}





































 /*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found.");
        }
        org.springframework.security.core.userdetails.User.UserBuilder builder =
                org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
        builder.password(user.getPwd());
        builder.roles("STUDENT");

        UserDetails userDetails = builder.build();
        System.out.println("UserDetails: " + userDetails);
        return userDetails;
    }*/