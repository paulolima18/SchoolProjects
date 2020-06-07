import UDP_Agent.*;

import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static UDP_Agent.Datagram_Parser.process_datagram;

public  class Receiver_UDP implements Runnable{

    // Variaveis de instancia e construtor

    // Port usado como defeito
    public static int DEFAULT_PORT = 6666;
    // Socket por onde irá receber conteudo
    private DatagramSocket udp_socket;
    // AnonGW que trata
    private AnonGW anonGW;
    // List de datagrams que chegaram e ainda nao foram processados e condition para alertar processor quando chega um novo :
    private ArrayList<Packet> arrived;
    private Condition has_arrived;
    // Para tratar da concorrencia
    private ReentrantLock lock;
    // Flag para assinalar o facto do receiver estar a trabalhar
    private boolean running;

    public Receiver_UDP(DatagramSocket udp_socket, AnonGW anonGW) {
        this.udp_socket = udp_socket;
        this.anonGW = anonGW;
        this.lock = new ReentrantLock();
        this.arrived = new ArrayList<>();
        this.has_arrived = this.lock.newCondition();
        this.running = false;
    }

    // Funcionalidades

    // método que recebe datagrama

    public void receive_datagram(){
        byte[] buffer = new byte[4096 + 37 + 16];
        DatagramPacket packet = new DatagramPacket(buffer,4096 + 37 + 16);
        while (true){
            try {
                this.udp_socket.receive(packet);
                if (this.getAnonGW().getIps_anongw().contains(packet.getAddress().getHostAddress())) {
                    Packet p = new Packet(packet.getAddress(),packet);
                    this.lock.lock();
                    this.arrived.add(p);
                    break;
                }
            }
            catch (Exception e){ e.printStackTrace(); }
        }
    }

    // run

    public void run(){
        try {
            this.setRunning(true);
            Processor p = new Processor(this);
            new Thread(p).start();
            while (this.getRunning()){
                this.receive_datagram();
                this.has_arrived.signal();
                this.lock.unlock();
            }
        }
        catch (Exception e){}
        finally {
            this.lock.unlock();
        }
    }

    // get's e set's

    public static int getDefaultPort() {
        return DEFAULT_PORT;
    }

    public static void setDefaultPort(int defaultPort) {
        DEFAULT_PORT = defaultPort;
    }

    public DatagramSocket getUdp_socket() {
        return udp_socket;
    }

    public void setUdp_socket(DatagramSocket udp_socket) {
        this.udp_socket = udp_socket;
    }

    public AnonGW getAnonGW() {
        return anonGW;
    }

    public void setAnonGW(AnonGW anonGW) {
        this.anonGW = anonGW;
    }

    public ArrayList<Packet> getArrived() {
        return arrived;
    }

    public void setArrived(ArrayList<Packet> arrived) {
        this.arrived = arrived;
    }

    public Condition getHas_arrived() {
        return has_arrived;
    }

    public void setHas_arrived(Condition has_arrived) {
        this.has_arrived = has_arrived;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }

    public boolean getRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    // classe auxiliar para tratar do processamento dos packet que chegam

    public class Processor implements Runnable{

        // Variaveis de instancia e construtor
        private Receiver_UDP r;

        public Processor(Receiver_UDP r) {
            this.r = r;
        }

        // Funcionalidades

        public void run() {
            while (r.getRunning()){
                try {
                    r.getLock().lock();
                    while (r.getArrived().size() == 0){
                        r.getHas_arrived().await();
                    }
                    InetAddress ip = r.getArrived().get(0).getIp();
                    PDU p = process_datagram(r.getArrived().remove(0).getDatagramPacket().getData());
                    if (!r.getAnonGW().getClients().containsKey(new Client.Id(p.getClient(),p.getIp())) && p instanceof PDU_Request_connection) {
                        r.getAnonGW().getLock().lock();
                        r.getAnonGW().getClients().put(new Client.Id(p.getClient(),p.getIp()),new Client(new Client.Id(p.getClient(),p.getIp()),ip,1));
                        r.getAnonGW().setNew_client(true);
                        r.getAnonGW().getNew_client_arrived().signal();
                        r.getAnonGW().getLock().unlock();
                    }
                    if (r.getAnonGW().getClients().containsKey(new Client.Id(p.getClient(),p.getIp())) && p instanceof PDU_ACK){
                        if (p.getType() == 1) {
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getLock_toPeer1().lock();
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getPdus_FromPeer1().add(p);
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getCondition_fromPeer1().signal();
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getLock_toPeer1().unlock();
                        }
                        if (p.getType() == 3) {
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getLock_toPeer2().lock();
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getPdus_FromPeer2().add(p);
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getCondition_fromPeer2().signal();
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getLock_toPeer2().unlock();
                        }
                        if (((PDU_ACK) p).getSub_type() == (byte) 3){
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getLock_toPeer2().lock();
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).setState(2);
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getCondition_fromPeer2().signal();
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getLock_toPeer2().unlock();
                        }
                    }
                    if (p instanceof PDU_Data){
                        if (p.getType() == 2) {
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getLock_toServer().lock();
                            ((PDU_Data) p).decryptAES();
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getData_fromPeer1().add((PDU_Data) p);
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getCondition_dataFromPeer1().signal();
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getLock_toServer().unlock();
                        }
                        if (p.getType() == 4) {
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getLock_toClient().lock();
                            //System.out.println("ANTES DE DESENCRIPTAR= " + new String(((PDU_Data) p).getData(),Charset.defaultCharset()));
                            ((PDU_Data) p).decryptAES();
                            //System.out.println("DEPOIS DE DESENCRIPTAR= " + new String(((PDU_Data) p).getData(),Charset.defaultCharset()));
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getData_fromPeer2().add((PDU_Data) p);
                            r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getCondition_dataFromPeer2().signal();
                            getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getLock_toClient().unlock();
                        }
                    }
                    if (p instanceof PDU_Close_connection){
                        System.out.println("(Peer 2) Recebi pedido de fechar a conexão!");
                        Sender_UDP s = new Sender_UDP(anonGW.getDatagramSocket());
                        s.send_ack((byte)3, p.getClient(), p.getIp(), r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).getIp_anonGW().getHostAddress(),(byte) 3, 0);
                        System.out.println("(Peer 2) Enviei a confirmação de fecho da conexão!");
                        r.getAnonGW().getClients().get(new Client.Id(p.getClient(),p.getIp())).setState(2);
                        r.getAnonGW().getClients().remove(new Client.Id(p.getClient(),p.getIp()));
                        System.out.println("Apaguei o cliente " + (new Client.Id(p.getClient(),p.getIp())).toString());
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    r.getLock().unlock();
                }
            }
        }

        // get's e set's

        public Receiver_UDP getR() {
            return r;
        }

        public void setR(Receiver_UDP r) {
            this.r = r;
        }
    }

    public class Packet{

        // Variaveis de instancia e construtor

        private InetAddress ip;
        private DatagramPacket datagramPacket;

        public Packet(InetAddress ip, DatagramPacket datagramPacket) {
            this.ip = ip;
            this.datagramPacket = datagramPacket;
        }

        // get's e set's

        public InetAddress getIp() {
            return ip;
        }

        public void setIp(InetAddress ip) {
            this.ip = ip;
        }

        public DatagramPacket getDatagramPacket() {
            return datagramPacket;
        }

        public void setDatagramPacket(DatagramPacket datagramPacket) {
            this.datagramPacket = datagramPacket;
        }
    }
}



