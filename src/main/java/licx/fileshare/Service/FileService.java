package licx.fileshare.Service;

import licx.fileshare.Domain.FileInfor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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
        if (file.getParent() == null)
            return "$";
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
            String name = file.getName();
            if (file.getName().equals(""))
                name = path;
            fileInforList.add(new FileInfor(name, file.isDirectory(), path));
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


    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public boolean deleteFolder(String sPath) {
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return false;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }


    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private boolean deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    private boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        if (files == null){
            return dirFile.delete();
        }
        for (File file : files) {
            //删除子文件
            if (file.isFile()) {
                flag = deleteFile(file.getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        return dirFile.delete();
    }
}
