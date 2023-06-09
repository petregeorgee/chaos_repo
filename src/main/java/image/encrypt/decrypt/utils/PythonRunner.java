package image.encrypt.decrypt.utils;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class PythonRunner {
    public String runPythonScript(String scriptPath, String... arguments) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath);
            processBuilder.command().addAll(List.of(arguments));
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
