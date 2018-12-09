package com.java.gmall.manage;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageWebApplicationTests {

    @Test
    public void contextLoads() throws IOException, MyException {
        String path = GmallManageWebApplicationTests.class.getClassLoader().getResource("tracker.conf").getPath();

        ClientGlobal.init(path);

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer connection = trackerClient.getConnection();

        StorageClient storageClient = new StorageClient(connection,null);

        String[] jpgs = storageClient.upload_file("C:/Users/zengg/Desktop/IMG_20180416_182052.jpg","jpg",null);

        String url = "http://192.168.99.99";
        for (int i = 0; i < jpgs.length; i++){
            url = url + "/" + jpgs[i];
        }
        System.out.println(url);
    }

}
