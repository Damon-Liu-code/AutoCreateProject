package com.controller;

import com.util.FileUtil;
import com.util.YamlUtil;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

@RestController
public class OutputController {
    private static final Logger log = Logger.getLogger(OutputController.class);

    @RequestMapping("/getFileList")
    public ResponseEntity<List<FileItem>> getFileList(HttpServletRequest servletRequest) {
        String path = YamlUtil.getString("output.file_path");
        List<FileItem> fileList = new ArrayList<>();
        try {
            //读取Session
            HttpSession session = servletRequest.getSession();
            String entityName = session.getAttribute("entityName").toString();
            Resource resource = new FileSystemResource(path + "/" + entityName);
            String absolutePath = resource.getFile().getAbsolutePath();
            java.io.File directory = new java.io.File(absolutePath);
            if (directory.isDirectory()) {
                java.io.File[] files = directory.listFiles();
                if (files != null) {
                    for (java.io.File file : files) {
                        if (file.isFile()) {
                            fileList.add(new FileItem(file.getName()));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常情况
        }

        return ResponseEntity.ok(fileList);
    }

    @RequestMapping("/getFileContent/{fileName:.+}")
    public ResponseEntity<String> getFileContent(@PathVariable String fileName,
                                                 HttpServletRequest servletRequest) {
        StringBuilder contentBuilder = new StringBuilder();
        String path = YamlUtil.getString("output.file_path");
        try {
            //读取Session
            HttpSession session = servletRequest.getSession();
            String entityName = session.getAttribute("entityName").toString();
            Resource resource = new FileSystemResource(path + "/" + entityName + "/" + fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常情况
        }

        return ResponseEntity.ok(contentBuilder.toString());
    }

    @RequestMapping("/downloadZip")
    public void downloadFolder(HttpServletRequest servletRequest, HttpServletResponse response) {
        //读取Session
        HttpSession session = servletRequest.getSession();
        String entityName = session.getAttribute("entityName").toString();
        // 指定要压缩的文件夹路径
        String path = YamlUtil.getString("output.file_path");
        String folderPath = path + "/" + entityName;

        try {
            // 创建一个临时文件，用于保存压缩后的文件
            File tempFile = File.createTempFile("temp", ".zip");

            // 创建 ZipOutputStream 对象，指定输出流为临时文件
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(tempFile));

            // 递归压缩文件夹
            FileUtil.compressFolder(new File(folderPath), zipOut, "");

            zipOut.close();

            // 设置响应头信息
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + entityName + ".zip\"");

            // 将压缩后的文件写入响应流
            FileInputStream fileIn = new FileInputStream(tempFile);
            ServletOutputStream out = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            fileIn.close();
            out.flush();
            out.close();

            // 删除临时文件
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常情况
        }
    }


    // 内部类，用于表示文件项
    private static class FileItem {
        private String name;

        public FileItem(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
