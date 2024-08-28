package management.example.demo.Service;


import management.example.demo.Model.FileMetadata;
import management.example.demo.Repository.FileMetadataRepository;
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
import java.time.LocalDateTime;

@Service
public class FileService {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;
    @Value("${upload.path}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }

        try {
            //reads the contents of the uploaded file into a byte array
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir + file.getOriginalFilename());
            //writes the byte array to the specified path on the server
            Files.write(path, bytes);
            String fileName = file.getOriginalFilename();
            String filePath = path.toString();

            //To save the file data into the database
            //////////////
            FileMetadata fileMetadata= new FileMetadata();
            fileMetadata.setFileName(fileName);
            fileMetadata.setFileType(file.getContentType());
            fileMetadata.setFileSize(file.getSize());
            fileMetadata.setUploadDate(LocalDateTime.now().toString());
            fileMetadataRepository.save(fileMetadata);
            ///////////////

            return fileName; // Return the file path
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed";
        }
    }

    // Method to download the file
    public Resource downloadFile(Long fileId) {
        // Fetch file metadata from the database
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + fileId));

        try {
            // Load file as a resource
            Path filePath = Paths.get(uploadDir).resolve(fileMetadata.getFileName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or is not readable");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
