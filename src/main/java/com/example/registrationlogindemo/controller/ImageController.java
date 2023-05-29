package com.example.registrationlogindemo.controller;


import com.example.registrationlogindemo.entity.Image;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.ImageRepository;
import com.example.registrationlogindemo.service.UserService;
import com.example.registrationlogindemo.utils.ImageManager;
import com.example.registrationlogindemo.utils.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController
{
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageManager imageManager;
    @Autowired

    private UserService userService;


    @GetMapping("/{username}")
    public List<String> getUserEncryptedImagesIds(@PathVariable("username") String username)
    {
        return Arrays.asList("489735-234-234-asd", "poi3324-234-123-43");
    }

    @PostMapping("/{username}")
    public ResponseEntity<?> uploadImage(@PathVariable("username") String username, @RequestParam("image") MultipartFile file)
            throws IOException
    {
        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(file.getBytes()).build();
        File encryptedImage = imageManager.getEncryptedImage(image);
        image.setImage(ImageUtility.compressImage(Files.readAllBytes(encryptedImage.toPath())));
        imageRepository.save(image);
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
