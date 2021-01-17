package cz.vsb;

import cz.vsb.application.files.FilesLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"cz.vsb"})
public class Main{

    public static void main(String args[]) {
        SpringApplication application = new SpringApplication(Main.class);
        try {
            loadFiles();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Problem with input files, app will be terminated!");
            return;
        }
        application.run(args);
    }

    private static void loadFiles() throws InterruptedException {
        FilesLoader.loadFiles();
    }
}
