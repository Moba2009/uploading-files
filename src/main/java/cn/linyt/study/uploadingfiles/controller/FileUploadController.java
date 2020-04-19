package cn.linyt.study.uploadingfiles.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


/**
 * @ClassName FileUploadController
 * @Description TODO
 * @Author Mojo
 * @Date 2020/4/20 2:04
 * @Version 1.0
 **/
@Controller
public class FileUploadController {

    @PostMapping("/fileUpload")
    public String fileUpload(/*@RequestParam(value = "file")*/ MultipartFile file, Model model, HttpServletRequest request) {

        if (file.isEmpty()) {
            System.out.println("文件为空");
            model.addAttribute("whether", false);
            model.addAttribute("message", "未选择任何文件");
            return "uploadForm";
        }
        String fileName = file.getOriginalFilename();           // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = "C:\\Program Files\\nginx-1.16.1\\html\\upload\\img\\"; // 文件保存到服务器的路径
        fileName = UUID.randomUUID() + suffixName;              // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);    //写入磁盘
        } catch (IOException e) {
            e.printStackTrace();
        }
        String originalFileName = "http://111.230.223.21/upload/img/" + fileName;   //访问文件的路径
        model.addAttribute("originalFileName", originalFileName);
        model.addAttribute("whether", true);
        model.addAttribute("message", "文件上传成功");
        return "uploadForm";
    }

    @GetMapping("/uploadForm")
    public String uploadForm() {
        return "uploadForm";
    }
}
