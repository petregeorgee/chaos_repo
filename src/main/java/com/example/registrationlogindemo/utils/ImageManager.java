package com.example.registrationlogindemo.utils;

import com.example.registrationlogindemo.Constants;
import com.example.registrationlogindemo.PythonRunner;
import com.example.registrationlogindemo.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ImageManager {

    @Autowired
    PythonRunner pythonRunner;
    public void save(Image build) throws IOException {
        String path = writeImageToDisk(build);
        encryptImage(path);
    }

    private void encryptImage(String path) {
//        pythonRunner.runPythonScript();
    }

    private String writeImageToDisk(Image build) throws IOException {
        String path = Constants.SAVE_IMAGES_PATH.toString() + "/" + build.getId() + ".jpg";
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
