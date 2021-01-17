package cz.vsb.application;

import cz.vsb.application.files.*;
import cz.vsb.application.processors.*;
import cz.vsb.application.selects.SelectComparator;
import cz.vsb.application.selects.SelectWithSimilarity;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Application{

    public static void runApplication(){

     /*   char grammar = PropertyLoader.loadProperty("grammar").charAt(0);
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
        }*/
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

    public static SelectWithSimilarity[] calculateSimilarity(char grammar, String queryStmt, String query) throws InterruptedException, IOException {

        InputPreparator inputPreparator = new InputPreparator();

        GrammarState grammarState = inputPreparator.prepareInput(query, grammar, queryStmt);

        if(grammarState != GrammarState.CORRECT){
            SelectWithSimilarity[] empty = new SelectWithSimilarity[1];
            empty[0] = grammarState == GrammarState.EMPTY ?
                    new SelectWithSimilarity("No similar query found.", 0.0, -1) :
                    new SelectWithSimilarity("Wrong query syntax.", 0.0, -1);
            return empty;
        }

        GrammarFiles grammarFiles = FilesLoader.getGrammar(grammar);

        Map<Integer,String> paths = grammarFiles.getPaths();

        long start = System.currentTimeMillis();
        ArrayList<Cursor> cursors = new ArrayList<>();

        HashSet<Integer> pathIds = inputPreparator.getInputPaths();
        for(Integer i : pathIds){
            if(paths.containsKey(i)){
                String[] idsStr = paths.get(i).split(",");
                cursors.add(new Cursor(Arrays.stream(idsStr).mapToInt(Integer::parseInt).toArray(), i));
            }
        }
        int cursorsCount = cursors.size();

        long finish = System.currentTimeMillis();
        System.out.println("Get cursors time: " + (finish-start) + "ms");

        start = System.currentTimeMillis();

        ArrayList<SelectWithSimilarity> resultList = new ArrayList<>();

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
            double similarity = (double)count/(double)(grammarFiles.getPathsSize().get(min)+cursorsCount-count)*100;
            similarity = Math.round(similarity*100.0) / 100.0;
            resultList.add(new SelectWithSimilarity((grammarFiles.getQueries().get(min).getQuery()), similarity, grammarFiles.getQueries().get(min).getRowId()));
        }

        finish = System.currentTimeMillis();
        System.out.println("Computing time: " + (finish-start) + "ms");


        start = System.currentTimeMillis();
        Collections.sort(resultList, new SelectComparator());
        finish = System.currentTimeMillis();
        System.out.println("Sorting time: " + (finish-start) + "ms");


        SelectWithSimilarity[] top20 = new SelectWithSimilarity[20];

        for(int i = 0; i < 20; i++)
            top20[i] = resultList.get(i);

        return top20;
    }
}
