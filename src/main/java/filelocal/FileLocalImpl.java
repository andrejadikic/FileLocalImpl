package filelocal;


import Data.MyFile;
import lombok.Getter;
import paket.Configuration;
import paket.FileManager;
import paket.Metadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class FileLocalImpl extends FileManager {

    @Override
    public boolean createRoot(String path, String name, Configuration configuration) throws IOException {
        if(mkdir(path,name)){
            setRootPath(path+File.separator+name);
            setConfiguration(configuration);
            saveConfig(getRootPath());
            return true;
        }else{
            System.out.println("Error found");
            return false;
        }
    }

    @Override
    protected boolean checkConfig(String parentPath, String ext, long size, int n_number) {
        //TODO dodati da se prosledjuje i name od file i sta ce nam nnumber
        String name = "bhcdsbh";
        if(getRootPath()==null)
            return true;
        try {
            Path rootPath = Paths.get(getRootPath());
            Path path = Paths.get(parentPath);
            List<String> names = new ArrayList<>();
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            for(Path path1:directoryStream){
                names.add(path1.toString());
            }
            return !getConfiguration().getExcludedExt().contains(ext) &&
                    !names.contains(name) &&
                    getConfiguration().getSize() >= Files.size(rootPath) + size &&
                    getConfiguration().getFile_n() >= names.size() + n_number;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



    @Override
    public boolean mkdir(String path, String name) {
        String separator = System.getProperty("file.separator");
        File dir = (getRootPath()!=null? new File(getRootPath()+path+separator+name):
                new File(path+separator+name));
        String ext="";
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf != -1) {
            ext = name.substring(lastIndexOf);
        }
        if(dir.getParentFile().exists() && checkConfig(dir.getParentFile().getAbsolutePath(),ext,dir.length(),1)){
            dir.mkdir();
        }
//        if(!dir.getParentFile().exists()){
//            dir.mkdirs();
        return dir.exists();
    }

    @Override
    public boolean delete(String path) {
        return new File(getRootPath()+File.separator+path).delete();
    }
    //TODO da se doda i ime file koje se premesta ili je oldpath sa imenom a newpath samo destinacija a da sami dodamo ime
    @Override
    public boolean move(String oldPath, String newPath) {
        try {
            Files.move(Paths.get(getRootPath() + File.separator + oldPath),
                    Paths.get(getRootPath() + File.separator + newPath));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rename(String path, String name) {
        String newPath = Paths.get(getRootPath() + File.separator + path).getParent().toString()
                + File.separator + name;
        try {
            Files.move(Paths.get(getRootPath() + File.separator + path),
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
    public List<MyFile> searchDir(String s) {
        return null;
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
    public List<MyFile> filterByPeriod(String s, Date date, Date date1, boolean b) {
        return null;
    }

    @Override
    public void saveConfig(String path) {

    }

}
