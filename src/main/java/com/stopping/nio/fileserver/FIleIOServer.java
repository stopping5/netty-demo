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

    /**
     * 服务端接收文件
     * 客户端：
     *  ------------------  ------------------     ------------------
     *   读取文件输入流      &     数据输出流       &    socket outputStream
     *  ------------------  ------------------     ------------------
     *  服务端：
     *  ------------------  ------------------  ------------------
     *   socket inputStream &    数据输入流程    &    读取文件输出流
     *  ------------------  ------------------  ------------------
     * @throws IOException
     */

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
