package filelocal;


import Data.MyFile;
import org.apache.commons.io.FileUtils;
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
        fileLocal.createRoot("/Users/andrejadikic/Documents/SKProjekat","root",new Configuration(),8);
        fileLocal.saveConfig();


        File file = new File("/Users/andrejadikic/Documents/SKProjekat");

        Path path = Paths.get("/Users/andrejadikic/Documents/SKProjekat");
        System.out.println(Files.size(path));
//        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
//        FileTime time = attr.lastModifiedTime();
//        LocalDateTime ldt =  LocalDateTime.ofInstant( time.toInstant(), ZoneId.systemDefault());
//


//        fileLocal.mkdir("","dir1",7,true);
//        fileLocal.mkdir("","nsdjfn",8,true);
//        fileLocal.mkdir("","fdz",7,true);
//        fileLocal.mkdir("","nsdj`saafn",7,true);
//        fileLocal.mkdir("","dir2",7,true);
//        fileLocal.mkdir("","aa",7,true);
//        fileLocal.mkdir("dir",5);
        //fileLocal.delete("dir_1");

        System.out.println(FileUtils.sizeOfDirectory(file));
        System.out.println(fileLocal.searchDir(""));

//        fileLocal.mkdir("nsdjfn","dirr",10,false);
//        fileLocal.mkdir("/dir2","subdir",10,false);

        //fileLocal.saveConfig();
//        fileLocal.move("dir2/subdir2","dir1/subdir4");
        //fileLocal.rename("dir1/subdir1","dir2");
        //fileLocal.rename("dir1/subdir4","subdir3");
    }
}
