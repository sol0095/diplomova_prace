package cz.vsb.application.selects;

public class Select {

    private int rowId;
    private String query;

    public Select(int rowId, String query) {
        this.rowId = rowId;
        this.query = query;
    }

    public int getRowId() {
        return rowId;
    }

    public String getQuery() {
        return query;
    }
}
