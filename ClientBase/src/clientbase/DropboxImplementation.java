/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbase;

/**
 *
 * @author User
 */
public class DropboxImplementation {
    public static byte[] version,port,hostInt,displayName,namespaces;
    public int mod,divisor;
    public int hostIntID,tempHostIntID;
    public int createInt;
    public int createDataLen;
    
    static{
        version=Utility.hexStringToByteArray("2276657273696f6e223a205b322c20305d");
        port=Utility.hexStringToByteArray("22706f727422");
        hostInt=Utility.hexStringToByteArray("22686f73745f696e7422");
        displayName=Utility.hexStringToByteArray("22646973706c61796e616d6522");
        namespaces=Utility.hexStringToByteArray("226e616d6573706163657322");
    }
    
    public DropboxImplementation() {
        hostIntID=Utility.random.nextInt();
    }
    
    public int createPacket(byte [] data, int offset, int len, int destPort){

//        for(int i = offset + len - 1; i >=offset; i--)
//            data[i + 57] = data[i];
        byte[] tempData=new byte[len+4];
        System.arraycopy(data, offset, tempData, 0, len);
        divisor=len/4;
        
        if(len%4!=0){
            mod=len-(divisor*4);
            int dataToAddSize=4-mod;
            for(int i=0;i<dataToAddSize;i++) tempData[len+i]=0x00;
            divisor+=1;
//            for(int i=0;i<len+dataToAddSize;i++) System.out.print("  -->  "+tempData[i]+"\n");
        }
        if(data.length <= offset + divisor*13+122) return len;
        
        int index=offset,tempIndex=0;
        index=buildHeader(data, index, len, destPort);
        
        System.arraycopy(namespaces, 0, data, index, namespaces.length);
        index+=namespaces.length; //12 Bytes
        data[index++]=0x3a; data[index++]=0x20; //inside block separator
        data[index++]=0x5b;
        
        for(int i=0;i<divisor;i++){
            createInt=Functions.getInt4(tempData, tempIndex);
            tempIndex+=4;
//            System.out.println("-----------------------------------------> "+createInt);
            if(createInt<0){
                data[index++]=(byte) (Utility.random.nextInt(5)+50);
                createInt=createInt*-1;
            }else data[index++]=0x31;
            
            index=modIntDivision(data, index, createInt, 10);
            index+=10;
            if(i<divisor-1){
                data[index++]=0x2c; data[index++]=0x20; //Separation bytes
            }    
        }
        
        data[index++]=0x5d; data[index++]=0x7d; 
        
        return divisor*13+122;
    }
    
    
    public int readDataAndCreateNumber(byte[] data, int offset, int sign){
        int number=0,value,pow=9,index=offset;
        for(int i=0;i<10;i++){
            value=data[index++]-48;
            number=(int) (number+value*Math.pow(10, pow));
            pow--;
        }
        if(sign!=1) number=number*-1;
        return number;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        int index=offset,newIndex=0,sign,accessData;
        sign=data[index+75]-48;
        createDataLen=readDataAndCreateNumber(data, index+76, sign);
        createDataLen=createDataLen<<22;
        createDataLen=createDataLen>>22;
        divisor=createDataLen/4;
        if(createDataLen%4!=0) divisor+=1;
        for(int i=0;i<divisor;i++){
            sign=data[index+122+i*13]-48;
            accessData=readDataAndCreateNumber(data, index+122+i*13+1, sign);
            Functions.putInt4(data, offset+newIndex, accessData);
            newIndex+=4;
        }
        
        return createDataLen;
    }
    
    public int modIntDivision(byte[] data, int offset, int number,int size){
        int index=offset;
        for(int i=0;i<size;i++){
            mod=number%10;
            data[index+9-i]=(byte) (mod+48);
            number=number/10;
        }
        return index;
    }
    
    public int buildHeader(byte[] data, int offset,int len,int destPort){
        int index=offset;
        
        data[index++]=0x7b; //starter
        System.arraycopy(version, 0, data, index, version.length);
        index+=version.length;  //17bytes
        data[index++]=0x2c; data[index++]=0x20; //Separation bytes
        
        System.arraycopy(port, 0, data, index, port.length);
        index+=port.length; // 6 bytes
        data[index++]=0x3a; data[index++]=0x20; //inside block separator
//        index=modIntDivision(data, index, destPort, 5);
        
        for(int i=0;i<5;i++){
            mod=destPort%10;
            data[index+4-i]=(byte) (mod+48);
            destPort=destPort/10;
        }
        index+=5;
        data[index++]=0x2c; data[index++]=0x20; //Separation bytes
        
        System.arraycopy(hostInt, 0, data, index, hostInt.length);
        index+=hostInt.length; // 10 Bytes
        data[index++]=0x3a; data[index++]=0x20; //inside block separator
        
        data[index++]=(byte) (Utility.random.nextInt(9)+49);
        for(int i=0;i<27;i++) data[index++]=(byte) (Utility.random.nextInt(10)+48);
        
        hostIntID=hostIntID<<10;
        hostIntID=hostIntID|len;
        if(hostIntID<0) {
            hostIntID*=-1;
            data[index++]=(byte) (Utility.random.nextInt(5)+50);
        }else data[index++]=0x31;
//        tempHostIntID=hostIntID;
        index=modIntDivision(data, index, hostIntID, 10);
        index+=10;
        data[index++]=0x2c; data[index++]=0x20; //Separation bytes
        
        System.arraycopy(displayName, 0, data, index, displayName.length);
        index+=displayName.length; //13 Bytes
        data[index++]=0x3a; data[index++]=0x20; //inside block separator
//        tempHostIntID=hostIntID;
//        index=modIntDivision(data, index, tempHostIntID, 10);
//        index+=10;
        data[index++]=0x22; data[index++]=0x22; 
        data[index++]=0x2c; data[index++]=0x20; //Separation bytes
        
        hostIntID=Utility.random.nextInt();
        
        return index;
        
    }
    
    
}