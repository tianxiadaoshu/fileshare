package licx.fileshare.Service;

import licx.fileshare.Domain.FileInfor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {

    public List<FileInfor> getInitFiles(){
        File[] fileList = File.listRoots();
        if (fileList == null || fileList.length == 0) {
            return null;
        }
        return fileListToFileInforList(fileList);
    }

    public String getParentDir(String destination){
        File file = new File(destination);
        return file.getParent();
    }

    public List<FileInfor> getDir(String destination){
        File parentFile = new File(destination);
        File[] children = parentFile.listFiles();
        if (children == null || children.length == 0) {
            return null;
        }
        return fileListToFileInforList(children);
    }

    private List<FileInfor> fileListToFileInforList(File[] files){
        List<FileInfor> fileInforList = new ArrayList<>();
        for (File file : files) {
            String a_path = file.getAbsolutePath();
            String path = a_path.replace("\\", "/");
            fileInforList.add(new FileInfor(file.getName(), file.isDirectory(), path));
        }
        return fileInforList;
    }

//    private ArrayList<File> getDiskInformation() {
//
//        File[] disks = File.listRoots();
//        if (disks.length == 0) {
//            return null;
//        }
//
//        return new ArrayList<>(Arrays.asList(disks));
//    }
}
