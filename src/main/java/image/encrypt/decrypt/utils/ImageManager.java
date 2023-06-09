package image.encrypt.decrypt.utils;

import image.encrypt.decrypt.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static image.encrypt.decrypt.utils.ImageAction.*;

@Service
public class ImageManager {

    @Autowired
    PythonRunner pythonRunner;
    private final String PYTHON_SCRIPT_PATH = "C:\\Repo\\titu\\lorenz-euler-encrypt-decrypt\\main.py";

    public File getEncryptedImage(Image build) throws IOException {
        String originalImage = writeImageToDisk(build);

        String encryptedPath= encryptImage(originalImage);
        return new File(encryptedPath);
    }

    public File getDecryptedImage(Image image) throws IOException {
        String name = image.getName().split("\\.")[0];
        String originalImage = writeByteToDisk(ImageUtility.decompressImage(image.getImage()), name + "_enc.jpg");

        String decryptedPath= decryptImage(originalImage);
        return new File(decryptedPath);
    }

    public String getHistogram(String image) throws IOException
    {
        return histogram(image);
    }

    private String histogram(String path) throws IOException
    {
        String histogramPath;
        try
        {
            histogramPath = pythonRunner.runPythonScript(PYTHON_SCRIPT_PATH, path, String.valueOf(HISTOGRAM));
        } finally
        {
            Files.delete(Paths.get(path));
        }

        return histogramPath;
    }

    private String encryptImage(String path) throws IOException
    {
        String encryptedPath;
        try
        {
            encryptedPath = pythonRunner.runPythonScript(PYTHON_SCRIPT_PATH, path, String.valueOf(ENCRYPT));
        } finally
        {
            Files.delete(Paths.get(path));
        }

        return encryptedPath;
    }

    private String decryptImage(String path) throws IOException
    {
        String decryptedPath;
        try
        {
            decryptedPath = pythonRunner.runPythonScript(PYTHON_SCRIPT_PATH, path, String.valueOf(DECRYPT));
        } finally
        {
            Files.delete(Paths.get(path));
        }

        return decryptedPath;
    }

    private String writeImageToDisk(Image build) throws IOException {
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
        String path = Constants.SAVE_IMAGES_PATH + "\\" + filename;
        FileOutputStream fos = new FileOutputStream(path);
        try {
            fos.write(imageBytes);
        }
        finally {
            fos.close();
        }
        return path;
    }


    public String getEncryptedImageFromDb(Image image) throws IOException
    {
        image.setImage(ImageUtility.decompressImage(image.getImage()));
        return writeImageToDisk(image);
    }
}
