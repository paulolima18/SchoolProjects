package UDP_Agent;

import java.nio.ByteBuffer;

public abstract class PDU {

    // Variaveis de instancia e construtor

    // type: 0 = request connection, 1 = ACK_toServer, 2 = Data_toServer, 3 = ACK_fromServer, 4 = Data_fromServer, 5 = close connection
    private byte type;
    // client a que se refere
    private int client;
    // ip do anonGW que enviou
    private String ip;

    public PDU() {
    }

    public PDU(byte type, int client,String ip) {
        this.type = type;
        this.client = client;
        this.ip = ip;
    }

    // Funcionalidades

    public byte[] pdu_toBytes(){
        byte[] result = new byte[13];
        result[0] = this.getType();
        byte[] client = ByteBuffer.allocate(4).putInt(this.getClient()).array();
        System.arraycopy(client,0,result,1,client.length);
        System.arraycopy(this.getIp().getBytes(),0,result,5,8);
        return result;
    }

    // get's e set's

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    ///

    @Override
    public String toString() {
        return "PDU{" +
                "type=" + type +
                ", client=" + client +
                ", ip='" + ip + '\'' +
                '}';
    }
}
