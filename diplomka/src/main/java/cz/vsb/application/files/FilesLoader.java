package cz.vsb.application.files;

public class FilesLoader {

    private static GrammarFiles mysql;
    private static GrammarFiles tsql;
    private static GrammarFiles sqlite;
    private static GrammarFiles plsql;

    public static void loadFiles() throws InterruptedException {
        System.out.println("Loading files into hashmap...");
        long start = System.currentTimeMillis();
        setInstances();
        runLoading();
        long finish = System.currentTimeMillis();
        System.out.println("Files loading time: " + (finish-start) + "ms");
    }

    private static void setInstances(){
        mysql = new GrammarFiles(PropertyLoader.loadProperty("mysqlPathToIdFile"),
                PropertyLoader.loadProperty("mysqlPathIdWithRowIdsFile"),
                PropertyLoader.loadProperty("mysqlSelectsFile"),
                PropertyLoader.loadProperty("mysqlPathsSizeFile"));
        tsql = new GrammarFiles(PropertyLoader.loadProperty("tsqlPathToIdFile"),
                PropertyLoader.loadProperty("tsqlPathIdWithRowIdsFile"),
                PropertyLoader.loadProperty("tsqlSelectsFile"),
                PropertyLoader.loadProperty("tsqlPathsSizeFile"));
        sqlite = new GrammarFiles(PropertyLoader.loadProperty("sqlitePathToIdFile"),
                PropertyLoader.loadProperty("sqlitePathIdWithRowIdsFile"),
                PropertyLoader.loadProperty("sqliteSelectsFile"),
                PropertyLoader.loadProperty("sqlitePathsSizeFile"));
        plsql = new GrammarFiles(PropertyLoader.loadProperty("plsqlPathToIdFile"),
                PropertyLoader.loadProperty("plsqlPathIdWithRowIdsFile"),
                PropertyLoader.loadProperty("plsqlSelectsFile"),
                PropertyLoader.loadProperty("plsqlPathsSizeFile"));
    }

    private static void runLoading() throws InterruptedException {
        mysql.start();
        tsql.start();
        sqlite.start();
        plsql.start();

        mysql.join();
        tsql.join();
        sqlite.join();
        plsql.join();
    }

    public static GrammarFiles getGrammar(char grammar){
        GrammarFiles grammarFiles = null;

        switch (grammar){
            case '0':
                grammarFiles = mysql;
                break;
            case '1':
                grammarFiles = sqlite;
                break;
            case '2':
                grammarFiles = tsql;
                break;
            case '3':
                grammarFiles = plsql;
                break;
        }

        return grammarFiles;
    }
}
