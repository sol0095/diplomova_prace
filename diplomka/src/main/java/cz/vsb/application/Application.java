package cz.vsb.application;

import cz.vsb.application.files.FileToHashMapLoader;
import cz.vsb.application.files.InputFileReader;
import cz.vsb.application.files.PathFileWriter;
import cz.vsb.application.files.PropertyLoader;
import cz.vsb.application.processors.*;
import cz.vsb.application.selects.SelectComparator;
import cz.vsb.application.selects.SelectWithPaths;
import cz.vsb.application.selects.SelectWithSimilarity;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class Application{

    public static void runApplication(){

        char grammar = PropertyLoader.loadProperty("grammar").charAt(0);
        String queryStmt = getStmtName(grammar);

       /*if(Boolean.parseBoolean(PropertyLoader.loadProperty("generatePathsFile")))
            writePathsTofile(queryStmt);*/
        try {
            calculateSimilarity(grammar, queryStmt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void writePathsTofile(String queryStmt){
        long start = System.currentTimeMillis();

        PathsMap pathsMap = new PathsMap();
        Stream<String> lines = InputFileReader.readFile(PropertyLoader.loadProperty("inputXmlFile"));
        List<SelectWithPaths> resultList = Collections.synchronizedList(new ArrayList<>());

        PathFileWriter.startWriting(PropertyLoader.loadProperty("selectsFile"));
        lines.forEach(e ->{
            ArrayList<SelectWithPaths> threadResult = PathGenerator.generate(e, pathsMap, queryStmt);
            for(SelectWithPaths select : threadResult)
                resultList.add(select);
        });
        PathFileWriter.stopWriting();

        PathFileWriter.startWriting(PropertyLoader.loadProperty("pathToIdFile"));
        pathsMap.pathsWithNums.forEach((k,v)->{
            PathFileWriter.write(k + "_" + v.pathNum + "\n");
        });
        PathFileWriter.stopWriting();

        PathFileWriter.startWriting(PropertyLoader.loadProperty("pathIdWithRowIds"));
        pathsMap.pathsWithNums.forEach((k,v)->{
            String ids = v.rowIds.toString();
            PathFileWriter.write(v.pathNum + "_" + ids.substring(1, ids.length()-1).replaceAll("\\s","") + "\n");
        });
        PathFileWriter.stopWriting();

        long finish = System.currentTimeMillis();
        System.out.println("Writing time: " + (finish-start) + "ms");
    }

    private static void calculateSimilarity(char grammar, String quieryStmt) throws InterruptedException {

        long start = System.currentTimeMillis();
        InputPreparator.prepareInput(PropertyLoader.loadProperty("inputQuery"), grammar, quieryStmt);
        long finish = System.currentTimeMillis();
        System.out.println("Prepare time: " + (finish-start) + "ms");

        start = System.currentTimeMillis();
        FileToHashMapLoader pathsLoader = new FileToHashMapLoader(InputFileReader.readFile(PropertyLoader.loadProperty("pathIdWithRowIds")));
        FileToHashMapLoader selectsLoader = new FileToHashMapLoader(InputFileReader.readFile(PropertyLoader.loadProperty("selectsFile")));
        pathsLoader.start();
        selectsLoader.start();
        pathsLoader.join();
        selectsLoader.join();
        Map<Integer,String> paths = pathsLoader.getMap();
        Map<Integer,String> selects = selectsLoader.getMap();
        finish = System.currentTimeMillis();
        System.out.println("Files loading time: " + (finish-start) + "ms");


        start = System.currentTimeMillis();
        HashSet<Integer> selectIds = new HashSet<>();
        HashSet<Integer> pathIds = InputPreparator.getInputPaths();
        for(Integer i : pathIds){
            if(paths.containsKey(i)){
                String[] idsStr = paths.get(i).split(",");
                for(String s : idsStr)
                    selectIds.add(Integer.parseInt(s));
            }
        }
        finish = System.currentTimeMillis();
        System.out.println("Getting selects time: " + (finish-start) + "ms");

        start = System.currentTimeMillis();
        ArrayList<SelectWithSimilarity> results = new ArrayList<>();
        for(Integer i : selectIds){
            HashSet<Integer> fileHash = new HashSet<>();
            String select = selects.get(i);
            int split = select.indexOf('_');
            int rowId = Integer.parseInt(select.substring(0, split));
            select = select.substring(split+1);
            split = select.indexOf('_');
            String[] splittedPahts = select.substring(0, split).split(",");

            for(int j = 0; j < splittedPahts.length; j++)
                fileHash.add(Integer.parseInt(splittedPahts[j]));

            String query = select.substring(split+1);

            int intersection = 0;

            for(Integer s : pathIds){
                if(fileHash.contains(s))
                    intersection++;
            }
            double totalSize = pathIds.size() + fileHash.size() - intersection;
            results.add(new SelectWithSimilarity(query, (double)intersection / totalSize));
        }
        finish = System.currentTimeMillis();
        System.out.println("Computing time: " + (finish-start) + "ms");

        start = System.currentTimeMillis();
        Collections.sort(results, new SelectComparator());
        finish = System.currentTimeMillis();
        System.out.println("Sorting time: " + (finish-start) + "ms");

        for(int i = 0; i < 20; i++){
            System.out.println(results.get(i).getQuery());
            System.out.println(results.get(i).getSimilarity());
        }

    }

    private static String getStmtName(char grammar){
        String queryStmt = null;
        switch(grammar) {
            case '0':
                queryStmt = "sqlStatement";
                break;
            case '1':
                queryStmt = "sql_stmt";
                break;
            case '2':
                queryStmt = "sql_stmt";
                break;
            case '3':
                queryStmt = "sql_stmt";
                break;
        }
        return queryStmt;
    }
}
