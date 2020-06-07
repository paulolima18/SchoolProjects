import UDP_Agent.*;
import UDP_Agent.PDU_ACK;

import javax.crypto.KeyGenerator;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class AnonGW_Worker implements Runnable {

    // Variaveis de instancia

    // Socket de comunicação com o cliente caso seja uma thread a correr no Peer1, ou socket de comunicação com o servidor caso seja uma thread a correr no Peer2
    private Socket socket_tcp;
    // Variavel de controlo de comunicação
    private int type;
    // Client que trata
    private Client client;
    // socket udp
    private DatagramSocket socket_udp;

    // Construtor

    public AnonGW_Worker(Client client, Socket socket_tcp, int type, DatagramSocket socket_udp) {
        this.client = client;
        this.socket_tcp = socket_tcp;
        this.type = type;
        this.socket_udp = socket_udp;
    }

    // Funcionalidades

    public void run(){
        try{
            switch (this.getType()){
                case 0:{
                    try {
                        DataInputStream tcp_in = new DataInputStream(this.getSocket_tcp().getInputStream());
                        byte[] buffer = new byte[4096];
                        int read = 0;
                        while((read = tcp_in.read(buffer)) > 0){
                            System.out.println("(Peer 1) Recebi uma pedaço de bytes do cliente por conexão TCP");
                            this.getClient().getLock_toPeer2().lock();
                            this.getClient().getData_ToPeer2().add(Arrays.copyOfRange(buffer,0,read));
                            this.getClient().getCondition_toPeer2().signal();
                            this.getClient().getLock_toPeer2().unlock();
                        }
                        Sender_UDP s = new Sender_UDP(this.getSocket_udp());
                        s.send_close_connection((byte)5 , this.getClient().getId().getId(), this.getClient().getId().getIp(), this.getClient().getIp_anonGW().getHostAddress());
                        System.out.println("(Peer 1) Enviei pedido close conection");
                        this.getClient().getLock_toPeer2().lock();
                        while(this.getClient().getState() != 2) this.getClient().getCondition_fromPeer2().await();
                        this.getClient().getLock_toPeer2().unlock();
                        System.out.println("(Peer 1) Recebi confirmação pedido de close conection e vou fechar socket e eliminar a informação do cliente");
                        socket_tcp.close();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 1:{
                    try {
                        Sender_UDP s = new Sender_UDP(this.getSocket_udp());
                        int seq = 0;
                        while (this.getClient().getState() != 2) {
                            this.getClient().getLock_toPeer2().lock();
                            while (this.getClient().getData_ToPeer2().size() == 0) {
                                this.getClient().getCondition_toPeer2().await();
                            }
                            if (this.getClient().getState() == 0) {
                                s.send_request_connection((byte) 0, this.getClient().getId().getId(), this.getClient().getId().getIp(), this.getClient().getIp_anonGW().getHostAddress());
                                System.out.println("(Peer 1) Enviei um pedido de conexão para " + this.getClient().getIp_anonGW().getHostAddress());
                                while (this.getClient().getPdus_FromPeer2().size() == 0)
                                    this.getClient().getCondition_fromPeer2().await();
                                PDU_ACK pdu = (PDU_ACK) this.getClient().getPdus_FromPeer2().remove(0);
                                if (pdu.getSub_type() == (byte) 0) {
                                    System.out.println("(Peer 1) Recebi confimacao da entrega do pedido conexao!");
                                    s.send_snd_confimation_request_connection((byte) 1, this.getClient().getId().getId(), this.getClient().getId().getIp(), this.getClient().getIp_anonGW().getHostAddress());
                                    System.out.println("(Peer 1) Enviei a ultima iteracao do 3-way handshake!");
                                    this.getClient().setState(1);
                                }
                            }
                            if (this.getClient().getState() == 1) {
                                s.send_data((byte)2, this.getClient().getId().getId(), this.getClient().getId().getIp(), this.getClient().getIp_anonGW().getHostAddress(),seq,this.getClient().getData_ToPeer2().get(0).length, KeyGenerator.getInstance("AES").generateKey(),this.getClient().getData_ToPeer2().get(0));
                                System.out.println("(Peer 1) Enviei um pedaço de bytes por ligação UDP para: " + this.getClient().getIp_anonGW().getHostAddress());
                                seq++;
                                this.getClient().getLock_toPeer2().unlock();
                                new Thread(new Ack_Waiter(0,seq,this.getClient(),this.getClient().getData_ToPeer2().get(0),seq-1,this.getClient().getData_ToPeer2().get(0).length,s)).start();
                                System.out.println("(Peer 1) Lancei um waiter para " + this.getClient().getIp_anonGW().getHostAddress());
                                this.getClient().getData_ToPeer2().remove(0);
                            }

                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 2:{
                    try {
                        Sender_UDP s = new Sender_UDP(this.getSocket_udp());
                        this.getClient().getLock_toServer().lock();
                        while (this.getClient().getState() != 2){
                            while (this.getClient().getData_fromPeer1().size() == 0 ){
                                this.getClient().getCondition_dataFromPeer1().await();
                            }
                            for (int i = 0; i < this.getClient().getData_fromPeer1().size(); i++){
                                PDU_Data data = (PDU_Data) this.getClient().getData_fromPeer1().remove(0);
                                System.out.println("(Peer 2) Recebi um pedaço de bytes via UDP com seq= " + data.getSeq());
                                if (!this.getClient().contains_data(data,1)) this.getClient().getData_toServer().add(data);
                                s.send_ack((byte)3, this.getClient().getId().getId(),this.getClient().getId().getIp(),this.getClient().getIp_anonGW().getHostAddress(),(byte)2,data.getSeq() + 1);
                                System.out.println("(Peer 2) Enviei ack de confimação de receção bytes, com ack= " + data.getSeq() + 1);
                            }
                            this.getClient().getCondition_toServer().signal();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        this.getClient().getLock_toServer().unlock();
                    }
                    break;
                }
                case 3: {
                    try {
                        DataOutputStream tcp_out = new DataOutputStream(this.getSocket_tcp().getOutputStream());
                        int waiting_seq = 0;
                        this.getClient().getLock_toServer().lock();
                        while (this.getClient().getState() != 2){
                            while (this.getClient().contains_next_toServer(waiting_seq) == false) this.getClient().getCondition_toServer().await();
                            System.out.println("(Peer 2) Enviei dados ao target server por conexão TCP");
                            PDU_Data pdu = (PDU_Data) this.getClient().get_next_toServer(waiting_seq);
                            tcp_out.write(pdu.getData());
                            tcp_out.flush();
                            waiting_seq ++;
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        this.getClient().getLock_toServer().unlock();
                    }
                    break;
                }
                case 4:{
                    try {
                        DataInputStream tcp_in = new DataInputStream(this.getSocket_tcp().getInputStream());
                        byte[] buffer = new byte[4096];
                        int read = 0;
                        while ((read = tcp_in.read(buffer)) > 0){
                            this.getClient().getLock_toPeer1().lock();
                            System.out.println("(Peer 2) Recebi dados do target server para enviar ao cliente");
                            this.getClient().getData_ToPeer1().add(Arrays.copyOfRange(buffer,0,read));
                            this.getClient().getCondition_toPeer1().signal();
                            this.getClient().getLock_toPeer1().unlock();
                        }
                        this.getSocket_tcp().close();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 5:{
                    try {
                        int seq = 0;
                        Sender_UDP s = new Sender_UDP(this.getSocket_udp());
                        while (this.getClient().getState() != 2) {
                            this.getClient().getLock_toPeer1().lock();
                            while (this.getClient().getData_ToPeer1().size() == 0) {
                                this.getClient().getCondition_toPeer1().await();
                            }
                            for (int i = 0; i < this.getClient().getData_ToPeer1().size(); i++) {
                                byte[] data = this.getClient().getData_ToPeer1().remove(0);
                                s.send_data((byte)4,this.getClient().getId().getId(),this.getClient().getId().getIp(),this.getClient().getIp_anonGW().getHostAddress(),seq,data.length,KeyGenerator.getInstance("AES").generateKey(),data);
                                System.out.println("(Peer 2) Enviei um pedaço de bytes por ligação UDP para: " + this.getClient().getIp_anonGW().getHostAddress());
                                seq++;
                                new Thread(new Ack_Waiter(1,seq,this.getClient(),data,seq-1,data.length,s)).start();
                                System.out.println("(Peer 2) Lancei um ack waiter para há espera de receber ack=" + seq);
                            }
                            this.getClient().getLock_toPeer1().unlock();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 6:{
                    try {
                        Sender_UDP s = new Sender_UDP(this.getSocket_udp());
                        this.getClient().getLock_toClient().lock();
                        while (this.getClient().getState() != 2){
                            while (this.getClient().getData_fromPeer2().size() == 0){
                                this.getClient().getCondition_dataFromPeer2().await();
                            }
                            for (int i = 0 ; i < this.getClient().getData_fromPeer2().size(); i++){
                                PDU_Data data = (PDU_Data) this.getClient().getData_fromPeer2().remove(0);
                                System.out.println("(Peer 1) Recebi um pedaço de bytes via UDP com seq= " + data.getSeq());
                                if (!this.getClient().contains_data(data,0)) this.getClient().getData_toClient().add(data);
                                s.send_ack((byte)1, this.getClient().getId().getId(),this.getClient().getId().getIp(), this.getClient().getIp_anonGW().getHostAddress(),(byte)2,data.getSeq() + 1);
                                System.out.println("(Peer 1) Enviei ack de confimação de receção bytes, com ack= " + data.getSeq() + 1);
                            }
                            this.getClient().getCondition_toClient().signal();
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        this.getClient().getLock_toClient().unlock();
                    }
                    break;
                }
                case 7:{
                    try {
                        DataOutputStream tcp_out = new DataOutputStream(this.getSocket_tcp().getOutputStream());
                        int waiting_seq = 0;
                        this.getClient().getLock_toClient().lock();
                        while (this.getClient().getState() != 2){
                            while (this.getClient().contains_next_toClient(waiting_seq) == false){
                                this.getClient().getCondition_toClient().await();
                            }
                            System.out.println("(Peer 1) Enviei um pedaço bytes via TCP para o cliente");
                            PDU_Data pdu = (PDU_Data) this.getClient().get_next_toClient(waiting_seq);
                            tcp_out.write(pdu.getData(),0,pdu.getNr_bytes());
                            tcp_out.flush();
                            waiting_seq ++;
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        this.getClient().getLock_toClient().unlock();
                    }
                    break;
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
    }

    // get's e set's

    public Socket getSocket_tcp() {
        return socket_tcp;
    }

    public void setSocket_tcp(Socket socket_tcp) {
        this.socket_tcp = socket_tcp;
    }

    public DatagramSocket getSocket_udp() {
        return socket_udp;
    }

    public void setSocket_udp(DatagramSocket socket_udp) {
        this.socket_udp = socket_udp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
