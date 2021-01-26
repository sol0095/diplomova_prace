package cz.vsb.application.processors;

import cz.vsb.application.files.InputFileReader;
import cz.vsb.application.files.PropertyLoader;

import java.util.HashMap;
import java.util.stream.Stream;

public class PathsMapper extends Thread{

    private HashMap<String, Integer> pathsToId;

    public HashMap<String, Integer> getPathsToId(){
        return pathsToId;
    }

    @Override
    public void run() {

        pathsToId = new HashMap<>();
        Stream<String> lines = InputFileReader.readFile(PropertyLoader.loadProperty("pathToIdFile"));

        long start = System.currentTimeMillis();
        lines.forEach(l->{
            int firstSplit = l.lastIndexOf('_');
            String first = l.substring(0, firstSplit);
            int second = Integer.valueOf(l.substring(firstSplit+1));
            synchronized (pathsToId){
                pathsToId.put(first, second);
            }
        });
        long finish = System.currentTimeMillis();
        System.out.println("Thread time: " + (finish-start));
    }
}
