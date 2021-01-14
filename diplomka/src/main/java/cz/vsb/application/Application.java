package cz.vsb.application;

import cz.vsb.application.files.FileToHashMapLoader;
import cz.vsb.application.files.InputFileReader;
import cz.vsb.application.files.PathFileWriter;
import cz.vsb.application.files.PropertyLoader;
import cz.vsb.application.processors.*;
import cz.vsb.application.selects.SelectComparator;
import cz.vsb.application.selects.SelectWithSimilarity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Application{

    public static void runApplication(){

        char grammar = PropertyLoader.loadProperty("grammar").charAt(0);
        String queryStmt = getStmtName(grammar);

        if(Boolean.parseBoolean(PropertyLoader.loadProperty("generatePathsFile")))
            writePathsTofile(queryStmt);
        try {
            calculateSimilarity(grammar, queryStmt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static void calculateSimilarity(char grammar, String queryStmt) throws InterruptedException, IOException {

        long start = System.currentTimeMillis();

        InputThread inputThread = new InputThread(grammar, queryStmt);
        inputThread.start();

        SelectsFileThread selectsFileThread = new SelectsFileThread();
        selectsFileThread.start();

        FileToHashMapLoader pathsLoader = new FileToHashMapLoader(InputFileReader.readFile(PropertyLoader.loadProperty("pathIdWithRowIds")));
        pathsLoader.start();

        PathsSizeThread pathsSizeThread = new PathsSizeThread();
        pathsSizeThread.start();

        inputThread.join();
        pathsLoader.join();
        Map<Integer,String> paths = pathsLoader.getMap();

        long finish = System.currentTimeMillis();
        System.out.println("Loading files and total prepare time: " + (finish-start) + "ms");

        start = System.currentTimeMillis();
        ArrayList<Cursor> cursors = new ArrayList<>();

        HashSet<Integer> pathIds = InputPreparator.getInputPaths();
        int size = pathIds.size();
        for(Integer i : pathIds){
            if(paths.containsKey(i)){
                String[] idsStr = paths.get(i).split(",");
                cursors.add(new Cursor(Arrays.stream(idsStr).mapToInt(Integer::parseInt).toArray(), i));
            }
        }
        int cursorsCount = cursors.size();

        finish = System.currentTimeMillis();
        System.out.println("Get cursors time: " + (finish-start) + "ms");

        start = System.currentTimeMillis();

        HashMap<Integer, Integer> intersection = new HashMap<>();
        HashMap<Integer, Integer> nonIntersection = new HashMap<>();
        while(cursors.size() > 0){
            int min = cursors.get(0).getCurrent();
            int count = 0;

            for(Cursor c : cursors){
                if(c.getCurrent() < min)
                    min = c.getCurrent();
            }

            for(int i = 0; i < cursors.size(); i++){
                if(cursors.get(i).getCurrent() == min){
                    cursors.get(i).moveNext();
                    count++;
                }

                if(!cursors.get(i).isInRange()){
                    cursors.remove(i);
                    i--;
                }
            }

            intersection.put(min, count);
            nonIntersection.put(min, cursorsCount-count);
        }

        finish = System.currentTimeMillis();
        System.out.println("Computing time: " + (finish-start) + "ms");

        ArrayList<SelectWithSimilarity> resultList = new ArrayList<>();
        selectsFileThread.join();
        pathsSizeThread.join();

        intersection.forEach((k,v)->{
            double similarity = (double)v/(double)(pathsSizeThread.getPathsSize().get(k)+nonIntersection.get(k));
            resultList.add(new SelectWithSimilarity(selectsFileThread.getQueries().get(k), similarity));
        });

        start = System.currentTimeMillis();
        Collections.sort(resultList, new SelectComparator());
        finish = System.currentTimeMillis();
        System.out.println("Sorting time: " + (finish-start) + "ms");

        System.out.println("Total queries compared: " + resultList.size());

        for(int i = 0; i < 20; i++){
            System.out.println(resultList.get(i).getQuery());
            System.out.println(resultList.get(i).getSimilarity());
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
