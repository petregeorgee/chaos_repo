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

    public File getDecryptedImage(Image build) throws IOException {
        String originalImage = writeImageToDisk(build);
        String encryptedPath= encryptImage(originalImage);
        return new File(encryptedPath);
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

    private String writeImageToDisk(Image build) throws IOException {
        String path = Constants.SAVE_IMAGES_PATH + "/" + build.getName() + ".jpg";
        FileOutputStream fos = new FileOutputStream(path);
        try {
            fos.write(build.getImage());
        }
        finally {
            fos.close();
        }
        return path;
    }


}
