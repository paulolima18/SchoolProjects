package UDP_Agent;

public class PDU_Close_connection extends PDU{

    // Construtor

    public PDU_Close_connection(byte type, int client, String ip) {
        super(type,client,ip);
    }

    // Funcionalidades

    public byte[] pdu_toBytes(){
        byte[] result = super.pdu_toBytes();
        return result;
    }
}
