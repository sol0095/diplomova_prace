package cz.vsb.application.processors;

import cz.vsb.application.files.PropertyLoader;

public class InputThread extends Thread{

    private char grammar;
    private String queryStmt;
    private String query;
    private InputPreparator inputPreparator;

    public InputThread(char grammar, String queryStmt, String query, InputPreparator inputPreparator){
        this.grammar = grammar;
        this.queryStmt = queryStmt;
        this.query = query;
        this.inputPreparator = inputPreparator;
    }
    @Override
    public void run() {
        inputPreparator.prepareInput(query, grammar, queryStmt);
    }
}
