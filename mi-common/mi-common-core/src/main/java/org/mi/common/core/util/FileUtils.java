package org.mi.common.core.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mi-community
 * @description:
 * @author: Micah
 * @create: 2020-12-06 14:50
 **/
@Slf4j
public class FileUtils {

    /**
     * 系统的临时目录
     */
    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;

    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     */
    public static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * 从类路径读取文件
     * @param path
     * @return
     */
    public static String readFileContent(String path){
        String s = "";
        ClassPathResource classPathResource = new ClassPathResource(path);
        BufferedReader reader = classPathResource.getReader(StandardCharsets.UTF_8);
        StringBuilder content = new StringBuilder();
        try {
            while (StrUtil.isNotBlank((s = reader.readLine()))){
                content.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content.toString();
    }

    /**
     * MultiPartFile转化为File
     *
     * @param multipartFile
     * @return java.io.File
     */
    public static File toFile(MultipartFile multipartFile) {

        // 获取文件名
        String filename = multipartFile.getOriginalFilename();
        // 获取文件的后缀名
        String suffix = "." + FileUtil.getSuffix(filename);
        File file = null;

        try {
            // 创建一个唯一的临时文件

            file = File.createTempFile(IdUtil.simpleUUID(), suffix);
            // 将MultipartFile转化为File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return file;
    }

    /**
     * 获取文件名不带扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileNameWithOutEx(String fileName) {
        if (StrUtil.isNotBlank(fileName)) {
            // 获取点的位置
            int dot = fileName.lastIndexOf(".");
            if (dot > -1 && dot < fileName.length()) {
                return fileName.substring(0, dot);
            }
        }
        return null;
    }

    /**
     * 文件大小转换
     */
    public static String transformSize(Long size) {
        String resultSize;
        if (size / GB > 1) {
            // 转换成单位为GB
            resultSize = DF.format(size / (float) GB) + "GB ";
        } else if (size / MB > 1) {
            // 转换成单位为MB
            resultSize = DF.format(size / (float) MB) + "KB ";
        } else if (size / KB > 1) {
            // 转化为KB
            resultSize = DF.format(size / (float) KB) + "KB ";
        } else {
            // 转化为B
            resultSize = size + "B ";
        }
        return resultSize;
    }

    /**
     * 将InputStream转换成文件
     *
     * @param is
     * @return
     */
    public static File inputStreamToFile(InputStream is, String filePath) throws IOException {
        // 在本地初始化一个文件
        File file = new File(SYS_TEM_DIR + filePath);
        // 判断本地是否存在这个文件
        if (file.exists()) {
            // 存在直接返回该文件
            return file;
        }
        // 不存在则进行转换操作
        OutputStream os = new FileOutputStream(file);
        try {
            int hasRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((hasRead = is.read(buffer, 0, buffer.length)) != -1) {
                os.write(buffer, 0, hasRead);
            }
        } finally {
            is.close();
            os.close();
        }
        return file;

    }

    /**
     * 将文件名解析成文件上传的路径
     */
    public static File getUploadPath(MultipartFile file, String filePath) {
        // 获取文件名，不加后缀
        String nameWithOutEx = FileUtils.getFileNameWithOutEx(file.getOriginalFilename());
        // 获取后缀名
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        // 获取当前的时间，拼接字符串
        String nowStr = "-" + DateUtil.getNowLocalDateTime("yyyyMMddhhmmss");
        try {
            // 拼接文件名
            String fileName = nameWithOutEx + nowStr + "." + suffix;
            // 拼接文件路径
            String path = filePath + fileName;
            // getCanonicalFile 可解析正确各种路径
            File canonicalFile = new File(path).getCanonicalFile();
            // 检测是否存在该目录
            if (!canonicalFile.exists()) {
                // 如果不存在则创建目录
                boolean isMake = canonicalFile.getParentFile().mkdirs();
                if (!isMake) {
                    log.info("创建目录失败");
                }
            }
            // 将file写入canonicalFile
            file.transferTo(canonicalFile);
            return canonicalFile;

        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return null;
    }

    /**
     * 获取文件类型
     * @param type
     * @return
     */
    public static String getFileType(String type){
        String[] documents = new String[]{"txt", "doc", "pdf", "ppt", "pps", "xlsx", "xls", "docx"};
        String[] musics = new String[]{"mp3" ,"wav" ,"wma" ,"mpa" ,"ram" ,"ra" ,"aac" ,"aif" ,"m4a"};
        String[] videos = new String[]{"avi", "mpg", "mpe", "mpeg", "asf", "wmv", "mov", "qt", "rm", "mp4", "flv", "m4v", "webm", "ogv", "ogg"};
        String[] images = new String[]{"bmp", "dib", "pcp", "dif", "wmf", "gif", "jpg", "tif", "eps", "psd", "cdr", "iff", "tga", "pcd", "mpt" ,"png","jpeg"};
        if (Arrays.asList(images).contains(type)) {
            return "图片";
        } else if (Arrays.asList(documents).contains(type)) {
            return "文档";
        } else if (Arrays.asList(musics).contains(type)) {
            return "音乐";
        } else if (Arrays.asList(videos).contains(type)) {
            return "视频";
        } else {
            return "其他";
        }
    }

    /**
     * 检查文件是否超过指定的大小,没超过为true，超过为false
     */
    public static Boolean checkSize(long maxSize, long size){
        // 1M
        int length = 1024 * 1024;
        return size <= (maxSize * length);
    }

    /**
     * 生成文件的路径
     * @param multipartFile
     * @return
     */
    public static String createFilePath(MultipartFile multipartFile,Long id) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd")  + "/" +
                id + "/" + IdUtil.simpleUUID() + "." +
                FileUtil.getSuffix(FileUtils.toFile(multipartFile));
    }

    /**
     * 生成文件的路径
     * @param suffix 文件后缀名
     * @return
     */
    public static String createFilePath(String suffix,Long id) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd")  + "/" +
                id + "/" + IdUtil.simpleUUID() + "." +
                suffix;
    }

    /**
     * 校验文件的类型是否正确
     * @param originalFilename
     * @return
     */
    public static Boolean checkFileType(String originalFilename) {
        for (String type : IMAGE_TYPE) {
            if (StrUtil.endWithIgnoreCase(originalFilename,type)){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查两个文件是否是一样的
     *
     */
    public static Boolean checkIsSame(File file1,File file2){
        String m1 = FileUtils.getMD5(file1);
        String m2 = FileUtils.getMD5(file1);
        return StrUtil.equals(m1,m2);
    }

    /**
     * 获取文件的MD5值
     * @param file
     * @return
     */
    public static String getMD5(File file){
        return getMD5(FileUtils.getByte(file));
    }

    /**
     * 获取文件的长度
     * @param file
     * @return
     */
    private static byte[] getByte(File file) {
        // 得到文件长度
        byte[] b = new byte[(int) file.length()];
        try {
            InputStream in = new FileInputStream(file);
            try {
                System.out.println(in.read(b));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return b;
    }

    /**
     * 获取文件的MD5值
     * @param bytes
     * @return
     */
    public static String getMD5(byte[] bytes){
        // 16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(bytes);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            // 移位 输出字符串
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 下载文件
     * @param request 请求对象
     * @param response 响应对象
     * @param file 本地缓存的需要下载的文件
     * @param deleteOnExit 下载完成后是否删除本地文件
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, File file, boolean deleteOnExit){
        // 设置响应的编码集
        response.setCharacterEncoding(request.getCharacterEncoding());
        // 设置响应的ContentType
        response.setContentType("application/octet-stream");
        InputStream is = null;
        try {
            // 获取本地文件的输入流
            is = new FileInputStream(file);
            // 设置响应头信息
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            // 将输入流写到response对象的输出流中
            IoUtil.copy(is,response.getOutputStream());
            // 刷新输出流
            response.flushBuffer();
        }catch (IOException e){
            log.info(e.getMessage(), e);
        }finally {
            if (is != null) {
                try {
                    is.close();
                    if (deleteOnExit) {
                        // 删除本地存在的文件
                        file.deleteOnExit();
                    }
                } catch (IOException e) {
                    log.info(e.getMessage(), e);
                }
            }
        }
    }

}
