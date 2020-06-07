
import UDP_Agent.*;


import javax.crypto.SecretKey;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender_UDP {

    // Variaveis de instacia e construtor

    public DatagramSocket socket ;

    public Sender_UDP(DatagramSocket socket) {
        this.socket = socket;
    }

    // Funcionalidades

    public void send_request_connection(byte type, int client, String source,String destination){
        try {
            PDU_Request_connection pdu = new PDU_Request_connection(type,client,source);
            DatagramPacket packet = new DatagramPacket(pdu.pdu_toBytes(),pdu.pdu_toBytes().length, InetAddress.getByName(destination),6666);
            socket.send(packet);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void send_fst_confimation_request_connection(byte type, int client, String source, String destination){
        try {
            PDU_ACK pdu = new PDU_ACK(type,client,source,(byte)0,0);
            DatagramPacket packet = new DatagramPacket(pdu.pdu_toBytes(),pdu.pdu_toBytes().length, InetAddress.getByName(destination),6666);
            socket.send(packet);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void send_snd_confimation_request_connection(byte type,int client, String source, String destination) {
        try {
            PDU_ACK pdu = new PDU_ACK(type,client,source,(byte)1,0);
            DatagramPacket packet = new DatagramPacket(pdu.pdu_toBytes(),pdu.pdu_toBytes().length, InetAddress.getByName(destination),6666);
            socket.send(packet);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public int send_data(byte type, int client, String source, String destination, int seq, int nr_bytes, SecretKey key,byte[] data){
        int size = 0;
        try{
            PDU_Data pdu = new PDU_Data(type,client,source,seq,nr_bytes,key,data);
            pdu.encryptAES();
            DatagramPacket packet = new DatagramPacket(pdu.pdu_toBytes(),pdu.pdu_toBytes().length, InetAddress.getByName(destination),6666);
            socket.send(packet);
            size = pdu.getData().length;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return size;
    }

    public void send_ack(byte type, int client, String source, String destination, byte sub_type, int ack){
        try {
            PDU_ACK pdu = new PDU_ACK(type,client,source,sub_type,ack);
            DatagramPacket packet = new DatagramPacket(pdu.pdu_toBytes(),pdu.pdu_toBytes().length, InetAddress.getByName(destination),6666);
            socket.send(packet);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void send_close_connection(byte type, int client, String source, String destination){
        try {
            PDU_Close_connection pdu = new PDU_Close_connection(type,client,source);
            DatagramPacket packet = new DatagramPacket(pdu.pdu_toBytes(),pdu.pdu_toBytes().length, InetAddress.getByName(destination),6666);
            socket.send(packet);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
