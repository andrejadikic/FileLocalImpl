package filelocal;


import Data.MyException;
import Data.MyFile;
import com.google.gson.Gson;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import paket.Configuration;
import paket.FileManager;
import paket.RepoManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class FileLocalImpl extends FileManager {

    static {
        RepoManager.registerManager(new FileLocalImpl());
    }

    @Override
    public boolean createRoot(String path, String name, Configuration configuration) {
        if(mkdir(path,name)){
            rootPath=(path+File.separator+name);
            this.configuration=configuration;
            //saveConfig();
            return true;
        }else{
            System.out.println("nije napravljeno");
            return false;
        }
    }
// parentPath je apsolutna putanja
    @Override
    protected boolean checkConfig(String parentPath, String ext, long size){
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
                boolean check = !configuration.getExcludedExt().contains(ext) &&
                        configuration.getSize() >= Files.size(rootPath) + size &&
                        brojFajlova;
                if(!check)
                    System.out.println("Proveri konfiguraciju");
                return check;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }



    @Override
    public boolean mkdir(String path, String name) {
        File dir = new File(getFullPath(path+File.separator+name));
        String ext = name.lastIndexOf(".")!=-1 ? name.substring(name.lastIndexOf(".")+1): "";
        if(dir.getParentFile().exists() && checkConfig(dir.getParentFile().getAbsolutePath(),ext,0)){
            return dir.mkdir();
        }
        return dir.exists();
    }

    @Override
    public boolean delete(String path){
        boolean del = new File(getFullPath(path)).delete();
        if(del)
            return true;
        return false;
    }

    @Override
    public boolean move(String oldPath, String newPath) throws MyException{
        // newPath je destinacija bez imena
        try {
            String ext = FilenameUtils.getExtension(oldPath);
            String newP = getFullPath(newPath)+File.separator+FilenameUtils.getName(oldPath);
            if(checkConfig(getFullPath(newPath),ext,FileUtils.sizeOf(new File(getFullPath(oldPath))))){
                Files.move(Paths.get(getFullPath(oldPath)), Paths.get(newP));
                return true;
            }
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean rename(String path, String name){
        String newPath = Paths.get(getFullPath(path)).getParent().toString() + File.separator + name;
        try {
            Files.move(Paths.get(getFullPath(path)), Paths.get(newPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(newPath).exists();
    }

    @Override
    public boolean download(String item, String dest) {
        File it = new File(getFullPath(item));
        File des = new File(dest+File.separator+it.getName());
        try {
            FileUtils.copyFile(it,des);
        } catch (IOException e) {

        }
        return des.exists();
    }

    @Override
    public boolean upload(String item, String dest){
        File it = new File(item);
        File des = new File(getFullPath(dest)+File.separator+it.getName());
        try {
            String ext = item.lastIndexOf(".")!=-1 ? item.substring(item.lastIndexOf(".")+1): "";
            if(checkConfig(getFullPath(dest),ext,FileUtils.sizeOf(it)))
                FileUtils.copyFile(it,des);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return des.exists();
    }

    @Override
    public List<MyFile> searchDir(String filepath) {
        Path path = Paths.get(getFullPath(filepath));
        List<MyFile> myFiles = new ArrayList<>();
        try{
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            for(Path path1:directoryStream){
                File file = new File(path1.toString());
                if(file.isFile()) {
                    BasicFileAttributes attr = Files.readAttributes(path1, BasicFileAttributes.class);
                    String name = file.getName();
                    String ext = FilenameUtils.getExtension(path1.toString());
                    long size = FileUtils.sizeOf(file);
                    LocalDateTime lastModified = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                    LocalDateTime timeCreated = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
                    myFiles.add(new MyFile(getFullPath(path1.toString()), name, size, lastModified, timeCreated, ext));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return myFiles;
    }


    @Override
    public List<MyFile> searchSubDir(String filepath){
        Path path = Paths.get(getFullPath(filepath));
        List<MyFile> myFiles = new ArrayList<>();
        try{
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            for(Path path1:directoryStream){
                File file = new File(path1.toString());
                if(file.isDirectory()) {
                    myFiles.addAll(searchSubDir(path1.toString()));
                    // todo mora da se poradi na ovom

                }else{
                    BasicFileAttributes attr = Files.readAttributes(path1, BasicFileAttributes.class);
                    String name = file.getName();
                    String ext = FilenameUtils.getExtension(path1.toString());
                    long size = FileUtils.sizeOf(file);
                    LocalDateTime lastModified = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                    LocalDateTime timeCreated = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
                    myFiles.add(new MyFile(getFullPath(path1.toString()), name, size, lastModified, timeCreated, ext));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return myFiles;
    }
    // ext u direktorijumu i svim poddirektorijumima
    @Override
    public List<MyFile> filterByExt(String filepath, String extFilter) {
        Path path = Paths.get(getFullPath(filepath));
        List<MyFile> myFiles = new ArrayList<>();
        try{
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            for(Path path1:directoryStream){
                File file = new File(path1.toString());
                if(file.isFile()) {
                    BasicFileAttributes attr = Files.readAttributes(path1, BasicFileAttributes.class);
                    String name = file.getName();
                    String ext = FilenameUtils.getExtension(path1.toString());
                    long size = FileUtils.sizeOf(file);
                    LocalDateTime lastModified = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                    LocalDateTime timeCreated = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
                    if(ext.equalsIgnoreCase(extFilter))
                        myFiles.add(new MyFile(getFullPath(path1.toString()), name, size, lastModified, timeCreated, ext));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return myFiles;
    }
//substr part of name of file
    @Override
    public List<MyFile> searchSubstring(String substr){
        Path path = Paths.get(getFullPath(rootPath));
        List<MyFile> myFiles = new ArrayList<>();
        try{
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            for(Path path1:directoryStream){
                File file = new File(path1.toString());
                String name = file.getName();
                if(file.isFile() && name.contains(substr)) {
                    BasicFileAttributes attr = Files.readAttributes(path1, BasicFileAttributes.class);
                    String ext = FilenameUtils.getExtension(path1.toString());
                    long size = FileUtils.sizeOf(file);
                    LocalDateTime lastModified = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                    LocalDateTime timeCreated = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
                    myFiles.add(new MyFile(getFullPath(path1.toString()), name, size, lastModified, timeCreated, ext));
                }else{
                    myFiles.addAll(searchSubstring(path1.toString()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return myFiles;
    }

    @Override
    public boolean existName(String filepath, String name) throws MyException{
        File file = new File(getFullPath(filepath));
        if(!file.exists())
            throw new MyException("Nije dobra putanja");
        for(File file1: Objects.requireNonNull(file.listFiles())){
            if(file1.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    @Override
    public List<String> getParentPath(String s) throws MyException {
        return null;
    }

    @Override
    public List<MyFile> filterByPeriod(String s, LocalDateTime localDateTime, LocalDateTime localDateTime1, boolean b) throws MyException{
        return null;
    }

    @Override
    public void saveConfig() throws MyException{

        try(FileWriter writer = new FileWriter(rootPath+File.separator+"config.json")) {
            Gson gson = new Gson();
            gson.toJson(configuration, writer);
            //writer.close();
            //File javaFile = new File("src/main/resources/config.json");

        }catch (Exception e){
            throw new MyException(e.getMessage());
        }

    }

    @Override
    protected String getFullPath(String s){
        if(rootPath==null || s.startsWith(rootPath))
            return s;
        if(s.equalsIgnoreCase(rootPath) || s.equals(""))
            return rootPath;
        if(s.startsWith(File.separator))
            return rootPath+s;
        return rootPath+File.separator+s;
    }

}
