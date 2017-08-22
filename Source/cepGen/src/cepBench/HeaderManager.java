package cepBench;

import java.util.BitSet;

/**
 * Created by mohammadreza on 7/2/2016.
 */
public class HeaderManager {


    public byte version ;
    public byte timeStamp;
    public byte serializerType;
    public byte serializerVersion;
    public byte isExtended;
    public byte orderType;
    public byte extensionSize;
    public String Extension;

    public int header_lenght ;
    public HeaderManager(){
        version = 0b0000000;
        timeStamp = 0b0000000;
        serializerType = 0b0000000;
        serializerVersion = 0b0000000;
        isExtended = 0b0000001;
        orderType = 0b0000011;


    }

    /** read the configurations given by a config manager
     * generating a header containing the relevant information.
     * @param confm config manager
     */
    public void generateHeader(ConfigManager confm){
        version = Byte.decode(confm.getVersion());
        serializerVersion = Byte.decode(confm.getSerializerVersion());

        if (confm.getIsTimestamp().equals("true")){
            orderType = 2;
        }
        else if (confm.getIsTimestamp().equals("false"))
        {
            orderType = 1 ;
        }

        if (confm.getTimestampUnit().equals("mili")){
            timeStamp = -86;
        }
        else if (confm.getTimestampUnit().equals("seconds")) {
            timeStamp = 85;
        }

        if (confm.getSerializer().equals("protobuf")){
            serializerType = 51;
        }
        else if (confm.getSerializer().equals("java")){
            serializerType = -52;
        }
        isExtended = 2;
    }

    /**
     * Generates a byte array to later be put as a header in the files.
     * @return the header byte array
     */
    public byte[] toByteArray(){
        byte[] headerbits = new byte[]{version,timeStamp,serializerType,serializerVersion,isExtended,orderType};
        BitSet fbs = BitSet.valueOf(headerbits);
        BitSet hbs = new BitSet();


        for(int i = 0; i<4; i++){
            if(fbs.get(i))
            hbs.set(i);
        }



        for (int i = 4; i <30 ; i++){
            if(fbs.get(i+4))
                hbs.set(i);
        }
        for (int i = 30; i <32 ; i++) {
            if(fbs.get(i+4+6))
                hbs.set(i);
        }

        byte[] header = hbs.toByteArray();
        header_lenght = header.length ;
        return header;
    }

    /**
     * read a byte array, extracting its information into the header manager object.
     * used for converting the serialized header into header manager object.
     * @param headerBytes
     */
    public void readHeader(byte[] headerBytes){
        BitSet bs = BitSet.valueOf(headerBytes);
        System.out.println(bs.length());
        byte[] normalBytes = bs.get(4,28).toByteArray();
        version = bs.get(0,4).toByteArray()[0];
        timeStamp = normalBytes[0];
        serializerType = normalBytes[1];
        serializerVersion = normalBytes[2];


        if ((!bs.get(28))&bs.get(29)){
            isExtended = 2;
        }
        else {
            isExtended = 1;
        }

        if ((!bs.get(30)) && bs.get(31)){
            orderType = 2;
        }
        else {
            orderType = 1;
        }

    }
}
