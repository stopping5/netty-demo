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
     * 理解channel和stream的区别
     * 1. channel
     * - 本身并能直接访问数据，如名它只是一个管道用于运输数据，而需要搭配Buffer使用，Buffer是装载数据的载体
     * - channel可以双向传输数据（写入、读出）
     * - 支持异步
     */
    public static void copyFileByChannel(){
        //读取源文件
        File file = new File("/Users/stopping/stopping/netty-demo/src/1.txt");
        File copyFile = new File("/Users/stopping/stopping/netty-demo/src/1copy.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        try(
            //链接输入文件的输入流和输出流
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream copyFileOutputStream = new FileOutputStream(copyFile);
        ){
            //输入通道和输出通道 channelAPI既有读取也有写入
            FileChannel channel = fileInputStream.getChannel();
            FileChannel copyChannel = copyFileOutputStream.getChannel();
            while (true){
                //清空缓存区
                byteBuffer.clear();
                //将源文件channel数据写入buffer
                int read = channel.read(byteBuffer);
                if (read == -1){
                    System.out.println("文件读取结束");
                    break;
                }
                //切换读写模式
                byteBuffer.flip();
                //读出buffer数据到复制channel
                copyChannel.write(byteBuffer);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
