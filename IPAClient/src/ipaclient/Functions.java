/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipaclient;

import java.io.IOException;
import java.io.InputStream;
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
    
    static int reader;
    static void ignoreByte(InputStream is, int index) throws IOException {
       for(int j=0;j<index;j++)
           reader=is.read();
    }
    public static int makeSameLength(byte data[], int offset,int sizeToAdd){
        int index=offset;
        for(int i=0;i<sizeToAdd;i++)
            data[index++]=(byte) Utility.random.nextInt(255);
        return index;
    }
    
    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    
    public static int getUserName(byte[] data,int offset){
        int randomSeq=0;
        int randomNumber=Utility.random.nextInt(11)+6;
        int index=offset;
        
        for(int i=0;i<randomNumber;i++){
            randomSeq=Utility.random.nextInt(3);
            switch(randomSeq){
                case 0: 
                    data[index++]=(byte) (Utility.random.nextInt(10)+48);
                    break;
                case 1:
                    data[index++]=(byte) (Utility.random.nextInt(26)+65);
                    break;
                case 2:
                    data[index++]=(byte) (Utility.random.nextInt(26)+97);
                    
            }
            
        }
        return index;
    }
    
    public static int getPassWord(byte[] data, int offset){
        int randomSeq=0,index=offset;
        int randomNumber=Utility.random.nextInt(26)+6;        
        for(int i=0;i<randomNumber;i++){
            randomSeq=Utility.random.nextInt(2);
            switch(randomSeq){
                case 0: 
                    data[index++]=(byte) (Utility.random.nextInt(10)+48);
                    break;
                case 1:
                    data[index++]=(byte) (Utility.random.nextInt(58)+65);
                    break;
                    
            }
            
        }
        return index;
    }
    public static String readLine(InputStream is, byte[] data) throws IOException {
        StringBuilder sb = new StringBuilder();
        int index = 0;

        while (true) {
            int a = is.read(data, index, 1);
            if (a == -1) {
                break;
            }
            if (data[index] == 0x0a) {
                if (index > 0 && data[index - 1] == 0x0d) {
                    break;
                }
            }
            index++;
        }

        String str = new String(data, 0, index + 1);
        if (index > 2) {
            return str;
        }
        return "";
    }

    public static String readLine(byte[] data, int index, int packetLen) {
        while (index < packetLen) {
            if (data[index] == 0x0a) {
                if (index > 0 && data[index - 1] == 0x0d) {
                    break;
                }
            }
            index++;
        }
        String str = new String(data, 0, index + 1);
        if (index > 2) {
            return str;
        }
        return "";
    }
    
    
    
}
