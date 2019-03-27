package licx.fileshare.Controller;

import licx.fileshare.Domain.FileInfor;
import licx.fileshare.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/")
    public String getInitFiles(Model model){
        List<FileInfor> fileInforList = fileService.getInitFiles();
        model.addAttribute("parent_url", "#");
        model.addAttribute("cur_url", "root");
        model.addAttribute("fileList", fileInforList);
        return "index";
    }

    @GetMapping("/dir/get")
    public String getSomeDirectory(String destination, Model model){
        List<FileInfor> fileInforList = fileService.getDir(destination);
        String parentUrl = fileService.getParentDir(destination);
        model.addAttribute("parent_url", parentUrl);
        model.addAttribute("fileList", fileInforList);
        model.addAttribute("cur_url", destination);
        return "index";
    }


    @GetMapping("/download/file")
    public ResponseEntity<FileSystemResource> downloadFile(String destination) {
        return exportFile(new File(destination));
    }

    private ResponseEntity<FileSystemResource> exportFile(File file) {

        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + file.getName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")) .body(new FileSystemResource(file));
    }


    @PostMapping("/file")
    @ResponseBody
    public HashMap<String, String> singleFileUpload(@RequestParam("file") MultipartFile[] files,
                                   @RequestParam("curUrl") String curUrl) {
        HashMap<String, String> result = new HashMap<>();
        String fileNameList = "";
        if (files.length == 0){
            result.put("back_infor", "请先选择要上传的文件");
            return result;
        }
//        if (file.isEmpty()) {
//            result.put("back_infor", "请先选择要上传的文件");
//            return result;
//        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                result.put("back_infor", "请先选择要上传的文件");
                return result;
            }
            try {
                // Get the file and save it somewhere

                byte[] bytes = file.getBytes();
                Path path = Paths.get(curUrl + file.getOriginalFilename());
                Files.write(path, bytes);

                fileNameList = String.format("%s%s", fileNameList, "','" + file.getOriginalFilename());
//                result.put("back_infor",
//                        "成功上传文件 '" + file.getOriginalFilename() + "'");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        result.put("back_infor",
                "成功上传文件 " + fileNameList + "' ");

        return result;
    }

    @DeleteMapping("/file")
    @ResponseBody
    public HashMap<String, String> deleteFile(@RequestParam("fileUrl") String fileUrl){
        File desFile = new File(fileUrl);
        HashMap<String, String> result = new HashMap<>();

        if (!desFile.exists()){
            result.put("back_message", "文件不存在！");
            return result;
        }

        if (fileService.deleteFolder(fileUrl))
            result.put("back_message", "文件(夹)删除成功。");
        else
            result.put("back_message", "文件(夹)删除失败。");
        return result;
    }

    @PostMapping("/dir")
    @ResponseBody
    public HashMap<String, String> createDirectory(@RequestParam("desUrl") String desUrl,
                                                   @RequestParam("dirName") String dirName){
        File file = new File(desUrl, dirName);
        HashMap<String, String> result = new HashMap<>();
        if (file.exists() && file.isDirectory()){
            result.put("back_message", "文件夹已存在！");
            return result;
        }
        if (file.mkdir())
            result.put("back_message", "文件夹创建成功！");
        else
            result.put("back_message", "文件夹创建失败！");
        return result;
    }
}
