package filelocal;


import Data.MyFile;
import paket.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {
    private MyFile myFile;
    public static void main(String[] args) throws IOException {
        FileLocalImpl fileLocal = new FileLocalImpl();
        //fileLocal.createRoot("/Users/andrejadikic/Documents/SKProjekat","root",new Configuration());

        File file = new File("/Users/andrejadikic/Documents/SKProjekat");
        Path path = Paths.get("/Users/andrejadikic/Documents/SKProjekat");
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        FileTime time = attr.lastModifiedTime();
        LocalDateTime ldt =  LocalDateTime.ofInstant( time.toInstant(), ZoneId.systemDefault());



//        fileLocal.mkdir("dir1");
//        fileLocal.delete("dir1");
//        fileLocal.mkdir("dir",5);
//        fileLocal.mkdir("dir1","dir4");
//        fileLocal.mkdir("/dir2","subdir",20);
//        fileLocal.move("dir2/subdir2","dir1/subdir4");
        //fileLocal.rename("dir1/subdir1","dir2");
        //fileLocal.rename("dir1/subdir4","subdir3");
    }
}
