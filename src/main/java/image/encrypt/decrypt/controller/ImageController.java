package image.encrypt.decrypt.controller;


import image.encrypt.decrypt.entity.Image;
import image.encrypt.decrypt.entity.User;
import image.encrypt.decrypt.repository.ImageRepository;
import image.encrypt.decrypt.service.UserService;
import image.encrypt.decrypt.utils.ImageManager;
import image.encrypt.decrypt.utils.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

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
        final Image image = imageRepository.findById(id).get();
        String path = String.valueOf(imageManager.getDecryptedImage(image));
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(image.getType()))
                .body(Files.readAllBytes(new File(path).toPath()));
    }

    @GetMapping("/encrypted/{id}")
    public ResponseEntity<byte[]> getEncryptedImage(@PathVariable("id") String id) throws IOException {
        final Image image = imageRepository.findById(id).get();
        String encryptedImage = imageManager.getEncryptedImageFromDb(image);
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(image.getType()))
                .body(Files.readAllBytes(new File(encryptedImage).toPath()));
    }

    @GetMapping("/histogram/{id}")
    public ResponseEntity<byte[]> getHistogram(@PathVariable("id") String id) throws IOException {
        final Image image = imageRepository.findById(id).get();
        String encryptedImage = imageManager.getEncryptedImageFromDb(image);
        String histogram = imageManager.getHistogram(encryptedImage);
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(image.getType()))
                .body(Files.readAllBytes(new File(histogram).toPath()));
    }

    @GetMapping("/upload")
    public String upload() throws IOException {
        return "image";
    }

    @PostMapping("/new")
    public String uploadImage(@RequestParam("image") MultipartFile file)
            throws IOException
    {
        User authUser = getAuthUser();
        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(file.getBytes())
                .dateUploaded(new Date())
                .username(authUser.getUsername()).build();

        File encryptedImage = imageManager.getEncryptedImage(image);
        byte[] encryptedImageBytes = ImageUtility.compressImage(Files.readAllBytes(encryptedImage.toPath()));
        image.setImage(encryptedImageBytes);

        imageRepository.save(image);
        return "redirect:list";
    }

    @GetMapping(path = {"/list"})
    public String getListOfImagesId(Model model)
    {
        User authUser = getAuthUser();
        final List<Image> imageList = imageRepository.findByUsername(authUser.getUsername());

        model.addAttribute("images", imageList);

        return "list";
    }

    private User getAuthUser()
    {
        String emailAddress = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userService.findByEmail(emailAddress);
    }

}