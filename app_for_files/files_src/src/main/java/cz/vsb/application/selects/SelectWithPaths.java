package cz.vsb.application.selects;

import java.util.ArrayList;

public class SelectWithPaths {

    private int id;
    private int rowId;
    private String query;
    private ArrayList<String> paths;
    private ArrayList<Integer> pathsIds;

    public SelectWithPaths(int id, int rowId, String query, ArrayList<String> paths) {
        this.id = id;
        this.rowId = rowId;
        this.query = query;
        this.paths = paths;
    }

    public String getQuery() {
        return query;
    }

    public ArrayList<String> getPaths() {
        return paths;
    }

    public int getRowId(){
        return rowId;
    }

    public int getId(){
        return id;
    }

    public ArrayList<Integer> getPathsIds() {
        return pathsIds;
    }

    public void setPathsIds(ArrayList<Integer> pathsIds) {
        this.pathsIds = pathsIds;
    }
}
