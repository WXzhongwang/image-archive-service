package com.dick.archive;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dick.archive.configuration.ArchiveProperty;

/**
 * @author dick <18668485565@163.com>
 * @version V1.0.0
 * @description 文件一次性导入 优先于ArchiveStartUpRunner执行
 * @date created on 2021/4/16
 */
@Component
@Order(1)
public class ArchiveTransferRunner implements ApplicationRunner {

    @Resource
    private ArchiveProperty archiveProperty;

    @Override
    public void run(ApplicationArguments args) {
        String baseSourceFolder = archiveProperty.getSource();
        String targetFolder = archiveProperty.getDestination();
        File base = new File(baseSourceFolder);
        File target = new File(targetFolder);
        if (!base.isDirectory() || !target.isDirectory()) {
            throw new IllegalArgumentException("archive configuration is not correct, please check");
        }
        // 文件数可以进行初始估计
        Map<String, String> fileNames = new HashMap<>(16);
        listFiles(fileNames, new CustomFileNameFilter(), base, baseSourceFolder, targetFolder, 0);

        if (!CollectionUtils.isEmpty(fileNames)) {
            for (Map.Entry<String, String> entry : fileNames.entrySet()) {
                copy(entry.getKey(), entry.getValue());
            }
        }
    }

    private void listFiles(Map<String, String> fileNames, FilenameFilter filter, File baseFolder, String prefix,
        String targetFolder, int level) {
        if (level <= 3) {
            File[] list = baseFolder.listFiles(filter);
            if (list != null && list.length > 0) {
                for (File item : list) {
                    if (item.isDirectory()) {
                        level++;
                        listFiles(fileNames, filter, item, prefix, targetFolder, level);
                    } else {
                        verify(targetFolder, prefix, item.getAbsolutePath(), fileNames);
                    }
                }
            }
        }
    }

    private void verify(String targetFolder, String prefix, String absolutePath, Map<String, String> fileNames) {
        String fileName =
            absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1, absolutePath.lastIndexOf("."));
        String datePath = StringUtils.substringAfter(absolutePath, new File(prefix).getAbsolutePath());
        datePath = StringUtils.substringBefore(datePath, fileName + ".jpg");
        datePath = StringUtils.removeEnd(datePath, File.separator);
        datePath = StringUtils.removeStart(datePath, File.separator);
        String[] split = StringUtils.split(datePath, File.separator);
        if (split.length == 3) {
            // 缺少校验
            String dateOfYear = split[0];
            String dateOfMonth = split[1];
            String dateOfDay = split[2];
            String newFormat = targetFolder + File.separator + dateOfYear + "_" + dateOfMonth + "_" + dateOfDay + "_"
                + fileName + ".jpg";
            fileNames.put(absolutePath, newFormat);
        }
    }

    private void copy(String source, String target) {
        File sourceFile = new File(source);
        // 新文件
        File targetFile = new File(target);
        // 判断目标文件所在的目录是否存在
        if (!targetFile.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建父目录
            targetFile.getParentFile().mkdirs();
        }
        sourceFile.renameTo(targetFile);
    }

    public static class CustomFileNameFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".jpg") || new File(dir + File.separator + name).isDirectory();
        }
    }
}
