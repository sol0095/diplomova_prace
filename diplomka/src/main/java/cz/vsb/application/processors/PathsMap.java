package cz.vsb.application.processors;

import java.util.HashMap;

public class PathsMap {
    public HashMap<String, PathsMapValue> pathsWithNums;
    public int pathNum;

    public PathsMap(){
        this.pathsWithNums = new HashMap<>();
        this.pathNum = 0;
    }
}
