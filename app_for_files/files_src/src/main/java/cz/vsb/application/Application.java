package cz.vsb.application;

import cz.vsb.application.files.GrammarFiles;
import cz.vsb.application.files.InputFileReader;
import cz.vsb.application.files.PathFileWriter;
import cz.vsb.application.files.PropertyLoader;
import cz.vsb.application.processors.*;
import java.util.*;
import java.util.stream.Stream;

public class Application{

    public static void writePathsTofile() throws InterruptedException {
        long start = System.currentTimeMillis();

        GrammarFiles mysql = new GrammarFiles(PropertyLoader.loadProperty("mysqlPathToIdFile"),
                PropertyLoader.loadProperty("mysqlPathIdWithRowIdsFile"),
                PropertyLoader.loadProperty("mysqlSelectsFile"),
                PropertyLoader.loadProperty("mysqlPathsSizeFile"), PropertyLoader.loadProperty("mysqlXmlFile"), "root");
        GrammarFiles sqlite = new GrammarFiles(PropertyLoader.loadProperty("sqlitePathToIdFile"),
                PropertyLoader.loadProperty("sqlitePathIdWithRowIdsFile"),
                PropertyLoader.loadProperty("sqliteSelectsFile"),
                PropertyLoader.loadProperty("sqlitePathsSizeFile"), PropertyLoader.loadProperty("sqliteXmlFile"), "parse");
        GrammarFiles tsql = new GrammarFiles(PropertyLoader.loadProperty("tsqlPathToIdFile"),
                PropertyLoader.loadProperty("tsqlPathIdWithRowIdsFile"),
                PropertyLoader.loadProperty("tsqlSelectsFile"),
                PropertyLoader.loadProperty("tsqlPathsSizeFile"), PropertyLoader.loadProperty("tsqlXmlFile"), "tsql_file");
        GrammarFiles plsql = new GrammarFiles(PropertyLoader.loadProperty("plsqlPathToIdFile"),
                PropertyLoader.loadProperty("plsqlPathIdWithRowIdsFile"),
                PropertyLoader.loadProperty("plsqlSelectsFile"),
                PropertyLoader.loadProperty("plsqlPathsSizeFile"), PropertyLoader.loadProperty("plsqlXmlFile"), "sql_script");


        mysql.start();
        sqlite.start();
        tsql.start();
        plsql.start();

        mysql.join();
        sqlite.join();
        tsql.join();
        plsql.join();

        long finish = System.currentTimeMillis();
        System.out.println("Writing time: " + (finish-start) + "ms");
    }
}
