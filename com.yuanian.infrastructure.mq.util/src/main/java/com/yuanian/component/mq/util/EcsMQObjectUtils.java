package com.yuanian.component.mq.util;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liujy
 * @date 2020/6/4 10:58
 **/
public class EcsMQObjectUtils {

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>(16);
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if("payload".equals(fieldName)){
                break;
            }
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * 对象转字节数组
     */
    public static byte[] objectToBytes(Object obj) throws IOException {
        try(
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream sOut = new ObjectOutputStream(out)
        ){
            sOut.writeObject(obj);
            sOut.flush();
            byte[] bytes = out.toByteArray();
            return bytes;
        }
    }

    /**
     * 字节数组转对象
     */
    public static Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        try(
                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                ObjectInputStream sIn = new ObjectInputStream(in)
        ){
            return sIn.readObject();
        }
    }

}
