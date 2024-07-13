package management.example.demo.Service;

import management.example.demo.Model.User;

public interface UserService {
    User findByUsername(String username);

    User save(User user);

}