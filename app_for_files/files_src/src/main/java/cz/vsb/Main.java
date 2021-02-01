package cz.vsb;

import cz.vsb.application.Application;

public class Main{

    public static void main(String args[]) {

        try {
            Application.writePathsTofile();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("There is a problem with input files. Please check correct paths.");
        }
    }
}
