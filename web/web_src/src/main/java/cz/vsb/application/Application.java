package cz.vsb.application;

import cz.vsb.application.files.*;
import cz.vsb.application.processors.*;
import cz.vsb.application.selects.SelectComparator;
import cz.vsb.application.selects.SelectWithSimilarity;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Application{

    public static SelectWithSimilarity[] calculateSimilarity(char grammar, String queryStmt, String query){

        Semaphore semaphore = GrammarLock.getSemaphore();
        SelectWithSimilarity[] sortedResult = null;

        try {
            semaphore.acquire();
            GrammarFiles grammarFiles = FilesLoader.getGrammar(grammar);

            InputPreparator inputPreparator = new InputPreparator(grammarFiles);

            GrammarState grammarState = inputPreparator.prepareInput(query, grammar, queryStmt);

            if(grammarState != GrammarState.CORRECT){
                SelectWithSimilarity[] empty = new SelectWithSimilarity[1];
                empty[0] = grammarState == GrammarState.EMPTY ?
                        new SelectWithSimilarity("No similar query found.", 0.0, -1) :
                        new SelectWithSimilarity("Wrong query syntax.", 0.0, -1);
                return empty;
            }

            ArrayList<Cursor> cursors = createCursors(inputPreparator, grammarFiles);
            ArrayList<SelectWithSimilarity> resultList = iterateCursors(cursors, grammarFiles);

            Collections.sort(resultList, new SelectComparator());

            sortedResult = getTop20(resultList, grammar);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release();
        }

        if(sortedResult == null){
            SelectWithSimilarity[] empty = new SelectWithSimilarity[1];
            empty[0] = new SelectWithSimilarity("Error on server.", 0.0, -1);
            return empty;
        }

        return sortedResult;
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
      //  long start = System.currentTimeMillis();

        SelectWithSimilarity[] top20 = new SelectWithSimilarity[20];

        for(int i = 0; i < 20; i++){
            top20[i] = resultList.get(i);
            top20[i].formatQuery(grammar);
        }

       // long finish = System.currentTimeMillis();
       // System.out.println("Formatting time: " + (finish-start) + "ms");

        return top20;
    }
}
