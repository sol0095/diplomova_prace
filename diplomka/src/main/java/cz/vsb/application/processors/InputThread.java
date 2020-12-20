package cz.vsb.application.processors;

import cz.vsb.application.files.PropertyLoader;

public class InputThread extends Thread{

    private char grammar;
    private String queryStmt;
    public InputThread(char grammar, String queryStmt){
        this.grammar = grammar;
        this.queryStmt = queryStmt;
    }
    @Override
    public void run() {
        InputPreparator.prepareInput(PropertyLoader.loadProperty("inputQuery"), grammar, queryStmt);
    }
}
