package cz.vsb.application.processors;

import java.util.HashMap;

public class PathsMap {
    public HashMap<String, PathsMapValue> pathsWithNums;
    public int pathNum;
    public int uniqueID;

    public PathsMap(){
        this.pathsWithNums = new HashMap<>();
        this.pathNum = 0;
        this.uniqueID = 0;
    }
}
