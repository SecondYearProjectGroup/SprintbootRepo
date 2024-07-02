package management.example.demo.Service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface LoginDetailService {
    //'UserDetails' is an interface provided by the Spring Security
    //Use for authentication and authorization
    UserDetails loadLoginByRegNumber(String regNumber) throws UsernameNotFoundException;

    //To setup the Security Configuration Autowired with this interface
}
