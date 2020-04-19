package cn.linyt.study.uploadingfiles.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * @ClassName FileUploadController
 * @Description TODO
 * @Author Mojo
 * @Date 2020/4/20 2:04
 * @Version 1.0
 **/
@Slf4j
@Controller
public class FileUploadController {

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpg", "image/jpeg", "image/png");

    @PostMapping("/fileUpload")
    public String fileUpload(@RequestParam(value = "file") MultipartFile file, Model model, HttpServletRequest request) {

        // 是否接收到文件
        if (file.isEmpty()) {
            log.info("### file is null ###");
            model.addAttribute("message", "未选择任何文件");
            return "uploadForm";
        }
        // 文件名
        String fileName = file.getOriginalFilename();
        // 后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 校验文件的类型
        String contentType = file.getContentType();
        log.info("### upload file type: {} ###", contentType);
        if (!CONTENT_TYPES.contains(contentType)){
            // 文件类型不合法，直接返回null
            log.info("### Type not supported：{} ###", suffixName);
            model.addAttribute("message", "文件类型不合法");
            return "uploadForm";
        }
        // 校验文件的内容（校验是否是图片类型）
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file.getInputStream());
            if (null == bufferedImage){
                log.info("### Content not supported：{} ###", suffixName);
                model.addAttribute("message", "文件内容不合法");
                return "uploadForm";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 文件保存到服务器的路径
        String filePath = "C:\\Program Files\\nginx-1.16.1\\html\\upload\\img\\";
        // 新文件名
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            // 写入磁盘
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 访问文件的路径
        String originalFileName = "http://111.230.223.21/upload/img/" + fileName;
        model.addAttribute("originalFileName", originalFileName);
        model.addAttribute("message", "文件上传成功");
        return "uploadForm";
    }

    @GetMapping("/uploadForm")
    public String uploadForm() {
        return "uploadForm";
    }
}
