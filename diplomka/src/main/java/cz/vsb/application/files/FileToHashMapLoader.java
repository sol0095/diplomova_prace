package cz.vsb.application.files;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class FileToHashMapLoader extends Thread{

    private Map<Integer, String> map;
    private Stream<String> lines;

    public FileToHashMapLoader(Stream<String> lines){
        this.map = new ConcurrentHashMap<>();
        this.lines = lines;
    }

    @Override
    public void run() {
        lines.forEach(l->{
            int firstSplit = l.indexOf('_');
            map.put(Integer.parseInt(l.substring(0, firstSplit)), l.substring(firstSplit+1));
        });
    }

    public Map<Integer, String> getMap() {
        return map;
    }
}
