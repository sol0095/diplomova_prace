package cz.vsb.application.files;

import cz.vsb.application.selects.Select;

import java.util.HashMap;
import java.util.stream.Stream;

public class GrammarFiles extends Thread{

    private HashMap<String, Integer> mappingHash;
    private HashMap<Integer, String> paths;
    private HashMap<Integer, Select> queries;
    private HashMap<Integer, Integer> pathsSize;
    private String mappingProp, pathIdProp, selectsProp, pathsSizeProp;

    public GrammarFiles(String mappingProp, String pathIdProp, String selectsProp, String pathsSizeProp){
        mappingHash = new HashMap<>();
        paths = new HashMap<>();
        queries = new HashMap<>();
        pathsSize = new HashMap<>();
        this.mappingProp = mappingProp;
        this.pathIdProp = pathIdProp;
        this.selectsProp = selectsProp;
        this.pathsSizeProp = pathsSizeProp;
    }

    @Override
    public void run() {
        loadMappingFile();
        loadPathIdFile();
        loadSelectsFile();
        loadPathsSizeFile();
    }

    public HashMap<String, Integer> getMappingHash() {
        return mappingHash;
    }

    public HashMap<Integer, String> getPaths() {
        return paths;
    }

    public HashMap<Integer, Select> getQueries() {
        return queries;
    }

    public HashMap<Integer, Integer> getPathsSize() {
        return pathsSize;
    }

    private void loadMappingFile(){
        Stream<String> lines = InputFileReader.readFile(mappingProp);

        lines.forEach(l->{
            int firstSplit = l.lastIndexOf('_');
            synchronized (mappingHash){
                mappingHash.put(l.substring(0, firstSplit), Integer.parseInt(l.substring(firstSplit+1)));
            }
        });
    }

    private void loadPathIdFile(){
        Stream<String> lines = InputFileReader.readFile(pathIdProp);

        lines.forEach(l->{
            int firstSplit = l.indexOf('_');
            synchronized (paths){
                paths.put(Integer.parseInt(l.substring(0, firstSplit)), l.substring(firstSplit+1));
            }
        });
    }

    private void loadSelectsFile() {
        Stream<String> lines = InputFileReader.readFile(selectsProp);

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

    private void loadPathsSizeFile(){
        Stream<String> pathsSizeLines = InputFileReader.readFile(pathsSizeProp);
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
