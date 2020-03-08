
package cigiclient;


import java.net.InetAddress;

/**
 *
 * @author User
 */

public class CIGIImplementation {
    public byte packetIDIGControl;
    public byte packetSizeIGControl;
    public byte CIGIVersion;
    public byte dbNumber;
    public byte packetHead;
    public long frameCounter;
    public byte packetIDHeightofTerrain;
    public byte packetSizeHeightofTerrain;
    public int hotID;
    public byte packetIDLineofSight;
    public byte packetSizeLineofSight;
    
    
    public CIGIImplementation() {
        packetIDIGControl=0x01;
        packetSizeIGControl=0x10;
        CIGIVersion=0x02;
        dbNumber=0x00;
        packetHead=0x40;
        frameCounter=0x01;
        packetIDHeightofTerrain=0x2c;
        packetSizeHeightofTerrain=0x18;
        hotID=0x01;
        packetIDLineofSight=0x2b;
        packetSizeLineofSight=0x30;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 27)
            return len;
        for(int i = offset + len - 1; i >=offset+15; i--)
            data[i + 27] = data[i];
        
        int timelen=3;
        byte[] timeData=new byte[timelen];
        for(int i=offset+14,j=2;i>=offset+12;i--)
            timeData[j--]=data[i];
        
        int latitudelen=6;
        byte[] latitudedata=new byte[latitudelen];
        
        for(int i=offset+11,j=5;i>=offset+6;i--){
            latitudedata[j--]=data[i];
            
        }
        
        
        int longitudelen=6;
        byte[] longitudeData=new byte[longitudelen];
        for(int i=offset+5,j=5;i>=offset;i--)
            longitudeData[j--]=data[i];
        
        
        int index=offset;
        /** IG Control started**/
        data[index++]=packetIDIGControl;
        data[index++]=packetSizeIGControl;
        data[index++]=CIGIVersion;
        data[index++]=dbNumber;
        data[index++]=packetHead;
        
        data[index++]=0x00;data[index++]=0x00;data[index++]=0x00;
        
        Functions.putLong(data, index, frameCounter);
        index +=4;
        data[index++]=0x00;
        System.arraycopy(timeData, 0, data, index, timelen);
        index+=timelen;
        /** IG Control finished **/
        
        /** Height Of terrain part started**/
        data[index++]=packetIDHeightofTerrain;
        data[index++]=packetSizeHeightofTerrain;
        
        Functions.putInt(data, index, hotID);
        index += 2;
        data[index++]=0x00;data[index++]=0x00;data[index++]=0x00;data[index++]=0x00;
        data[index++]=packetHead;
        byte shift = (byte) Functions.random.nextInt(255);
        shift=(byte) (shift<<3);
        data[index++]=shift;
        System.arraycopy(latitudedata, 0, data, index, latitudelen);
        index+=latitudelen;
        data[index++]=packetHead;
        
        byte shift2 = (byte) Functions.random.nextInt(255);
        shift2=(byte) (shift2<<3);
        data[index++]=shift2;
        
        System.arraycopy(longitudeData, 0, data, index, longitudelen);
        index+=longitudelen;
        /** Height Of terrain part Finished**/
        
        
        /** line of sight started **/
        data[index++]=packetIDLineofSight;
        data[index++]=packetSizeLineofSight;

        /** line of sight finished **/
        
        frameCounter++;
        hotID++;
        return index+len-15;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        frameCounter=Functions.getLong(data, offset + 8);
        hotID=Functions.byteArrayToint(data,offset+18);
        for(int i=offset;i<offset+6;i++){
            data[i]=data[i+34];
        }
        
        for(int i=offset+6;i<offset+12;i++){
            data[i]=data[i+20];
        }
        for(int i=offset+12;i<offset+15;i++){
            data[i]=data[i+1];
        }
        
        for(int i=offset+15;i<offset+len+15;i++){
            data[i]=data[i+27];
        }

        return len-27;
    }
    
}
