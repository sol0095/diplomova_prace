package cz.vsb.application.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class InputFileReader {

    public static Stream<String> readFile(String path){
        try {
            return Files.lines(Paths.get(path)).parallel();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Missing file or incorrect file name, application will be terminated!");
            System.exit(-1);
        }

        return  null;
    }
}
