package com.dick.archive;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.dick.archive.configuration.ArchiveProperty;
import com.dick.archive.monitor.ArchiveMonitor;
import com.dick.archive.monitor.ImageArchiveListener;

/**
 * @author dick <18668485565@163.com>
 * @version V1.0.0
 * @description ArchiveStartUpRunner
 * @date created on 2021/4/16
 */
@Component
@Order(2)
public class ArchiveStartUpRunner implements ApplicationRunner {

    @Resource
    private ArchiveProperty archiveProperty;

    @Autowired
    private ImageArchiveListener archiveListener;

    @Override
    public void run(ApplicationArguments args) {
        ArchiveMonitor monitor = new ArchiveMonitor(archiveProperty.getInterval());
        monitor.monitor(archiveProperty.getSource(), archiveListener);
        monitor.start();
    }
}
