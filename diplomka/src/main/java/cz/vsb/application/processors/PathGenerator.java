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
            strTofile.append(selectWithPaths.getId() + "|sep|" + selectWithPaths.getQuery() + "|sep|");
            Integer num = null;
            ArrayList<Integer> pathsIds = new ArrayList<>();

            for(String s : selectWithPaths.getPaths()){
                int i = 0;

                while(selectPahts.contains(s + "." + i))
                    i++;

                synchronized (pathsMap){
                    if(pathsMap.pathsWithNums.containsKey(s + "." + i)){
                        PathsMapValue pmv = pathsMap.pathsWithNums.get(s + "." + i);
                        num = pmv.pathNum;
                        pmv.rowIds.add(selectWithPaths.getId());
                    }
                    else{
                        PathsMapValue pmv = new PathsMapValue();
                        pmv.pathNum = pathsMap.pathNum;
                        pmv.rowIds.add(selectWithPaths.getId());
                        pathsMap.pathsWithNums.put(s + "." + i, pmv);
                        num = pathsMap.pathNum;
                        pathsMap.pathNum++;
                    }
                }

                selectPahts.add(s + "." + i);
                strTofile.append(num + ",");
                pathsIds.add(num);
                selectWithPaths.setPathsIds(pathsIds);
            }
            strTofile.append("\n");
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
                int id = Integer.parseInt(document.getElementsByTagName("rowId").item(0).getFirstChild().getNodeValue());

                for(int i = 0; i < nodeList.getLength(); i++){
                    ArrayList<String> pathsInTree = new ArrayList<>();
                    XmlTreeView.getLeafPaths((Element)nodeList.item(i), new StringBuilder(), pathsInTree);
                    selectsWithPaths.add(new SelectWithPaths(id, selectCode, pathsInTree));
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
