package filelocal;


import Data.MyFile;
import paket.Configuration;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private MyFile myFile;
    public static void main(String[] args) throws IOException {
        FileLocalImpl fileLocal = new FileLocalImpl();
        fileLocal.createRoot("/Users/andrejadikic/Documents/SKProjekat","root",new Configuration());

//        fileLocal.mkdir("dir1");
//        fileLocal.delete("dir1");
//        fileLocal.mkdir("dir",5);
//        fileLocal.mkdir("dir1","dir4");
//        fileLocal.mkdir("/dir2","subdir",20);
//        fileLocal.move("dir2/subdir2","dir1/subdir4");
        fileLocal.rename("dir1/subdir1","dir2");
        //fileLocal.rename("dir1/subdir4","subdir3");
    }
}
