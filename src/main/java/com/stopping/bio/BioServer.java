package com.stopping.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO服务
 * @Classname: BioServer
 * @Date: 2022/12/7 11:09 上午
 * @author: stopping
 */
public class BioServer {
    public static final Integer PORT = 9999;

    public static int CLIENT_NUM = 0;

    private static ExecutorService CLIENT_MESSAGE_POOL = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("socket服务启动端口：" + PORT);

        byte [] block = new byte[1024];

        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("新的链接,当前链接数："+ ++CLIENT_NUM);
            CLIENT_MESSAGE_POOL.execute(()->{
                InputStream inputStream = null;
                try {
                    inputStream = socket.getInputStream();
                    int flag =  inputStream.read(block);
                    if (flag != -1){
                        System.out.println(Thread.currentThread().getId()+"接收消息:" + new String(block));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        inputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
