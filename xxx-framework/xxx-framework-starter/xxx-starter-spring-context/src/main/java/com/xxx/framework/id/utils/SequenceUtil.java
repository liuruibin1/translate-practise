//package com.xxx.framework.mybatis.utils;
//
//import com.baomidou.mybatisplus.core.toolkit.Sequence;
//import org.springframework.stereotype.Component;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//@Component
//public class SequenceUtil {
//
//    private final Sequence sequence;
//
//    public SequenceUtil() {
//        try {
//            this.sequence = new Sequence(InetAddress.getLocalHost());
//        } catch (UnknownHostException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public long nextId() {
//        return this.sequence.nextId();
//    }
//
//}