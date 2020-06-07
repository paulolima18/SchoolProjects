package UDP_Agent;

import java.nio.ByteBuffer;

public class PDU_ACK extends PDU {

    // Variaveis de instancia

    // 0 = recebi pedido conexao, 1 = recebi confirmacao de pedido de conexao entregue, 2 = recebi data , 3 = confirmacao de close connection recebido
    private byte sub_type;
    private int ack;

    public PDU_ACK(byte type, int client, String ip, byte sub_type, int ack) {
        super(type,client,ip);
        this.sub_type = sub_type;
        this.ack = ack;
    }

    // Funcionalidades

    public byte[] pdu_toBytes(){
        byte[] base_pdu = super.pdu_toBytes();
        byte[] ack = ByteBuffer.allocate(4).putInt(this.getAck()).array();
        byte[] sub_type = new byte[1];
        sub_type[0] = this.getSub_type();
        byte[] result = new byte[base_pdu.length + 1 + ack.length];
        System.arraycopy(base_pdu,0,result,0,base_pdu.length);
        System.arraycopy(sub_type,0,result,13,1);
        System.arraycopy(ack,0,result,14,ack.length);
        return result;
    }

    // get's e set's

    public byte getSub_type() {
        return sub_type;
    }

    public void setSub_type(byte sub_type) {
        this.sub_type = sub_type;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }
}
