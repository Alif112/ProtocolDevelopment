/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gtpclient;

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
    public static void putInt4(byte[] data, int index, int value) {
        if(data.length < index+4)return;
        data[index] = (byte)(value >> 24 & 0xff);
        data[index + 1] = (byte)(value >> 16 & 0xff);
        data[index + 2] = (byte)(value >> 8 & 0xff);
        data[index + 3] = (byte)(value & 0xff);
        return;
    }
    public static int getInt4(byte[] data, int index) {
    	int result = 0;
	if(data == null || index+4 > data.length)return result;
	result = data[index] & 0xff;
	result = (result << 8) | (data[index + 1] & 0xff);
	result = (result << 8) | (data[index + 2] & 0xff);
	result = (result << 8) | (data[index + 3] & 0xff);
	return result;
    }
    
    
    
    public static void putLong8(byte[] data, int index, long value) {
        if(data.length < index+8)return;
        data[index] = (byte)(value >> 56 & 0xff);
        data[index + 1] = (byte)(value >> 48 & 0xff);
        data[index + 2] = (byte)(value >> 40 & 0xff);
        data[index + 3] = (byte)(value >> 32 & 0xff);
        data[index + 4] = (byte)(value >> 24 & 0xff);
        data[index + 5] = (byte)(value >> 16 & 0xff);
        data[index + 6] = (byte)(value >> 8 & 0xff);
        data[index + 7] = (byte)(value & 0xff);
        return;
    }
    
    public static long getLong8(byte[] data, int index) {
    	long result = 0;
	if(data == null || index+8 > data.length)return result;
	result = data[index] & 0xff;
	result = (result << 8) | (data[index + 1] & 0xff);
	result = (result << 8) | (data[index + 2] & 0xff);
	result = (result << 8) | (data[index + 3] & 0xff);
	result = (result << 8) | (data[index + 4] & 0xff);
	result = (result << 8) | (data[index + 5] & 0xff);
	result = (result << 8) | (data[index + 6] & 0xff);
        result = (result << 8) | (data[index + 7] & 0xff);
        return result;
    }
    
    

    public static long getLong(byte[] data, int index) {
    	long result = 0;
		if(data == null || index+4 > data.length)return result;
		result = data[index] & 0xff;
		result = (result << 8) | (data[index + 1] & 0xff);
		result = (result << 8) | (data[index + 2] & 0xff);
		result = (result << 8) | (data[index + 3] & 0xff);
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
    
    public static void putShort2(byte[] data, int index, short value) {
        if (data.length < index + 2) return;
        data[index] = (byte) (value >> 8 & 0xff);
        data[index + 1] = (byte) (value & 0xff);
        return;
    }
    public static short getShort2(byte[] array, int startIndex) {
        return (short) (((array[startIndex] & 0xff) << 8) | (array[startIndex + 1] & 0xff));
    }
    
    public static int getInt(byte[] array, int startIndex) {
        return ((array[startIndex] & 0xff) << 8) | (array[startIndex + 1] & 0xff);
    }
    
    public static int byteArrayToint(byte[] array, int startIndex) {
        return ((array[startIndex] & 0xff) << 8) | (array[startIndex + 1] & 0xff);
    }
    
}
