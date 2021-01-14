package cz.vsb.application.processors;

import cz.vsb.application.files.InputFileReader;
import cz.vsb.application.files.PropertyLoader;
import cz.vsb.grammars.*;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.antlr.v4.runtime.CharStreams.fromString;

public class InputPreparator {

    private static HashSet<Integer> inputHash = new HashSet<>();

    public static void prepareInput(String query, char grammar, String queryStmt){
        long start= System.currentTimeMillis();
        ParseTree parseTree = null;
        Parser parser = null;
        query = query.toUpperCase();

        if(grammar == '0'){
            MySqlLexer lexer = new MySqlLexer(fromString(query));
            parser = new MySqlParser(new CommonTokenStream(lexer));
            parseTree = ((MySqlParser)parser).root();
        }
        else if(grammar == '1'){
            SQLiteLexer lexer = new SQLiteLexer(fromString(query));
            parser = new SQLiteParser(new CommonTokenStream(lexer));
            parseTree = ((SQLiteParser)parser).parse();
        }
        else if(grammar == '2'){
            TSqlLexer lexer = new TSqlLexer(fromString(query));
            parser = new TSqlParser(new CommonTokenStream(lexer));
            parseTree = ((TSqlParser)parser).tsql_file();
        }
        else if(grammar == '3'){
            PlSqlLexer lexer = new PlSqlLexer(fromString(query));
            parser = new PlSqlParser(new CommonTokenStream(lexer));
            parseTree = ((PlSqlParser)parser).sql_script();
        }

        if(parseTree != null && parser.getNumberOfSyntaxErrors() == 0){
            long finish= System.currentTimeMillis();
            System.out.println("Grammar time: " + (finish-start) + "ms");
            start = System.currentTimeMillis();

            ResultPreparator resultPreparator = new ResultPreparator();
            resultPreparator.prepareData(query, parseTree, parser);
            finish = System.currentTimeMillis();
            System.out.println("Converting tree to string xml time: " + (finish-start) + "ms");

            prepareInputPaths(resultPreparator.getXmlData(), queryStmt);
        }
    }

    private static void removeErrorListeners(Lexer lexer, Parser parser){
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
    }

    private static void prepareInputPaths(String xmlTree, String queryStmt){
        long start = System.currentTimeMillis();
        long start2 = System.currentTimeMillis();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            ArrayList<String> inputPaths = new ArrayList<>();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(new InputSource(new StringReader(xmlTree)));
            long finish2 = System.currentTimeMillis();
            long convertTime = finish2 - start2;


            XmlTreeView.getLeafPaths((Element)(document.getElementsByTagName(queryStmt).item(0)), new StringBuilder(), inputPaths);
            HashSet<String> inputHashStr = new HashSet<>();

            for(String s : inputPaths){
                int i = 0;
                while(inputHashStr.contains(s+ "." + i)){
                    i++;
                }
                inputHashStr.add(s+ "." + i);
            }

            long creatingMapTime = getPathsIDs(inputHashStr);
            long finish = System.currentTimeMillis();

            System.out.println("Converting string to xml: " + convertTime + "ms");
            System.out.println("Getting paths from input query: " + (finish-start-creatingMapTime-convertTime) + "ms");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long getPathsIDs(HashSet<String> inputHashStr){

        long start = System.currentTimeMillis();
        Stream<String> lines = InputFileReader.readFile(PropertyLoader.loadProperty("pathToIdFile"));
        HashMap<String, Integer> mappingHash = new HashMap<>();

        lines.forEach(l->{
            int firstSplit = l.indexOf('_');
            synchronized (mappingHash){
                mappingHash.put(l.substring(0, firstSplit), Integer.parseInt(l.substring(firstSplit+1)));
            }
        });
        long finish = System.currentTimeMillis();
        long creatingMapTime = finish - start;
        System.out.println("Converting file to hashmap: " + creatingMapTime + "ms");


        for(String s : inputHashStr){
            inputHash.add(mappingHash.get(s));
        }

        return creatingMapTime;

        /*lines.forEach(s ->{
            int split = s.indexOf('_');
            if(inputHashStr.contains(s.substring(0, split))) {
                synchronized (inputHash) {
                    inputHash.add(Integer.parseInt(s.substring(split+1)));
                }
            }
        });*/



    }

    public static HashSet<Integer> getInputPaths(){
        return inputHash;
    }
}
