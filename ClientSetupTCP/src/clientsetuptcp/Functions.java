/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientsetuptcp;

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
    
    static void ignoreByte(InputStream is, int i) throws IOException {
        int r;
       for(int j=0;j<i;j++)
           r=is.read();
    }
    public static int getRandomData(byte [] array,int offset,int len) {
        for(int i=0;i<len;i++){
            array[offset+i] = (byte) random.nextInt(256);
        }
        return offset+len;
    }
    // ======================================================================== //
    public static byte[] getRandomData(int len) {
        byte[] data = new byte[len];
        random.nextBytes(data);
        return data;
    }
    
    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    public static byte[] getImapClientHeader(int index){
        String str="";
        String[] headesrArray={" LIST \"\" \"\"\r\n",
            " SELECT \"INBOX\"\r\n",
            " NOOP\r\n",
            " FETCH 12 RFC822\r\n",
            " STORE 4 +FLAGS.SILENT (\\Seen \\Deleted)\r\n",
            " STORE 4 -FLAGS.SILENT (\\Flagged \\Answered)\r\n",
            " EXPUNGE\r\n",
            " CLOSE\r\n"};
        str=headesrArray[index];
        byte[] data = str.getBytes();
        if(data.length<45){
            data=concatenateByteArrays(data,getRandomData(45-data.length));
        }

        return data;
    }
    
    public static byte[] getImapServerHeader(int index){
        String str="";
        String[] headesrArray={" OK LIST completed.\r\n",
            " OK [READ-WRITE] SELECT completed.\r\n",
            " OK NOOP completed.\r\n",
            " OK FETCH completed.\r\n",
            " OK STORE completed.\r\n",
            " OK STORE completed.\r\n",
            " OK EXPUNGE completed.\r\n",
            " OK CLOSE completed.\r\n"};
        str=headesrArray[index];
        byte[] data = str.getBytes();
        if(data.length<36){
            data=concatenateByteArrays(data,getRandomData(36-data.length));
        }

        return data;
    }
        // ========================================================================= //
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if(len % 2 !=0)return null;
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    public static int hexStringToByteArray(String s, byte [] destByteArray, int offset) {
        int len = s.length();
        if(len % 2 !=0)return offset;
        //byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            destByteArray[offset + i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return offset+len/2;
    }
        // ======================================================================== //
    public static int readByte(InputStream is, byte [] data) throws  Exception{
        int minLen = 5;
        int rl, crl;
        int mlen = minLen;
        byte[] chunkHeader = new byte[minLen];
        crl = rl = 0;
        while (crl < minLen) {
            rl = is.read(chunkHeader, crl, minLen - crl);
            if (rl < 0) {
                throw new Exception("Socket Closed:");
            }
            crl += rl;
        }
        System.arraycopy(chunkHeader,0,data,0,mlen);
        minLen = chunkHeader[mlen - 2] & 0xff;
        minLen = (minLen << 8) | (chunkHeader[mlen - 1] & 0xff);
        byte[] b = new byte[minLen];
        crl = 0;
        while (crl < minLen) {
            rl = is.read(b, crl, minLen - crl);
            if (rl < 0) // socket close case
            {
                throw new Exception("Socket Closed:");
            }
            crl += rl;
        }
        System.arraycopy(b,0,data,mlen,b.length);
        return minLen;
    }
    public static int readByte(InputStream is, byte [] data, int len) throws Exception {
        int rl, crl,minLen;
        minLen=len;
        crl = 0;
        while (crl < minLen) {
            rl = is.read(data, crl, minLen - crl);
            if (rl < 0) // socket close case
            {
                throw new Exception("Socket Closed:");
            }
            crl += rl;
        }
        return crl;
    }
    public static int readByte(InputStream is,byte [] data,int offset,int length) throws Exception{
        int rl, crl;
        length = length + offset;
        crl = offset;
        while (crl < length) {
            rl = is.read(data, crl, length - crl);
            if (rl < 0) //socket close case
            {
                throw new Exception("Socket Closed:");
            }
            crl += rl;
        }
        return crl;

    }
    public static int readByte(InputStream is, int headerLen, int offset, byte[] data) throws Exception {

        int minLen = headerLen + 2, totalRead = 0, readLen;
        while (minLen > 0) {

            readLen = is.read(data, offset+totalRead, minLen);
            if (readLen < 0) {
                break;
            }
            totalRead += readLen;
            minLen -= readLen;
        }
        minLen = data[offset+headerLen] & 0xff;
        minLen = (minLen << 8) | (data[offset+headerLen + 1] & 0xff);
        while (minLen > 0) {
            readLen = is.read(data, offset+totalRead, minLen);
            if (readLen < 0) // socket close case
            {
                break;
            }
            totalRead += readLen;
            minLen -= readLen;
        }
        return offset+totalRead;
    }
    
    //==============================================================================
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



    public static String readLine(InputStream is, byte [] data,int offset) throws IOException {
        int index=offset;
        while(true){
            int a=is.read(data,index,1);
            if(a < 0)break;
            if(data[index]==0x0a){
                if(index>0 && data[index-1]==0x0d)break;
            }
            index++;
        }
        String str=new String(data,0,index+1);
        if(index - offset >2){
            return str;
        }
        return "";
    }
    public static String readLine(byte [] data,int index,int packetLen){
        int currentIndex = index;
        while(currentIndex < packetLen){
            if(data[currentIndex]==0x0a){
                if(currentIndex>0 && data[currentIndex-1]==0x0d){
                    currentIndex++;
                    break;
                }
            }
            currentIndex++;
        }
        if(currentIndex - index <= 2)return "";
        String str=new String(data,index,currentIndex - index);
        return str;
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
    
    public static int makeSameLength(byte data[], int offset,int sizeToAdd){
        int index=offset;
        for(int i=0;i<sizeToAdd;i++)
            data[index++]=(byte) Utility.random.nextInt(255);
        return index;
    }
}
