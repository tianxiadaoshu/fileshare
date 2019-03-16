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
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.Date;
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

}
