package com.stopping.nio.channel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Classname: ChannelWriteFileDemo
 * @Description: 通过Channel读取文件
 * @Date: 2022/12/8 9:58 上午
 * @author: stopping
 */
public class ChannelWriteFileDemo {
    public static void main(String[] args) throws Exception {
        //writeFileByChannel("写入数据哈哈哈哈，what your name");
        copyFileByChannel();
    }

    /**
     * 通过Channel读取文件
     * @throws IOException
     */
    public static void readFileByChannel() throws IOException{
        //读取源文件
        File file = new File("/Users/stopping/stopping/netty-demo/src/1.txt");
        //转输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        //获取输入流的channel
        FileChannel fileChannel = fileInputStream.getChannel();
        //缓存
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将channel读取的数据写入缓存
        fileChannel.read(byteBuffer);
        System.out.println("读取文件内容:" + new String(byteBuffer.array()));
        fileInputStream.close();
    }

    /**
     * 通过获取的内容写入文件
     * 字节流读取文件 -> buffer -> channel -> outputStream
     * ps:channel和Buffer配套使用
     * @param content
     * @throws Exception
     */
    public static void writeFileByChannel(String content){
        //读取源文件
        File file = new File("/Users/stopping/stopping/netty-demo/src/2.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        try(
                //转输入流
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream  byteArrayInputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        ){
            while (true){
                byteBuffer.clear();
                int read = byteArrayInputStream.read(byteBuffer.array());
                if (read == -1){
                    break;
                }
                fileOutputStream.getChannel().write(byteBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("读取流数据结束");
    }


    /**
     * 复制文件通过channel
     */
    public static void copyFileByChannel(){
        //读取源文件
        File file = new File("/Users/stopping/stopping/netty-demo/src/1.txt");
        File copyFile = new File("/Users/stopping/stopping/netty-demo/src/1copy.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        try(
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream copyFileOutputStream = new FileOutputStream(copyFile);
        ){
            FileChannel channel = fileInputStream.getChannel();
            FileChannel copyChannel = copyFileOutputStream.getChannel();
            while (true){
                byteBuffer.clear();
                int read = channel.read(byteBuffer);
                if (read == -1){
                    System.out.println("文件读取结束");
                    break;
                }
                byteBuffer.flip();
                copyChannel.write(byteBuffer);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
