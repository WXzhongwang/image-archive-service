package com.dick.archive.monitor;

import java.io.File;

import javax.annotation.Resource;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;

import com.dick.archive.configuration.ArchiveProperty;

/**
 * @author dick <18668485565@163.com>
 * @version V1.0.0
 * @description ImageArchiveListener
 * @date created on 2021/4/16
 */
public class ImageArchiveListener implements FileAlterationListener {

    @Resource
    private ArchiveProperty archiveProperty;

    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {

    }

    @Override
    public void onDirectoryCreate(File file) {

    }

    @Override
    public void onDirectoryChange(File file) {

    }

    @Override
    public void onDirectoryDelete(File file) {

    }

    @Override
    public void onFileCreate(File file) {
        String absolutePath = file.getAbsolutePath();
        if (absolutePath.endsWith(".jpg")) {
            // 有需要可以考虑可以引入内存队列，降低写入频次

            String fileName =
                absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1, absolutePath.lastIndexOf("."));
            String datePath =
                StringUtils.substringAfter(absolutePath, new File(archiveProperty.getSource()).getAbsolutePath());
            datePath = StringUtils.substringBefore(datePath, fileName + ".jpg");
            datePath = StringUtils.removeEnd(datePath, File.separator);
            datePath = StringUtils.removeStart(datePath, File.separator);
            String[] split = StringUtils.split(datePath, File.separator);

            if (split.length == 3) {
                // 缺少校验
                String dateOfYear = split[0];
                String dateOfMonth = split[1];
                String dateOfDay = split[2];
                String targetPath = archiveProperty.getDestination() + File.separator + dateOfYear + "_" + dateOfMonth
                    + "_" + dateOfDay + "_" + fileName + ".jpg";

                File sourceFile = new File(absolutePath);
                // 新文件
                File targetFile = new File(targetPath);
                // 判断目标文件所在的目录是否存在
                if (!targetFile.getParentFile().exists()) {
                    // 如果目标文件所在的目录不存在，则创建父目录
                    targetFile.getParentFile().mkdirs();
                }
                sourceFile.renameTo(targetFile);
            }
        }
    }

    @Override
    public void onFileChange(File file) {

    }

    @Override
    public void onFileDelete(File file) {

    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {

    }
}
