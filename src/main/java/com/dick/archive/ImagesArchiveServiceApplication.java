package com.dick.archive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dick.archive.monitor.ImageArchiveListener;

/**
 * 图片文件归档服务
 * 
 * @author shengwangzhong
 */
@SpringBootApplication
public class ImagesArchiveServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImagesArchiveServiceApplication.class, args);
    }

    @Bean
    public ImageArchiveListener archiveListener() {
        return new ImageArchiveListener();
    }

}
