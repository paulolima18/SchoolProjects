import UDP_Agent.*;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Client {

    // Variáveis de instância e construtor

    // identificacao
    private Id id;

    //  0 = ainda nao conectado ao anonGW emparelhado, 1 = já conectado e pronto a iniciar a tranferencia dados, 2 = fechar a conexão
    private int state;

    // peer 1 -> peer 2
    private List<byte[]> data_ToPeer2;
    private List<PDU> pdus_FromPeer2; // pdu's de request connection ou ack's
    private List<PDU_Data> data_fromPeer2; // pdu's de dados recebidos do peer2

    // peer 1 <- peer 2
    private List<byte[]> data_ToPeer1;
    private List<PDU> pdus_FromPeer1; // pdu's de request connection ou ack's
    private List<PDU_Data> data_fromPeer1; // pdu's de dados recebidos do peer1

    // from server
    private List<byte[]> data_FromServer;

    // to server
    private List<PDU_Data> data_toServer; // filtra

    // to client
    private List<PDU_Data> data_toClient; // filtra

    // ip do anonGW ao qual esta emparelhado
    private InetAddress ip_anonGW;

    // Para lidar com as threads:

    private ReentrantLock lock_toPeer2;
    private ReentrantLock lock_toPeer1;
    private ReentrantLock lock_toServer;
    private ReentrantLock lock_toClient;

    // Serve para adormecer a thread enquanto não há bytes do client para tratar
    private Condition condition_toPeer2;
    // Serve para adormecer a thread enquanto espera por um ack do Peer 2
    private Condition condition_fromPeer2;
    // Serve para adormecer a thread enquanto espera por a rececao dados do Peer 2
    private Condition condition_dataFromPeer2;
    // Serve para adormecer a thread que envia dados resposta para Peer 1
    private Condition condition_toPeer1;
    // Serve para adormecer a thread enquanto espera por um ack do Peer 1
    private Condition condition_fromPeer1;
    // Serve para adormecer a thread enquanto espera por a rececao dados do Peer 1
    private Condition condition_dataFromPeer1;

    private Condition condition_toServer;
    private Condition condition_toClient;

    // type = 0 => client do peer 1; =1 => client do peer 2
    public Client(Id id, InetAddress ip_anonGW, int type) {
        if (type == 0){
            this.id = id;
            this.data_ToPeer2 = new ArrayList<>();
            this.pdus_FromPeer2 = new ArrayList<>();
            this.data_fromPeer2 = new ArrayList<>();
            this.data_toClient = new ArrayList<>();
            this.lock_toPeer2 = new ReentrantLock();
            this.lock_toClient = new ReentrantLock();
            this.condition_toPeer2 = lock_toPeer2.newCondition();
            this.condition_fromPeer2 = lock_toPeer2.newCondition();
            this.condition_dataFromPeer2 = lock_toClient.newCondition();
            this.condition_toClient = lock_toClient.newCondition();
            this.ip_anonGW = ip_anonGW;
        }
        if (type == 1){
            this.data_ToPeer1 = new ArrayList<>();
            this.pdus_FromPeer1 = new ArrayList<>();
            this.data_fromPeer1 = new ArrayList<>();
            this.data_toServer = new ArrayList<>();
            this.lock_toPeer1 = new ReentrantLock();
            this.lock_toServer = new ReentrantLock();
            this.condition_toPeer1 = lock_toPeer1.newCondition();
            this.condition_fromPeer1 = lock_toPeer1.newCondition();
            this.condition_dataFromPeer1 = lock_toServer.newCondition();
            this.condition_toServer = lock_toServer.newCondition();
            this.ip_anonGW = ip_anonGW;
            this.id = id;
        }
    }

    // funcionalidades

    public boolean contains_next_toServer(int seq){
        for (PDU_Data pdu : this.getData_toServer()){
            if (pdu.getSeq() == seq) return true;
        }
        return false;
    }

    public PDU_Data get_next_toServer(int seq){
        for (PDU_Data pdu : this.getData_toServer()){
            if (pdu.getSeq() == seq) return pdu;
        }
        return null;
    }

    public boolean contains_next_toClient(int seq){
        for (PDU_Data pdu : this.getData_toClient()){
            if (pdu.getSeq() == seq) return true;
        }
        return false;
    }

    public PDU_Data get_next_toClient(int seq){
        for (PDU_Data pdu : this.getData_toClient()){
            if (pdu.getSeq() == seq) return pdu;
        }
        return null;
    }

    public boolean containsAck(int waiting_ack, int type){
        boolean result = false;
        if (type == 0){
            for(PDU pdu : this.getPdus_FromPeer2()){
                PDU_ACK ack = (PDU_ACK)pdu;
                if (ack.getAck() == waiting_ack) {
                    result = true;
                }
            }
        }
        if (type == 1){
            for(PDU pdu : this.getPdus_FromPeer1()){
                PDU_ACK ack = (PDU_ACK)pdu;
                if (ack.getAck() == waiting_ack) return true;
            }
        }
        return result;
    }

    public boolean contains_data(PDU_Data data, int type){
        boolean result = false;
        if (type == 0){
            for(PDU_Data pdu : this.getData_toClient()){
                if (pdu.getSeq() == data.getSeq()) return true;
            }
        }
        if (type == 1){
            for(PDU_Data pdu : this.getData_toServer()){
                if (pdu.getSeq() == data.getSeq()) return true;
            }
        }
        return result;
    }

    // get's e set's


    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<byte[]> getData_ToPeer2() {
        return data_ToPeer2;
    }

    public void setData_ToPeer2(List<byte[]> data_ToPeer2) {
        this.data_ToPeer2 = data_ToPeer2;
    }

    public List<PDU> getPdus_FromPeer2() {
        return pdus_FromPeer2;
    }

    public void setPdus_FromPeer2(List<PDU> pdus_FromPeer2) {
        this.pdus_FromPeer2 = pdus_FromPeer2;
    }

    public List<byte[]> getData_ToPeer1() {
        return data_ToPeer1;
    }

    public void setData_ToPeer1(List<byte[]> data_ToPeer1) {
        this.data_ToPeer1 = data_ToPeer1;
    }

    public List<PDU> getPdus_FromPeer1() {
        return pdus_FromPeer1;
    }

    public void setPdus_FromPeer1(List<PDU> pdus_FromPeer1) {
        this.pdus_FromPeer1 = pdus_FromPeer1;
    }

    public List<byte[]> getData_FromServer() {
        return data_FromServer;
    }

    public void setData_FromServer(List<byte[]> data_FromServer) {
        this.data_FromServer = data_FromServer;
    }

    public List<PDU_Data> getData_toServer() {
        return data_toServer;
    }

    public void setData_toServer(List<PDU_Data> data_toServer) {
        this.data_toServer = data_toServer;
    }

    public List<PDU_Data> getData_toClient() {
        return data_toClient;
    }

    public void setData_toClient(List<PDU_Data> data_toClient) {
        this.data_toClient = data_toClient;
    }

    public InetAddress getIp_anonGW() {
        return ip_anonGW;
    }

    public void setIp_anonGW(InetAddress ip_anonGW) {
        this.ip_anonGW = ip_anonGW;
    }

    public ReentrantLock getLock_toPeer2() {
        return lock_toPeer2;
    }

    public void setLock_toPeer2(ReentrantLock lock_toPeer2) {
        this.lock_toPeer2 = lock_toPeer2;
    }

    public ReentrantLock getLock_toServer() {
        return lock_toServer;
    }

    public void setLock_toServer(ReentrantLock lock_toServer) {
        this.lock_toServer = lock_toServer;
    }

    public Condition getCondition_toPeer2() {
        return condition_toPeer2;
    }

    public void setCondition_toPeer2(Condition condition_toPeer2) {
        this.condition_toPeer2 = condition_toPeer2;
    }

    public Condition getCondition_fromPeer2() {
        return condition_fromPeer2;
    }

    public void setCondition_fromPeer2(Condition condition_fromPeer2) {
        this.condition_fromPeer2 = condition_fromPeer2;
    }

    public Condition getCondition_toPeer1() {
        return condition_toPeer1;
    }

    public void setCondition_toPeer1(Condition condition_toPeer1) {
        this.condition_toPeer1 = condition_toPeer1;
    }

    public Condition getCondition_fromPeer1() {
        return condition_fromPeer1;
    }

    public void setCondition_fromPeer1(Condition condition_fromPeer1) {
        this.condition_fromPeer1 = condition_fromPeer1;
    }

    public Condition getCondition_toServer() {
        return condition_toServer;
    }

    public void setCondition_toServer(Condition condition_toServer) {
        this.condition_toServer = condition_toServer;
    }

    public Condition getCondition_toClient() {
        return condition_toClient;
    }

    public void setCondition_toClient(Condition condition_toClient) {
        this.condition_toClient = condition_toClient;
    }

    public List<PDU_Data> getData_fromPeer1() {
        return data_fromPeer1;
    }

    public void setData_fromPeer1(List<PDU_Data> data_fromPeer1) {
        this.data_fromPeer1 = data_fromPeer1;
    }

    public Condition getCondition_dataFromPeer1() {
        return condition_dataFromPeer1;
    }

    public void setCondition_dataFromPeer1(Condition condition_dataFromPeer1) {
        this.condition_dataFromPeer1 = condition_dataFromPeer1;
    }

    public ReentrantLock getLock_toPeer1() {
        return lock_toPeer1;
    }

    public void setLock_toPeer1(ReentrantLock lock_toPeer1) {
        this.lock_toPeer1 = lock_toPeer1;
    }

    public List<PDU_Data> getData_fromPeer2() {
        return data_fromPeer2;
    }

    public void setData_fromPeer2(List<PDU_Data> data_fromPeer2) {
        this.data_fromPeer2 = data_fromPeer2;
    }

    public ReentrantLock getLock_toClient() {
        return lock_toClient;
    }

    public void setLock_toClient(ReentrantLock lock_toClient) {
        this.lock_toClient = lock_toClient;
    }

    public Condition getCondition_dataFromPeer2() {
        return condition_dataFromPeer2;
    }

    public void setCondition_dataFromPeer2(Condition condtion_dataFromPeer2) {
        this.condition_dataFromPeer2 = condtion_dataFromPeer2;
    }

    public static class Id{

        // Variaveis de instancia

        private int id;
        private String ip;

        public Id(int id, String ip) {
            this.id = id;
            this.ip = ip;
        }

        // get's e set's

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        // funcionalidades

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id1 = (Id) o;
            return id == id1.id &&
                    Objects.equals(ip, id1.ip);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, ip);
        }

        @Override
        public String toString() {
            return "Id{ " +
                    "id=" + id +
                    ", ip='" + ip + '\'' +
                    '}';
        }
    }

}
