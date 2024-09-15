package management.example.demo.Controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import management.example.demo.DTO.UserProfileUpdateRequest;
import management.example.demo.Repository.ProfileRepository;
import management.example.demo.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @Value("${upload.path}")
    private String uploadDir;

    // New method to handle other profile updates
    @PutMapping("/student/update/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable("id") String username, @RequestBody UserProfileUpdateRequest profileUpdateRequest) {
        boolean updateSuccess = profileService.updateStudentProfile(username, profileUpdateRequest);

        if (updateSuccess) {
            return ResponseEntity.ok("Profile updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Profile update failed");
        }
    }

    // New method to handle other profile updates for staff members
    @PutMapping("/user/update/{userId}")
    public ResponseEntity<String> updateProfileStaffMembers(@PathVariable("userId") Long userId, @RequestBody UserProfileUpdateRequest profileUpdateRequest) {
        boolean updateSuccess = profileService.updateStaffMemberProfile(userId, profileUpdateRequest);

        if (updateSuccess) {
            return ResponseEntity.ok("Profile updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Profile update failed");
        }
    }

    @PostMapping("/updatePicture/{id}")
    public ResponseEntity<String> updateProfilePicture(@RequestParam("file") MultipartFile file, @PathVariable("id") Long profileId) {
        String responseMessage = profileService.updateProfilePicture(file, profileId);

        if (responseMessage.equals("Profile picture updated successfully")) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
        }
    }

    @GetMapping("/picture/{userId}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable Long userId) {
        Resource file = profileService.getProfilePicture(userId);

        if (file == null) {
            // Handle the case where the file is not found (return a 404 or default image)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // or a default image resource
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }



    //Handle to return the JSON response from the backend
    @DeleteMapping("/picture/delete/{userId}")
    public ResponseEntity<Map<String, String>> deleteProfilePicture(@PathVariable Long userId) {
        String message = profileService.deleteProfilePicture(userId);
        // Return a JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

}
