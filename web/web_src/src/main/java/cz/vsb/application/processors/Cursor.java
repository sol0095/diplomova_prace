package cz.vsb.application.processors;

public class Cursor {
    public int[] queryIds;
    int currentPosition;
    int pathId;

    public Cursor(int[] queryIds, int pathId) {
        this.queryIds = queryIds;
        this.currentPosition = 0;
        this.pathId = pathId;
    }

    public void moveNext(){
        currentPosition++;
    }

    public int getCurrent(){
        return queryIds[currentPosition];
    }

    public int currentPosition(){
        return currentPosition;
    }

    public boolean isInRange(){
        return currentPosition < queryIds.length;
    }
}
