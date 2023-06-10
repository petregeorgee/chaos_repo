package image.encrypt.decrypt.controller;


import image.encrypt.decrypt.entity.Image;
import image.encrypt.decrypt.entity.User;
import image.encrypt.decrypt.repository.ImageRepository;
import image.encrypt.decrypt.service.UserService;
import image.encrypt.decrypt.utils.ImageManager;
import image.encrypt.decrypt.utils.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        File decryptedFile = new File(path);
        deleteAfterFiveSeconds(decryptedFile);
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(image.getType()))
                .body(Files.readAllBytes(decryptedFile.toPath()));
    }

    @GetMapping("/encrypted/{id}")
    public ResponseEntity<byte[]> getEncryptedImage(@PathVariable("id") String id) throws IOException {
        final Image image = imageRepository.findById(id).get();
        File encryptedFile = imageManager.getEncryptedImageFromDb(image);
        deleteAfterFiveSeconds(encryptedFile);
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(image.getType()))
                .body(Files.readAllBytes(encryptedFile.toPath()));
    }

    @GetMapping("/histogram/{id}")
    public ResponseEntity<byte[]> getHistogram(@PathVariable("id") String id) throws IOException {
        final Image image = imageRepository.findById(id).get();
        File encryptedImage = imageManager.getEncryptedImageFromDb(image);
        File histogramFile = imageManager.getHistogram(String.valueOf(encryptedImage.toPath()));
        deleteAfterFiveSeconds(histogramFile);
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(image.getType()))
                .body(Files.readAllBytes(histogramFile.toPath()));
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
        deleteAfterFiveSeconds(encryptedImage);
        byte[] encryptedImageBytes = ImageUtility.compressImage(Files.readAllBytes(encryptedImage.toPath()));
        image.setImage(encryptedImageBytes);

        imageRepository.save(image);
        return "redirect:list";
    }

    private void deleteAfterFiveSeconds(File encryptedImage)
    {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            try
            {
                Files.deleteIfExists(Path.of(encryptedImage.getPath()));
            } catch (IOException e)
            {

            }
        },5,  TimeUnit.SECONDS);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteImage(@PathVariable("id") String id)
    {

        System.out.println(id);
        Image image = imageRepository.findById(id).get();
        if (image.getId().equals(id))
            imageRepository.deleteById(id);
        else
            System.out.println("Image not found with id " + id);

        return "image";
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
