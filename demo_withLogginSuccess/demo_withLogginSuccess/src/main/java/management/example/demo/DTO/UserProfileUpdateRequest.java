package management.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateRequest {

    private String nameWithInitials;
    private String contactNumber;
    private String email;
}
