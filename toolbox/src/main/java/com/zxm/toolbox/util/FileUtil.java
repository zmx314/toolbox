package com.zxm.toolbox.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    public static final List<String> EXCEL_SUFFIXES = Arrays.asList(".xls",".xlsx");
    /**
     * 得到文件扩展名
     * @param file 文件
     * @return 扩展名
     */
    public static String getExtension(File file){
        String name = file.getAbsolutePath().toLowerCase();
        return name.substring(name.lastIndexOf("."));
    }

    /**
     * 验证文件是否为无效的文件
     * @param file 文件
     * @return 无效，返回true；有效，则返回false；
     */
    public static boolean invalidFile(File file) {
        if (file == null) {
            System.out.println("文件为null");
            return true;
        }
        if (file.isDirectory()) {
            System.out.println("文件为目录：" + file.getAbsolutePath());
            return true;
        }
        if (!file.exists()) {
            System.out.println("文件不存在：" + file.getAbsolutePath());
            return true;
        }
        return false;
    }

    public static boolean invalidDirectory(File file) {
        if (file == null) {
            System.out.println("文件为null");
            return true;
        }
        if (!file.isDirectory()) {
            System.out.println("file不是目录");
            return false;
        }
        if (!file.exists()) {
            System.out.println("目录不存在");
            return false;
        }
        return true;
    }
    /**
     * 拷贝文件
     *
     * @param srcFile  源文件
     * @param destFile 目的文件
     * @throws IOException 输入输出异常
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        if (invalidFile(srcFile) || invalidFile(destFile))
            return;
        FileChannel inputChannel;
        FileChannel outputChannel;
        FileInputStream in = new FileInputStream(srcFile);
        System.out.println(destFile.getAbsolutePath());
        FileOutputStream out = new FileOutputStream(destFile);
        inputChannel = in.getChannel();
        outputChannel = out.getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        inputChannel.close();
        outputChannel.close();
        in.close();
        out.close();
    }
}
