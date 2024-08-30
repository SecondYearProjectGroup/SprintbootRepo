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
import java.util.*;

@Service
public class FileService {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;
    @Value("${upload.path}")
    private String uploadDir;

    public List<String> uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return Collections.singletonList("Please select a file to upload");
        }

        try {
            // Generate a unique identifier for the file
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // reads the contents of the uploaded file into a byte array
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir + uniqueFileName);
            // writes the byte array to the specified path on the server
            Files.write(path, bytes);
            String filePath = path.toString();

            // To save the file data into the database
            FileMetadata fileMetadata = new FileMetadata();
            fileMetadata.setFileName(uniqueFileName); // Save the unique file name
            fileMetadata.setOriginalFileName(file.getOriginalFilename()); // Save the original file name if needed
            fileMetadata.setFileType(file.getContentType());
            fileMetadata.setFileSize(file.getSize());
            fileMetadata.setUploadDate(LocalDateTime.now().toString());
            fileMetadataRepository.save(fileMetadata);

            List<String> outputs = new ArrayList<>();
            outputs.add(uniqueFileName);
            outputs.add(fileMetadata.getOriginalFileName());

            return outputs     ; // Return the unique file name
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.singletonList("File upload failed");
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

    public Optional<FileMetadata> getFileMetadata(Long id){
        return fileMetadataRepository.findById(id);
    }



    public Resource downloadFileByName(String fileName) {
        // Fetch file metadata by file name
        FileMetadata fileMetadata = fileMetadataRepository.findByFileName(fileName)
                .orElseThrow(() -> new RuntimeException("File not found with name " + fileName));

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
