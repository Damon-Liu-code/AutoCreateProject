package com.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 *
 * @author Damon.Liu
 */

public class FileUtil {
    private static final Logger log = Logger.getLogger(FileUtil.class);

    public static void FileWrite(String fileContent, String filePath, String fileName) {
        try {
            File directory = new File(filePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(filePath + "/" + fileName)), StandardCharsets.UTF_8));
            writer.write(fileContent);
            writer.close();
            System.out.println(fileName + "generated successfully!");
        } catch (IOException e) {
            log.error(e);
            System.out.println("Error generating " + fileName + ": " + e.getMessage());
        }
    }

    public static void compressFolder(File folder, ZipOutputStream zipOut, String parentFolder) throws IOException {
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                compressFolder(file, zipOut, parentFolder + folder.getName() + "/");
            }
        } else {
            byte[] buffer = new byte[4096];
            FileInputStream fileIn = new FileInputStream(folder);
            zipOut.putNextEntry(new ZipEntry(parentFolder + folder.getName()));
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                zipOut.write(buffer, 0, bytesRead);
            }
            fileIn.close();
            zipOut.closeEntry();
        }
    }

}
