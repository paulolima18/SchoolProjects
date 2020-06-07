package UDP_Agent;

public class PDU_Request_connection extends PDU{

    public PDU_Request_connection(byte type, int client,String ip) {
        super(type,client,ip);
    }

    // Funcionalidades

    public byte[] pdu_toBytes(){
        byte[] result = super.pdu_toBytes();
        return result;
    }

}
