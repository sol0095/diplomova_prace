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
       // test();


    }

    private static void test(){
        Stream<String> lines = InputFileReader.readFile(PropertyLoader.loadProperty("mysqlSelectsFile")).limit(10000);

        System.out.println("testing");
        lines.forEach(l->{
            int split = l.indexOf('_');
            int id = Integer.valueOf(l.substring(0, split));
            String sub = l.substring(split+1);
            split = sub.indexOf('_');

            Select select = new Select(Integer.valueOf(sub.substring(0, split)), sub.substring(split+1));

            Application.calculateSimilarity('0', "root", select.getQuery());


        });

        System.out.println("test ends");

        lines = InputFileReader.readFile(PropertyLoader.loadProperty("tsqlSelectsFile")).limit(10000);

        System.out.println("testing");
        lines.forEach(l->{
            int split = l.indexOf('_');
            int id = Integer.valueOf(l.substring(0, split));
            String sub = l.substring(split+1);
            split = sub.indexOf('_');

            Select select = new Select(Integer.valueOf(sub.substring(0, split)), sub.substring(split+1));

            Application.calculateSimilarity('2', "tsql_file", select.getQuery());


        });

        System.out.println("test ends");

        lines = InputFileReader.readFile(PropertyLoader.loadProperty("plsqlSelectsFile")).limit(10000);

        System.out.println("testing");
        lines.forEach(l->{
            int split = l.indexOf('_');
            int id = Integer.valueOf(l.substring(0, split));
            String sub = l.substring(split+1);
            split = sub.indexOf('_');

            Select select = new Select(Integer.valueOf(sub.substring(0, split)), sub.substring(split+1));

            Application.calculateSimilarity('3', "sql_script", select.getQuery());


        });

        System.out.println("test ends");

        lines = InputFileReader.readFile(PropertyLoader.loadProperty("sqliteSelectsFile")).limit(10000);

        System.out.println("testing");
        lines.forEach(l->{
            int split = l.indexOf('_');
            int id = Integer.valueOf(l.substring(0, split));
            String sub = l.substring(split+1);
            split = sub.indexOf('_');

            Select select = new Select(Integer.valueOf(sub.substring(0, split)), sub.substring(split+1));

            Application.calculateSimilarity('1', "parse", select.getQuery());


        });

        System.out.println("test ends");
    }
}
