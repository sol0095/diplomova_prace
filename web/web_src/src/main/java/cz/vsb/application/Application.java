package cz.vsb.application;

import cz.vsb.application.files.*;
import cz.vsb.application.processors.*;
import cz.vsb.application.selects.SelectComparator;
import cz.vsb.application.selects.SelectWithSimilarity;

import java.io.IOException;
import java.util.*;

public class Application{

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


        //getting cursors
        long start = System.currentTimeMillis();

        ArrayList<Cursor> cursors = createCursors(inputPreparator, grammarFiles);

        long finish = System.currentTimeMillis();
        System.out.println("Get cursors time: " + (finish-start) + "ms");


        //computing similarity
        start = System.currentTimeMillis();

        ArrayList<SelectWithSimilarity> resultList = iterateCursors(cursors, grammarFiles);

        finish = System.currentTimeMillis();
        System.out.println("Computing time: " + (finish-start) + "ms");


        //sorting
        start = System.currentTimeMillis();

        Collections.sort(resultList, new SelectComparator());

        finish = System.currentTimeMillis();
        System.out.println("Sorting time: " + (finish-start) + "ms");


        return getTop20(resultList, grammar);
    }

    private static ArrayList<Cursor> createCursors(InputPreparator inputPreparator, GrammarFiles grammarFiles){
        ArrayList<Cursor> cursors = new ArrayList<>();
        Map<Integer,String> paths = grammarFiles.getPaths();

        HashSet<Integer> pathIds = inputPreparator.getInputPaths();
        for(Integer i : pathIds){
            if(paths.containsKey(i)){
                String[] idsStr = paths.get(i).split(",");
                cursors.add(new Cursor(Arrays.stream(idsStr).mapToInt(Integer::parseInt).toArray(), i));
            }
        }

        return cursors;
    }

    private static ArrayList<SelectWithSimilarity> iterateCursors(ArrayList<Cursor> cursors, GrammarFiles grammarFiles){
        ArrayList<SelectWithSimilarity> resultList = new ArrayList<>();
        int cursorsCount = cursors.size();

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

        return resultList;
    }

    private static SelectWithSimilarity[] getTop20(ArrayList<SelectWithSimilarity> resultList, char grammar){
        long start = System.currentTimeMillis();

        SelectWithSimilarity[] top20 = new SelectWithSimilarity[20];

        for(int i = 0; i < 20; i++){
            top20[i] = resultList.get(i);
            top20[i].formatQuery(grammar);
        }

        long finish = System.currentTimeMillis();
        System.out.println("Formatting time: " + (finish-start) + "ms");

        return top20;
    }
}
