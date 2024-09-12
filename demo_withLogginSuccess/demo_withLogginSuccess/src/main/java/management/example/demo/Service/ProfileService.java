package management.example.demo.Service;

import management.example.demo.DTO.UserProfileUpdateRequest;
import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Model.Profile;
import management.example.demo.Model.User;
import management.example.demo.Repository.ConfirmedStudentRepository;
import management.example.demo.Repository.ProfileRepository;
import management.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ConfirmedStudentRepository confirmedStudentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Value("${upload.path}")
    private String uploadDir;

    public boolean updateStudentProfile(String username, UserProfileUpdateRequest profileUpdateRequest) {
        Optional<ConfirmedStudent> confirmedStudentOptional = confirmedStudentRepository.findById(username);

        if (confirmedStudentOptional.isPresent()) {
            ConfirmedStudent confirmedStudent = confirmedStudentOptional.get();
            // Only update fields if they are not null or empty in the request
            if (profileUpdateRequest.getNameWithInitials() != null && !profileUpdateRequest.getNameWithInitials().isEmpty()) {
                confirmedStudent.setNameWithInitials(profileUpdateRequest.getNameWithInitials());
            }

            if (profileUpdateRequest.getContactNumber() != null && !profileUpdateRequest.getContactNumber().isEmpty()) {
                confirmedStudent.setContactNumber(profileUpdateRequest.getContactNumber());
            }

            if (profileUpdateRequest.getEmail() != null && !profileUpdateRequest.getEmail().isEmpty()) {
                confirmedStudent.setEmail(profileUpdateRequest.getEmail());
            }

            confirmedStudentRepository.save(confirmedStudent);
            return true;
        } else {
            return false;
        }
    }

    public String updateProfilePicture(MultipartFile file , Long userId) {
        // Attempt to find the profile by its ID
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Profile> profileOpt = profileRepository.findById(userId);

        if (!userOpt.isPresent()) {
            return "User not found"; // Handle the case where the User does not exist
        }

        User user = userOpt.get();
        Profile profile;

        if (profileOpt.isPresent()) {
            // Profile exists, update it
            profile = profileOpt.get();
        } else {
            // Profile does not exist, create a new one and set the user
            profile = new Profile();
            profile.setUser(user); // Ensure the User is set correctly
        }

        // Upload the file and get the unique file name
        // When using the uploadFile method in the fileService, it saves the file data in the fileMetadata,
        // But when deleting the profile picture it is not handle to delete the related data from the entity
        //Consider it.
        List<String> fileData = fileService.uploadFile(file);
        String uniqueFileName = fileData.get(0); // Get the unique file name from the uploaded file data

        // Set the profile picture to the unique file name
        profile.setProfilePicture(uniqueFileName);

        // Save the profile (either updated or newly created) to the repository
        profileRepository.save(profile);

        return "Profile picture updated successfully";
    }


    public Resource getProfilePicture(Long userId) {
        // Fetch the profile picture metadata using the user ID
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile picture not found for user ID " + userId));

        // Check if the profile picture is present
        // If there is no any profile picture it has to return an indicator that there is no profile picture
        //Here having the error below is normal
        if (profile.getProfilePicture() == null) {
            throw new RuntimeException("Profile picture not set for user ID " + userId);
        }

        try {
            Path filePath = Paths.get(uploadDir).resolve(profile.getProfilePicture()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Profile picture not found or is not readable");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String deleteProfilePicture(Long userId) {
        // Fetch the profile picture metadata using the user ID
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile picture not found for user ID " + userId));

        try {
            // Define the path to the profile picture
            Path filePath = Paths.get(uploadDir).resolve(profile.getProfilePicture()).normalize();

            // Delete the file from the file system
            Files.deleteIfExists(filePath);

            // Delete the file metadata from the database
            //profileRepository.delete(profile);
            profile.setProfilePicture(null);
            profileRepository.save(profile);

            return "Profile picture deleted successfully";
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete profile picture", e);
        }
    }


}