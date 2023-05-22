package image.encrypt.decrypt.images.repository;

import java.util.Optional;

import image.encrypt.decrypt.images.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
}
