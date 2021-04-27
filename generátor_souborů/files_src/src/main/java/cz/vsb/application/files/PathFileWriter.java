package cz.vsb.application.files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PathFileWriter {
    private BufferedWriter bufferedWriter;

    public synchronized void write(String str){
        try {
            bufferedWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startWriting(String path){
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(path, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopWriting(){
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
