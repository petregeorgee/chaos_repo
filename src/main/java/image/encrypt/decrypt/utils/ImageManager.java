package image.encrypt.decrypt.utils;

import image.encrypt.decrypt.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static image.encrypt.decrypt.utils.ImageAction.*;

@Service
public class ImageManager {

    @Autowired
    PythonRunner pythonRunner;

    public String getPythonPath()
    {
        return Objects.requireNonNull(findFolder("lorenz-euler-encrypt-decrypt")) + "\\main.py";
    }

    public File getEncryptedImage(Image image) throws IOException {
        image.setName(image.getName().replace(" ", "_"));
        String originalImage = writeImageToDisk(image);

        String encryptedPath= encryptImage(originalImage);
        return new File(encryptedPath);
    }

    public File getDecryptedImage(Image image) throws IOException {
        String name = image.getName().split("\\.")[0];
        String originalImage = writeByteToDisk(ImageUtility.decompressImage(image.getImage()), name + "_enc.jpg");

        String decryptedPath= decryptImage(originalImage);
        return new File(decryptedPath);
    }

    public File getHistogram(String image) throws IOException
    {
        String histogramPath = histogram(image);
        return new File(histogramPath);
    }

    private String histogram(String path) throws IOException
    {
        String histogramPath;
        try
        {
            histogramPath = pythonRunner.runPythonScript(getPythonPath(), path, String.valueOf(HISTOGRAM));
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
            encryptedPath = pythonRunner.runPythonScript(getPythonPath(), path, String.valueOf(ENCRYPT));
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
            decryptedPath = pythonRunner.runPythonScript(getPythonPath(), path, String.valueOf(DECRYPT));
        } finally
        {
            Files.delete(Paths.get(path));
        }

        return decryptedPath;
    }

    private String writeImageToDisk(Image build) throws IOException {
        String path = Constants.SAVE_IMAGES_PATH + "\\" + build.getName();
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


    public File getEncryptedImageFromDb(Image image) throws IOException
    {
        image.setImage(ImageUtility.decompressImage(image.getImage()));
        String encryptedPath = writeImageToDisk(image);
        return new File(encryptedPath);
    }

    public static File findFolder(String folderName) {
        // Get the current working directory
        String currentDirectory = System.getProperty("user.dir");

        // Create a File object representing the current directory
        File directory =new File(new File(currentDirectory).getParent());

        // Get a list of all files and folders in the current directory
        File[] files = directory.listFiles();

        if (files != null) {
            // Iterate over the files and folders
            for (File file : files) {
                // Check if the current item is a folder and has the desired name
                if (file.isDirectory() && file.getName().equals(folderName)) {
                    return file; // Found the folder
                }
            }
        }

        return null; // Folder not found
    }
}
