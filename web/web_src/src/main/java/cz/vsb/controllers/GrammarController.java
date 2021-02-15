package cz.vsb.controllers;

import cz.vsb.application.Application;
import cz.vsb.application.files.PropertyLoader;
import cz.vsb.application.selects.SelectWithSimilarity;
import org.apache.tomcat.util.codec.binary.Base64;
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
        String queryStmt = getStmtName(grammar);
        return Application.calculateSimilarity(grammar, queryStmt, new String(Base64.decodeBase64(query.getBytes())));
    }

    private static String getStmtName(char grammar){
        String queryStmt = null;
        switch(grammar) {
            case '0'://mysql
                queryStmt = "root";
                break;
            case '1'://sqlite
                queryStmt = "parse";
                break;
            case '2'://tsql
                queryStmt = "tsql_file";
                break;
            case '3'://plsql
                queryStmt = "sql_script";
                break;
        }
        return queryStmt;
    }
}
