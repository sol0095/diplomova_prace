package cz.vsb.application.processors;

import cz.vsb.application.files.PathFileWriter;
import cz.vsb.application.selects.SelectWithPaths;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;

public class PathGenerator {

    private static int mainTagLength = "</sqlSelects>".length();
    public static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    private static void writeToFile(ArrayList<SelectWithPaths> selectsWithPaths, PathsMap pathsMap) {
        StringBuilder strTofile = new StringBuilder();

        for(SelectWithPaths selectWithPaths : selectsWithPaths){
            HashSet<String> selectPahts = new HashSet<>();
            strTofile.append(selectWithPaths.getId() + "_" + selectWithPaths.getRowId() + "_" + selectWithPaths.getQuery()+ "\n");

            for(String s : selectWithPaths.getPaths()){
                int i = 0;

                while(selectPahts.contains(s + "." + i))
                    i++;

                synchronized (pathsMap){
                    if(pathsMap.pathsWithNums.containsKey(s + "." + i)){
                        PathsMapValue pmv = pathsMap.pathsWithNums.get(s + "." + i);
                        pmv.rowIds.add(selectWithPaths.getId());
                    }
                    else{
                        PathsMapValue pmv = new PathsMapValue();
                        pmv.pathNum = pathsMap.pathNum;
                        pmv.rowIds.add(selectWithPaths.getId());
                        pathsMap.pathsWithNums.put(s + "." + i, pmv);
                        pathsMap.pathNum++;
                    }
                }

                selectPahts.add(s + "." + i);
            }
        }

        PathFileWriter.write( strTofile.toString());
    }

    public static ArrayList<SelectWithPaths> generate(String line, PathsMap pathsMap, String queryStmt){
        ArrayList<SelectWithPaths> selectsWithPaths = new ArrayList<>();

        if(line.length() > mainTagLength){
            try {
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document document = dBuilder.parse(new InputSource(new StringReader(line)));
                NodeList nodeList = document.getElementsByTagName(queryStmt);
                String selectCode = document.getElementsByTagName("selectCode").item(0).getFirstChild().getNodeValue();
                int rowId = Integer.parseInt(document.getElementsByTagName("rowId").item(0).getFirstChild().getNodeValue());

                if(nodeList.getLength() > 1){
                    System.out.println("error");
                }

                for (int i = 0; i < nodeList.getLength(); i++) {
                    ArrayList<String> pathsInTree = new ArrayList<>();
                    XmlTreeView.getLeafPaths((Element) nodeList.item(i), new StringBuilder(), pathsInTree);

                    synchronized (pathsMap){
                        selectsWithPaths.add(new SelectWithPaths(pathsMap.uniqueID, rowId, selectCode, pathsInTree));
                        pathsMap.uniqueID++;
                    }
                }
                writeToFile(selectsWithPaths, pathsMap);

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return selectsWithPaths;
    }
}
