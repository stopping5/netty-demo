package com.stopping.chatRoot;

import java.io.InputStream;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        ChatRoomClient chatRoomClient = new ChatRoomClient();
        InputStream in = System.in;
        while (true){
            Scanner scanner = new Scanner(in);
            String msg = scanner.nextLine();
            System.out.println("客户端发送:"+msg);
            chatRoomClient.send(msg);
        }
    }
}
