package cz.vsb.application;

import cz.vsb.application.files.InputFileReader;
import cz.vsb.application.files.PathFileWriter;
import cz.vsb.application.files.PropertyLoader;
import cz.vsb.application.processors.*;
import java.util.*;
import java.util.stream.Stream;

public class Application{

    public static void runApplication(){

        char grammar = PropertyLoader.loadProperty("grammar").charAt(0);
        String queryStmt = getStmtName(grammar);

        writePathsTofile(queryStmt);
    }

    private static void writePathsTofile(String queryStmt){
        long start = System.currentTimeMillis();

        PathsMap pathsMap = new PathsMap();
        Stream<String> lines = InputFileReader.readFile(PropertyLoader.loadProperty("inputXmlFile"));

        PathFileWriter.startWriting(PropertyLoader.loadProperty("selectsFile"));
        lines.forEach(e ->{
            PathGenerator.generate(e, pathsMap, queryStmt);
        });
        PathFileWriter.stopWriting();

        PathFileWriter.startWriting(PropertyLoader.loadProperty("pathToIdFile"));
        pathsMap.pathsWithNums.forEach((k,v)->{
            PathFileWriter.write(k + "_" + v.pathNum + "\n");
        });
        PathFileWriter.stopWriting();

        HashMap<Integer, Integer> pathsSize = new HashMap<>();

        PathFileWriter.startWriting(PropertyLoader.loadProperty("pathIdWithRowIds"));
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
            PathFileWriter.write(v.pathNum + "_" + ids.substring(1, ids.length()-1).replaceAll("\\s","") + "\n");
        });
        PathFileWriter.stopWriting();

        PathFileWriter.startWriting(PropertyLoader.loadProperty("pathsSize"));
        String str = pathsSize.toString();
        PathFileWriter.write(str.substring(1, str.length()-1).replaceAll("\\s",""));
        PathFileWriter.stopWriting();

        long finish = System.currentTimeMillis();
        System.out.println("Writing time: " + (finish-start) + "ms");
    }

    private static String getStmtName(char grammar){
        String queryStmt = null;
        switch(grammar) {
            case '0':
                queryStmt = "root";
                break;
            case '1':
                queryStmt = "parse";
                break;
            case '2':
                queryStmt = "tsql_file";
                break;
            case '3':
                queryStmt = "sql_script";
                break;
        }
        return queryStmt;
    }
}
