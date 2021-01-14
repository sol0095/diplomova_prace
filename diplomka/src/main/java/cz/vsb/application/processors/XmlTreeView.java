package cz.vsb.application.processors;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;

public class XmlTreeView {
    private static String[] searchedWords = {
            "JOIN", "GROUP BY", "ORDER BY", "MAX", "MIN", "SUM", "COUNT", "ASC", "DESC"
    };

    public static void getLeafPaths(Element elem, StringBuilder path, ArrayList<String> pathsInTree) {
        final int pathLen = path.length();
        if (pathLen != 0)
            path.append("->");
        path.append(elem.getTagName());

        if(elem.getTagName().equals("specialWord")){
            String str = elem.getTextContent();
            if(str != null && checkSearchedWords(str))
                path.append(str);
        }

        boolean hasChild = false;
        for (Node child = elem.getFirstChild(); child != null; child = child.getNextSibling())
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                hasChild = true;
                getLeafPaths((Element)child, path, pathsInTree);
            }
        if (! hasChild)
            pathsInTree.add(path.toString());
        path.setLength(pathLen);
    }

    private static boolean checkSearchedWords(String str){
        for(String s : searchedWords){
            //if(str.contains(s) && !str.contains("\"") && !str.contains("\'") && !str.contains("_") &&)
            int index = str.indexOf(s);
            //tr.length() == s.length()+1 && str.charAt(str.length()-1) == '(')
           /* if(((index == 0 && str.length() <= s.length()+2) || (index > 0 && str.contains(" "))) && )
                return true;*/

            if(!str.contains("\"") && !str.contains("\'") && !str.contains("_")){
                if(index == 0){
                    if(str.equals(s) || str.equals(s + "(") || str.equals(s + " ("))
                        return true;
                }
                else if(index > 0 && str.contains(" "))
                    return true;
            }
        }
        return false;
    }
}
