package management.example.demo.Repository;

import management.example.demo.Model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    Optional<FileMetadata> findByFileName(String fileName);
}
