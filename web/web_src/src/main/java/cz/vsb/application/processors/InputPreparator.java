package cz.vsb.application.processors;

import cz.vsb.application.GrammarState;
import cz.vsb.application.Pair;
import cz.vsb.application.files.FilesLoader;
import cz.vsb.application.files.GrammarFiles;
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
import java.util.HashSet;

import static org.antlr.v4.runtime.CharStreams.fromString;

public class InputPreparator {

    private HashSet<Integer> inputHash = new HashSet<>();
    private GrammarFiles grammarFiles;

    public GrammarState prepareInput(String query, char grammar, String queryStmt){
       // long start= System.currentTimeMillis();


        ParseTree parseTree = null;
        Parser parser = null;

        //only for check if query is in correct shape (contains characters, is uppercase...)
        Pair<GrammarState, String> pair = checkQuery(query);
        if(pair.getFirst() != GrammarState.CORRECT)
            return pair.getFirst();

        query = pair.getSecond();
        //System.out.println("Checking input time: " + (System.currentTimeMillis()-start) + "ms");

        if(grammar == '0'){
            MySqlLexer lexer = new MySqlLexer(fromString(query));
            parser = new MySqlParser(new CommonTokenStream(lexer));
            removeErrorListeners(lexer, parser);
            parseTree = ((MySqlParser)parser).root();
        }
        else if(grammar == '1'){
            SQLiteLexer lexer = new SQLiteLexer(fromString(query));
            parser = new SQLiteParser(new CommonTokenStream(lexer));
            removeErrorListeners(lexer, parser);
            parseTree = ((SQLiteParser)parser).parse();
        }
        else if(grammar == '2'){
            TSqlLexer lexer = new TSqlLexer(fromString(query));
            parser = new TSqlParser(new CommonTokenStream(lexer));
            removeErrorListeners(lexer, parser);
            parseTree = ((TSqlParser)parser).tsql_file();
        }
        else if(grammar == '3'){
            PlSqlLexer lexer = new PlSqlLexer(fromString(query));
            parser = new PlSqlParser(new CommonTokenStream(lexer));
            removeErrorListeners(lexer, parser);
            parseTree = ((PlSqlParser)parser).sql_script();
        }

        if(parseTree != null && parser.getNumberOfSyntaxErrors() == 0){
            //long finish= System.currentTimeMillis();
           // System.out.println("Grammar time: " + (finish-start) + "ms");

           // start = System.currentTimeMillis();
            ResultPreparator resultPreparator = new ResultPreparator();
            resultPreparator.prepareData(query, parseTree, parser);
          //  finish = System.currentTimeMillis();
          //  System.out.println("Converting tree to string xml time: " + (finish-start) + "ms");

            grammarFiles = FilesLoader.getGrammar(grammar);
            prepareInputPaths(resultPreparator.getXmlData(), queryStmt);
            return GrammarState.CORRECT;
        }
        return GrammarState.WRONG;
    }

    private Pair<GrammarState, String> checkQuery(String query){

        if(query == null)
            return new Pair<>(GrammarState.WRONG, null);
        else if(query.length() == 0)
            return new Pair<>(GrammarState.EMPTY, null);

        query = query.toUpperCase().replaceAll("\\s"," ");

        if(!query.matches(".*[A-Z].*"))
            return new Pair<>(GrammarState.EMPTY, null);

        return new Pair<>(GrammarState.CORRECT, query);
    }

    private void removeErrorListeners(Lexer lexer, Parser parser){
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
    }

    private void prepareInputPaths(String xmlTree, String queryStmt){
       // long start = System.currentTimeMillis();
      //  long start2 = System.currentTimeMillis();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            ArrayList<String> inputPaths = new ArrayList<>();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(new InputSource(new StringReader(xmlTree)));
          //  long finish2 = System.currentTimeMillis();
          //  long convertTime = finish2 - start2;


            XmlTreeView.getLeafPaths((Element)(document.getElementsByTagName(queryStmt).item(0)), new StringBuilder(), inputPaths);
            HashSet<String> inputHashStr = new HashSet<>();

            for(String s : inputPaths){
                int i = 0;
                while(inputHashStr.contains(s+ "." + i)){
                    i++;
                }
                inputHashStr.add(s+ "." + i);
            }

            getPathsIDs(inputHashStr);
            //long finish = System.currentTimeMillis();

          //  System.out.println("Converting string to xml: " + convertTime + "ms");
           // System.out.println("Getting paths from input query: " + (finish-start-convertTime) + "ms");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPathsIDs(HashSet<String> inputHashStr){
        for(String s : inputHashStr){
            inputHash.add(grammarFiles.getMappingHash().get(s));
        }
    }

    public HashSet<Integer> getInputPaths(){
        return inputHash;
    }
}
