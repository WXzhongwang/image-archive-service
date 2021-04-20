package com.dick.archive.monitor;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * @author dick <18668485565@163.com>
 * @version V1.0.0
 * @description 监听器
 * @date created on 2021/4/16
 */
public class ArchiveMonitor {

    private FileAlterationMonitor monitor;

    public ArchiveMonitor(long interval) {
        monitor = new FileAlterationMonitor(interval);
    }

    public void monitor(String path, FileAlterationListener listener) {
        // 观察者
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);
        observer.addListener(listener);
    }

    /**
     * 停止监听器
     */
    public void stop() {
        try {
            monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动监听器
     */
    public void start() {
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
