package cepBench; /**
 * Created by mohammadreza on 8/5/2016.
 */
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
        isExtended = 2;
        orderType = 0b0000000;
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
        //orderType = (byte)(bs.get(30)?1:0);
    }
}
