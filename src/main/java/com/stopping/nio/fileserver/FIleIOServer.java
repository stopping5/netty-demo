package com.stopping.nio.fileserver;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @Classname: FIleIOServer
 * @Description: 文件IO读取服务器
 * @Date: 2022/12/12 11:00 上午
 * @author: stopping
 */
public class FIleIOServer {

    public static void main(String[] args) throws IOException {
        FIleIOServer fIleIOServer = new FIleIOServer();
    }

    private ServerSocket socket;

    public FIleIOServer() throws IOException {
        socket = new ServerSocket(7001);
        start();
    }

    public void start() throws IOException {
        while (true){
            Socket clientSocket = socket.accept();
            FileOutputStream fileOutputStream = new FileOutputStream(new File("copy.mp4"));
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            byte [] b = new byte[4096];
            while (true){
               int read = dataInputStream.read(b);
               fileOutputStream.write(b);
               if (read == -1){
                   break;
               }
            }
        }
    }

}
