package cz.vsb.controllers;

import cz.vsb.application.Application;
import cz.vsb.application.files.PropertyLoader;
import cz.vsb.application.selects.SelectWithSimilarity;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GrammarController {

    @RequestMapping("/query_data")
    @CrossOrigin(origins = "*")
    public SelectWithSimilarity[] getData(@RequestParam String query, @RequestParam char grammar) {
        // char grammar = PropertyLoader.loadProperty("grammar").charAt(0);
        String queryStmt = getStmtName(grammar);
        try {
            return Application.calculateSimilarity(grammar, queryStmt, query);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
