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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/images")
public class ImageController
{
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageManager imageManager;
    @Autowired

    private UserService userService;


    @GetMapping("/decrypt/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) throws IOException {
        final Optional<Image> image = imageRepository.findById(id);
        //TODO: get decrypted image from database;
//        imageManager.getDecryptedImage(ImageUtility.decompressImage(image.get().getImage());
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(image.get().getType()))
                .body(ImageUtility.decompressImage(image.get().getImage()));
    }

    @GetMapping("/upload")
    public String upload() throws IOException {
        return "image";
    }

    @PostMapping("/{username}")
    public ResponseEntity<?> uploadImage(@PathVariable("username") String username, @RequestParam("image") MultipartFile file)
            throws IOException
    {
        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(file.getBytes())
                .username(username).build();

        File encryptedImage = imageManager.getEncryptedImage(image);
        byte[] encryptedImageBytes = ImageUtility.compressImage(Files.readAllBytes(encryptedImage.toPath()));
        image.setImage(encryptedImageBytes);

        imageRepository.save(image);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Image uploaded successfully: " +
                        file.getOriginalFilename());
    }

    @GetMapping(path = {"/list/{username}"})
    public ResponseEntity<List> getListOfImagesId(@PathVariable("username") String username) throws IOException {
        //TODO: get image by id and filter it by username.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String name = authentication.getName();
        final List<Image> imageList = imageRepository.findByUsername(username);

        return ResponseEntity
                .ok()
                .body(imageList.stream().map(Image::getId).collect(Collectors.toList()));
    }

}
