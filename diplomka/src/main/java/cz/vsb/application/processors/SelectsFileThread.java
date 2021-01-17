package cz.vsb.application.processors;

import cz.vsb.application.files.InputFileReader;
import cz.vsb.application.files.PropertyLoader;
import cz.vsb.application.selects.Select;

import java.util.HashMap;
import java.util.stream.Stream;

public class SelectsFileThread extends Thread{

    private HashMap<Integer, Select> queries;

    public SelectsFileThread(){
        queries = new HashMap<>();
    }

    public HashMap<Integer, Select> getQueries(){
        return queries;
    }

    @Override
    public void run() {
        Stream<String> lines = InputFileReader.readFile(PropertyLoader.loadProperty("selectsFile"));

        lines.forEach(l->{
            int split = l.indexOf('_');
            int id = Integer.valueOf(l.substring(0, split));
            String sub = l.substring(split+1);
            split = sub.indexOf('_');

            Select select = new Select(Integer.valueOf(sub.substring(0, split)), sub.substring(split+1));

            synchronized (queries){
                queries.put(id, select);
            }
        });
    }
}
