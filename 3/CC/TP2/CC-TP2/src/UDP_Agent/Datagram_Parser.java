package UDP_Agent;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Datagram_Parser {

    public static PDU process_datagram(byte[] data) throws Exception {
        PDU result = null;
        byte type = get_type(data);
        switch(type) {
            case 0 : {
                byte[] client = get_client(data);
                PDU_Request_connection pdu = new PDU_Request_connection((byte) 0, ByteBuffer.wrap(client).getInt(),new String(get_ip(data), StandardCharsets.UTF_8));
                result = pdu;
                break;
            }
            case 1 : {
                byte[] client = get_client(data);
                byte sub_type = get_subType(data);
                byte[] ack = get_ack(data);
                PDU_ACK pdu = new PDU_ACK(type,ByteBuffer.wrap(client).getInt(),new String(get_ip(data), StandardCharsets.UTF_8),sub_type,ByteBuffer.wrap(ack).getInt());
                result = pdu;
                break;
            }
            case 2: {
                byte[] client = get_client(data);
                byte[] seq = get_seq(data);
                byte[] nr_bytes = get_nr_bytes(data);
                byte[] key = get_key(data);
                byte[] buffer = get_data(data);
                PDU_Data pdu = new PDU_Data(type,ByteBuffer.wrap(client).getInt(),new String(get_ip(data), StandardCharsets.UTF_8),ByteBuffer.wrap(seq).getInt(),ByteBuffer.wrap(nr_bytes).getInt(),new SecretKeySpec(key,"AES"),buffer);
                result = pdu;
                break;

            }
            case 3:{
                byte[] client = get_client(data);
                byte sub_type = get_subType(data);
                byte[] ack = get_ack(data);
                PDU_ACK pdu = new PDU_ACK(type,ByteBuffer.wrap(client).getInt(),new String(get_ip(data), StandardCharsets.UTF_8),sub_type,ByteBuffer.wrap(ack).getInt());
                result = pdu;
                break;
            }
            case 4: {
                byte[] client = get_client(data);
                byte[] seq = get_seq(data);
                byte[] nr_bytes = get_nr_bytes(data);
                byte[] key = get_key(data);
                byte[] buffer = get_data(data);
                PDU_Data pdu = new PDU_Data(type,ByteBuffer.wrap(client).getInt(),new String(get_ip(data), StandardCharsets.UTF_8),ByteBuffer.wrap(seq).getInt(),ByteBuffer.wrap(nr_bytes).getInt(),new SecretKeySpec(key,"AES"),buffer);
                result = pdu;
                break;
            }
            case 5:{
                byte[] client = get_client(data);
                PDU_Close_connection pdu = new PDU_Close_connection((byte) 5, ByteBuffer.wrap(client).getInt(),new String(get_ip(data), StandardCharsets.UTF_8));
                result = pdu;
                break;
            }
        }
        return result;
    }

    // referente ao pdu base

    private static byte get_type(byte[] data) throws Exception {
       if (data == null) throw new Exception();
       return data[0];
    }

    private static byte[] get_client(byte[] data) throws Exception{
        if (data.length < 5) throw new Exception();
        return Arrays.copyOfRange(data,1,5);
    }

    public static byte[] get_ip(byte[] data) throws Exception{
        if (data.length < 13) throw new Exception();
        return Arrays.copyOfRange(data,5,13);
    }

    // referente ao pdu ack

    private static byte get_subType(byte[] data) throws Exception{
        if (data.length < 14) throw new Exception();
        return data[13];
    }

    public static byte[] get_ack(byte[] data) throws Exception{
        if (data.length < 18) throw new Exception();
        return Arrays.copyOfRange(data,14,18);
    }

    // referente ao pdu data

    public static byte[] get_seq(byte[] data) throws Exception{
        if (data.length < 17) throw new Exception();
        return Arrays.copyOfRange(data,13,17);
    }

    public static byte[] get_nr_bytes(byte[] data) throws Exception{
        if (data.length < 21) throw new Exception();
        return Arrays.copyOfRange(data,17,21);
    }

    public static byte[] get_key(byte[] data) throws Exception{
        if(data.length < 37) throw new Exception();
        return Arrays.copyOfRange(data,21,37);
    }

    public static byte[] get_data(byte[] data) throws Exception{
        if (data.length < 38) throw new Exception();
        return Arrays.copyOfRange(data,37,data.length);
    }



}
