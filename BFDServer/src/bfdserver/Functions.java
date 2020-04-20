/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfdserver;

import java.util.Random;

/**
 *
 * @author User
 */
class Functions {
    static Random random =new Random();
    
    public static void putLong(byte[] data, int index, long value) {
        if(data.length < index+4)return;
        data[index] = (byte)(value >> 24 & 0xff);
        data[index + 1] = (byte)(value >> 16 & 0xff);
        data[index + 2] = (byte)(value >> 8 & 0xff);
        data[index + 3] = (byte)(value & 0xff);
        return;
    }

    public static long getLong(byte[] data, int index) {
        long result = 0;
        
        result = (data[index] & 0xff) << 24;
        result = (data[index + 1] & 0xff) << 16;
        result = (data[index + 2] & 0xff) << 8;
        result = (data[index + 3] & 0xff);
        
        return result;
    }
    public static void putInt(byte[] data, int index, int value) {
        if (data.length < index + 2) {
            return;
        }
        data[index] = (byte) (value >> 8 & 0xff);
        data[index + 1] = (byte) (value & 0xff);
        return;
    }

    public static int byteArrayToint(byte[] array, int startIndex) {
        return ((array[startIndex] & 0xff) << 8) | (array[startIndex + 1] & 0xff);
    }
    
}
