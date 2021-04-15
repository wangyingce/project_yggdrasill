package cc.leevi.webbase.controller;

import cc.leevi.webbase.utils.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;

public class EntityController {
    /**
     * excel - xlsx前缀
     */
    protected static final String EXCEL_SUFFIX = ".xlsx";
    /**
     * word - docx前缀
     */
    protected static final String WORD_SUFFIX = ".docx";

    /**
     * 图片 - png前缀
     */
    protected static final String PNG_SUFFIX = ".png";

    /**
     * 默认文件名
     */
    protected static final String DEFAULT_FILENAME = "downloadfile";

    protected ResponseEntity wordEntity(InputStream is){
        return octetEntity(is,DEFAULT_FILENAME+WORD_SUFFIX);
    }

    protected ResponseEntity wordEntity(InputStream is,String filename){
        return octetEntity(is,filename+WORD_SUFFIX);
    }

    protected ResponseEntity wordEntity(byte[] bytes){
        return octetEntity(bytes,DEFAULT_FILENAME+WORD_SUFFIX);
    }

    protected ResponseEntity wordEntity(byte[] bytes,String filename){
        return octetEntity(bytes,filename+WORD_SUFFIX);
    }

    /**
     * 输出excel文件
     * @param is 文件流
     * @return
     */
    protected ResponseEntity excelEntity(InputStream is){
        return octetEntity(is,DEFAULT_FILENAME+EXCEL_SUFFIX);
    }


    /**
     * 输出excel文件
     * @param is 文件流
     * @param filename 文件名
     * @return
     */
    protected ResponseEntity excelEntity(InputStream is,String filename){
        return octetEntity(is,filename+EXCEL_SUFFIX);
    }
    /**
     * 输出excel文件
     * @param bytes 文件字节数组
     * @return
     */
    protected ResponseEntity excelEntity(byte[] bytes){
        return octetEntity(bytes,DEFAULT_FILENAME+EXCEL_SUFFIX);
    }

    /**
     * 输出excel文件
     * @param bytes 文件字节数组
     * @param filename 文件名
     * @return
     */
    protected ResponseEntity excelEntity(byte[] bytes,String filename){
        return octetEntity(bytes,filename+EXCEL_SUFFIX);
    }

    /**
     * 输出其他格式文件
     * @param is
     * @param filename
     * @return
     */
    protected ResponseEntity octetEntity(InputStream is, String filename){
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,buildFilename(filename))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(is,filename));
    }

    /**
     * 输出其他格式文件
     * @param bytes
     * @param filename
     * @return
     */
    protected ResponseEntity octetEntity(byte[] bytes,String filename){
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,buildFilename(filename))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    /**
     * 解决各浏览器下载文件名乱码问题
     * @param filename
     * @return
     */
    private String buildFilename(String filename){
        String headerValue = "attachment;";
        headerValue += " filename=\"" + StringUtils.urlEncode(filename) +"\";";
        headerValue += " filename*=utf-8''" + StringUtils.urlEncode((filename));
        return headerValue;
    }
}
