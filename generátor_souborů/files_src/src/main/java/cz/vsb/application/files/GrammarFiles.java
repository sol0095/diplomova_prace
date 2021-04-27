package cz.vsb.application.files;

import cz.vsb.application.processors.PathGenerator;
import cz.vsb.application.processors.PathsMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class GrammarFiles extends Thread{

    private String mappingProp, pathIdProp, selectsProp, pathsSizeProp, inputXMLProp, queryStmt;
    private PathFileWriter pathFileWriter;
    private PathsMap pathsMap;
    private PathGenerator pathGenerator;
    private HashMap<Integer, Integer> pathsSize;

    public GrammarFiles(String mappingProp, String pathIdProp, String selectsProp, String pathsSizeProp, String inputXMLProp, String queryStmt) {
        this.mappingProp = mappingProp;
        this.pathIdProp = pathIdProp;
        this.selectsProp = selectsProp;
        this.pathsSizeProp = pathsSizeProp;
        this.inputXMLProp = inputXMLProp;
        this.queryStmt = queryStmt;
        pathsMap = new PathsMap();
        pathFileWriter = new PathFileWriter();
        pathGenerator = new PathGenerator();
    }


    @Override
    public void run() {
        writeSelectsFile();
        writeMappingFile();
        writePathIdFile();
        writePathsSizeFile();
    }

    private void writeMappingFile(){
        pathFileWriter.startWriting(mappingProp);

        pathsMap.pathsWithNums.forEach((k,v)->{
            pathFileWriter.write(k + "_" + v.pathNum + "\n");
        });
        pathFileWriter.stopWriting();
    }

    private void writeSelectsFile(){
        Stream<String> lines = InputFileReader.readFile(inputXMLProp);

        pathFileWriter.startWriting(selectsProp);
        lines.forEach(e ->{
            pathGenerator.generate(e, pathsMap, queryStmt, pathFileWriter);
        });
        pathFileWriter.stopWriting();

        pathGenerator = null;
    }

    private void writePathIdFile(){
        pathsSize = new HashMap<>();

        pathFileWriter.startWriting(pathIdProp);
        pathsMap.pathsWithNums.forEach((k,v)->{
            List<Integer> list = new ArrayList<Integer>(v.rowIds);
            Collections.sort(list);
            String ids = list.toString();

            for(Integer i : list){
                synchronized (pathsSize){
                    if(pathsSize.containsKey(i)){
                        int count = pathsSize.get(i);
                        count++;
                        pathsSize.replace(i, count);
                    }
                    else{
                        pathsSize.put(i, 1);
                    }
                }
            }
            pathFileWriter.write(v.pathNum + "_" + ids.substring(1, ids.length()-1).replaceAll("\\s","") + "\n");
        });
        pathFileWriter.stopWriting();
        pathsMap = null;
    }

    private void writePathsSizeFile(){
        pathFileWriter.startWriting(pathsSizeProp);
        pathsSize.forEach((k, v)->{
            pathFileWriter.write(k + "=" + v + "\n");
        });
        pathFileWriter.stopWriting();
    }
}
