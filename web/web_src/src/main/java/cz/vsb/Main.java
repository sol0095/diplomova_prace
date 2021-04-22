package cz.vsb;

import cz.vsb.application.Application;
import cz.vsb.application.files.FilesLoader;
import cz.vsb.application.files.InputFileReader;
import cz.vsb.application.files.PropertyLoader;
import cz.vsb.application.selects.Select;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.stream.Stream;

@SpringBootApplication
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"cz.vsb"})
public class Main{

    public static void main(String args[]) {
        SpringApplication application = new SpringApplication(Main.class);

        try {
            FilesLoader.loadFiles();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Problem with input files, app will be terminated!");
            return;
        }

        application.run(args);
    }
}
