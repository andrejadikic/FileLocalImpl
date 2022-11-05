package filelocal;


import Data.MyFile;
import com.google.gson.Gson;
import lombok.Getter;
import paket.Configuration;
import paket.FileManager;
import paket.Metadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class FileLocalImpl extends FileManager {

//    public FileLocalImpl() {
//        rootPath="";
//    }

    @Override
    public boolean createRoot(String path, String name, Configuration configuration) throws IOException {
        if(mkdir(path,name)){
            rootPath=(path+File.separator+name);
            this.configuration=(configuration);
            //saveConfig();
            return true;
        }else{
            System.out.println("Error found");
            return false;
        }
    }
//parentPath je apsolutna putanja
    @Override
    protected boolean checkConfig(String parentPath, String ext, long size) {

        if(rootPath==null)
            return true;
        else{
            try {
                String pr = rootPath;
                Path rootPath = Paths.get(pr);
                Path path = Paths.get(parentPath);
                List<String> names = new ArrayList<>();
                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
                for(Path path1:directoryStream){
                    names.add(path1.toString());

                }
                boolean brojFajlova = (configuration.getFile_n().size() == 0) ||
                        (!configuration.getFile_n().containsKey(parentPath)) ||
                        (configuration.getFile_n().containsKey(parentPath) && configuration.getFile_n().get(parentPath)>=names.size()+1);
                //System.out.println(parentPath+" "+ configuration.getFile_n().get(parentPath) + " \n"+ names);
                return !configuration.getExcludedExt().contains(ext) &&
                        configuration.getSize() >= Files.size(rootPath) + size &&
                        brojFajlova;
                // TODO za mapu filen

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }



    @Override
    public boolean mkdir(String path, String name) {
        File dir = new File(getFullPath(path+File.separator+name));
        String ext = name.lastIndexOf(".")!=-1 ? name.substring(name.lastIndexOf(".")+1): "";
        if(dir.getParentFile().exists() && checkConfig(dir.getParentFile().getAbsolutePath(),ext,dir.length())){
            dir.mkdir();
        }
//        if(!dir.getParentFile().exists()){
//            dir.mkdirs();
        return dir.exists();
    }

    @Override
    public boolean delete(String path) {
        return new File(getFullPath(path)).delete();
    }

    @Override
    public boolean move(String oldPath, String newPath) {
        // newPath je destinacija bez imena
        try {
            Files.move(Paths.get(getFullPath(oldPath)),
                    Paths.get(getFullPath(newPath)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rename(String path, String name) {
        String newPath = Paths.get(getFullPath(path)).getParent().toString()
                + File.separator + name;
        try {
            Files.move(Paths.get(getFullPath(path)),
                    Paths.get(newPath));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean download(String s, String s1) {
        return false;
    }

    @Override
    public boolean upload(String s, String s1) {
        return false;
    }

    @Override
    public List<MyFile> searchDir(String filepath) {
        Path path = Paths.get(getFullPath(filepath));
        List<MyFile> myFiles = new ArrayList<>();
        try{
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            for(Path path1:directoryStream){
                BasicFileAttributes attr = Files.readAttributes(path1, BasicFileAttributes.class);
                String name = path1.getFileName().toString();
                String ext = name.lastIndexOf(".")!=-1 ? name.substring(name.lastIndexOf(".")+1): "";
                long size = Files.size(path);
                LocalDateTime lastModified =  LocalDateTime.ofInstant( attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                LocalDateTime timeCreated = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
                myFiles.add(new MyFile(getFullPath(path1.toString()),name,size,lastModified,timeCreated,ext));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return myFiles;
    }


    @Override
    public List<MyFile> searchSubDir(String s) {
        return null;
    }

    @Override
    public List<MyFile> filterByExt(String s, String s1) {
        return null;
    }

    @Override
    public List<MyFile> searchSubstring(String s) {
        return null;
    }

    @Override
    public boolean existName(String s, String s1) {
        return false;
    }

    @Override
    public List<String> getParentPath(String s) {
        return null;
    }

    @Override
    public List<MyFile> sortBy(List<MyFile> list, Metadata metadata) {
        return null;
    }

    @Override
    public List<String> filterData(List<MyFile> list, List<Metadata> list1) {

        return null;
    }

    @Override
    public List<MyFile> filterByPeriod(String s, LocalDateTime localDateTime, LocalDateTime localDateTime1, boolean b) {
        return null;
    }

    @Override
    public void saveConfig() {

        try(FileWriter writer = new FileWriter(rootPath+File.separator+"config.json")) {
            Gson gson = new Gson();
            gson.toJson(configuration, writer);
            //writer.close();
            //File javaFile = new File("src/main/resources/config.json");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected String getFullPath(String s) {
        if(rootPath==null)
            return s;
        if(s.equalsIgnoreCase(rootPath) || s.equals(""))
            return rootPath;
        if(s.startsWith(File.separator))
            return rootPath+s;
        return rootPath+File.separator+s;
    }

}
