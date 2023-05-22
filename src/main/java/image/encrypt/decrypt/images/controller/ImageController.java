package image.encrypt.decrypt.images.controller;

import image.encrypt.decrypt.images.repository.ImageRepository;
import image.encrypt.decrypt.images.utils.ImageUtility;
import image.encrypt.decrypt.images.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController
{
    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/{username}")
    public List<String> getUserEncryptedImagesIds(@PathVariable("username") String username)
    {
        return Arrays.asList("489735-234-234-asd", "poi3324-234-123-43");
    }

    @PostMapping("/{username}")
    public ResponseEntity<?> uploaddImage(@PathVariable("username") String username, @RequestParam("image") MultipartFile file)
            throws IOException
    {
        Image build = Image.builder()
                .id(32423L)
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(ImageUtility.compressImage(file.getBytes())).build();
        imageRepository.save(build);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Image uploaded successfully: " +
                        file.getOriginalFilename());
    }

    @GetMapping(path = {"{username}/{imageName}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("username") String username, @PathVariable("imageName") String imageName) throws IOException {
        //TODO: get image by id and filter it by username.
        final Optional<Image> dbImage = imageRepository.findByName(imageName);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(ImageUtility.decompressImage(dbImage.get().getImage()));
    }

}
