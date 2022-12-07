package com.stopping.nio.buffer;

import java.nio.IntBuffer;

/**
 * 缓冲demo
 */
public class BufferDemo {
    public static void main(String[] args) {
        intBufferDemo();
    }

    public static void intBufferDemo(){
        //创建一个容量为5的intBuffer
        IntBuffer intBuffer = IntBuffer.allocate(5);
        intBuffer.put(1);
        intBuffer.put(2);
        intBuffer.put(3);
        intBuffer.put(4);
        intBuffer.put(5);
        //转化读写模式 - 读取模式
        intBuffer.flip();
        //判断是否到达数组尾
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
