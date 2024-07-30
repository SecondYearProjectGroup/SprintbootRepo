package management.example.demo.Service;


import management.example.demo.Model.User;
import management.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import management.example.demo.enums.Role;

import java.util.HashSet;
import java.util.Set;


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
        // Get the roles from the user_ object if any
        Set<Role> roles = user_.getRoles();

        // If no roles are set, you can initialize with a default role, e.g., STUDENT
        if (roles == null || roles.isEmpty()) {
            roles = new HashSet<>();
            roles.add(Role.USER); // Or set to a default role as per your application's logic
        }

        // Create a new User object with the provided details and roles
        User user = new User(
                user_.getUsername(),
                user_.getFirstName(),
                user_.getLastName(),
                user_.getEmail(),
                passwordEncoder.encode(user_.getPassword()),
                roles
        );
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