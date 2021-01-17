package cz.vsb.application.selects;

public class SelectWithSimilarity {

    private String query;
    private double similarity;
    private int rowId;

    public SelectWithSimilarity(String query, double similarity, int rowId) {
        this.similarity = similarity;
        this.query = query;
        this.rowId = rowId;
    }

    public String getQuery() {
        return query;
    }

    public double getSimilarity() {
        return similarity;
    }

    public int getRowId() {
        return rowId;
    }
}
