package cz.vsb.application.processors;

import cz.vsb.application.files.InputFileReader;
import cz.vsb.application.files.PropertyLoader;

import java.util.HashMap;
import java.util.stream.Stream;

public class PathsSizeThread extends Thread{

    private HashMap<Integer, Integer> pathsSize;

    public PathsSizeThread(){
        pathsSize= new HashMap<>();
    }

    public HashMap<Integer, Integer> getPathsSize(){
        return pathsSize;
    }

    @Override
    public void run() {
        Stream<String> pathsSizeLines = InputFileReader.readFile(PropertyLoader.loadProperty("pathsSize"));
        pathsSizeLines.forEach(s -> {
            String[] pairs = s.split(",");
            for (int i=0;i<pairs.length;i++) {
                String pair = pairs[i];
                String[] keyValue = pair.split("=");

                synchronized (pathsSize){
                    pathsSize.put(Integer.valueOf(keyValue[0]), Integer.valueOf(keyValue[1]));
                }
            }
        });
    }
}
