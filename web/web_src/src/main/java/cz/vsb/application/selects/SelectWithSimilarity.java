package cz.vsb.application.selects;

import com.github.vertical_blank.sqlformatter.SqlFormatter;

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

    public void formatQuery(char grammar){
        try{
            if(grammar == '2' || grammar == '3')
                this.query = SqlFormatter.of("pl/sql").format(query);
            else
                this.query = SqlFormatter.format(query);
        }catch (RuntimeException e){
            System.out.println(query);
            System.out.println("This SQL code cannot be formatted!");
        }

    }
}
