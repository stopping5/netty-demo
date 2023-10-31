package com.stopping.nio.fileserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

/**
 * 文件IO客户端
 * @Classname: FileIOClient
 * @Date: 2022/12/12 11:11 上午
 * @author: stopping
 */
public class FileIOClient {
    public static void main(String[] args) throws IOException {
        FileIOClient fileIOClient =new FileIOClient();
    }

    private Socket socket;

    /**
     * 文件客户端
     *  ------------------  ------------------     ------------------
     *   读取文件输入流      &     数据输出流       &    socket outputStream
     *  ------------------  ------------------     ------------------
     * @throws IOException
     */
    public FileIOClient() throws IOException {
        socket = new Socket("localhost",7001);
        FileInputStream fileInputStream = new FileInputStream(new File("test.mp4"));
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[4096];
        long readCount = 0;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while ((readCount = fileInputStream.read(buffer)) >= 0 ){
            total += readCount;
            dataOutputStream.write(buffer);
        }
        System.out.println("数据传输时间:"+(System.currentTimeMillis() - startTime)+",数据大小:"+total);
        dataOutputStream.close();
        fileInputStream.close();
        socket.close();
    }
}
