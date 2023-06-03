package com.example.registrationlogindemo.utils;

import com.example.registrationlogindemo.Constants;
import com.example.registrationlogindemo.PythonRunner;
import com.example.registrationlogindemo.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageManager {

    @Autowired
    PythonRunner pythonRunner;
    public File getEncryptedImage(Image build) throws IOException {
        String originalImage = writeImageToDisk(build);

        String encryptedPath= encryptImage(originalImage);
        return new File(encryptedPath);
    }

    public File getDecryptedImage(Image image) throws IOException {
        String originalImage = writeByteToDisk(ImageUtility.decompressImage(image.getImage()), image.getName() + "_enc");

        String decryptedPath= decryptImage(originalImage);
        return new File(decryptedPath);
    }

    private String encryptImage(String path) throws IOException
    {
        String enc_path;
        try
        {
            enc_path = pythonRunner.runPythonScript("C:\\Repo\\titu\\lorenz-euler-encrypt-decrypt\\main.py", path, "ENCRYPT");
        } finally
        {
            Files.delete(Paths.get(path));
        }

        return enc_path;
    }

    private String decryptImage(String path) throws IOException
    {
        String dec_path;
        try
        {
            dec_path = pythonRunner.runPythonScript("C:\\Repo\\titu\\lorenz-euler-encrypt-decrypt\\main.py", path, "DECRYPT");
        } finally
        {
            Files.delete(Paths.get(path));
        }

        return dec_path;
    }

    public String writeImageToDisk(Image build) throws IOException {
        String path = Constants.SAVE_IMAGES_PATH + "/" + build.getName();
        FileOutputStream fos = new FileOutputStream(path);
        try {
            fos.write(build.getImage());
        }
        finally {
            fos.close();
        }
        return path;
    }


    private String writeByteToDisk(byte[] imageBytes, String filename) throws IOException {
        String path = Constants.SAVE_IMAGES_PATH + "/" + filename + ".jpg";
        FileOutputStream fos = new FileOutputStream(path);
        try {
            fos.write(imageBytes);
        }
        finally {
            fos.close();
        }
        return path;
    }


}
