package image.encrypt.decrypt.images.controller;

import image.encrypt.decrypt.auth.model.User;
import image.encrypt.decrypt.images.ImageRepository;
import image.encrypt.decrypt.images.ImageUtility;
import image.encrypt.decrypt.images.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

//        imageRepository.save(Image.builder()
//                .name(file.getOriginalFilename())
//                .type(file.getContentType())
//                .image(ImageUtility.compressImage(file.getBytes())).build());
        return ResponseEntity.status(HttpStatus.OK)
                .body("Image uploaded successfully: " +
                        file.getOriginalFilename());
    }

}
