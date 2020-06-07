import UDP_Agent.PDU;

import javax.crypto.KeyGenerator;

public class Ack_Waiter implements Runnable{

    // Variaveis de instancia e construtor

    // tipo
    private int type;
    // número do ack que espera obter
    private int ack_waiting;
    // client que trata
    private Client client;
    // data, seq, e sender para retransmissao caso necessário :
    private byte[] data;
    private int seq;
    private int nr_bytes;
    private Sender_UDP s;

    public Ack_Waiter(int type, int ack_waiting, Client client, byte[] data, int seq, int nr_bytes, Sender_UDP s) {
        this.type = type;
        this.ack_waiting = ack_waiting;
        this.client = client;
        this.data = data;
        this.seq = seq;
        this.nr_bytes = nr_bytes;
        this.s = s;
    }

    // Funcionalidades

    public void run(){
        boolean flag = false;
        long time = 1000 * 1000 * 1000 ;
        try {
            switch (this.getType()) {
                case 0 :{
                    while (!flag) {
                        this.getClient().getLock_toPeer2().lock();
                        System.out.println("(Waiter) Vou esperar 1 segundo por receber o ack= " + this.getAck_waiting());
                        while (!this.getClient().containsAck(ack_waiting, 0) && time > 0) {
                            time = this.getClient().getCondition_fromPeer2().awaitNanos(time);
                        }
                        if (this.getClient().containsAck(ack_waiting, 0)) {
                            System.out.println("(Waiter) Recebi o ack= " + this.getAck_waiting());
                            flag = true;
                        } else {
                            System.out.println("(Waiter) Não recebi recebi o ack= " + this.getAck_waiting() + " por isso vou reenviar o pacote e esperar novamente");
                            s.send_data((byte) 2, this.getClient().getId().getId(), this.getClient().getId().getIp(), this.getClient().getIp_anonGW().getHostAddress(), seq, nr_bytes, KeyGenerator.getInstance("AES").generateKey(),data);
                            time = 1000 * 1000 * 1000;
                        }
                        this.getClient().getLock_toPeer2().unlock();
                    }
                    break;
                }
                case 1 :{
                    while (!flag) {
                        this.getClient().getLock_toPeer1().lock();
                        System.out.println("(Waiter) Vou esperar 1 segundo por receber o ack= " + this.getAck_waiting());
                        while (!this.getClient().containsAck(ack_waiting, 1) && time > 0) {
                            time = this.getClient().getCondition_fromPeer1().awaitNanos(time);
                        }
                        if (this.getClient().containsAck(ack_waiting, 1)) {
                            System.out.println("(Waiter) Recebi o ack= " + this.getAck_waiting());
                            flag = true;
                        } else {
                            System.out.println("(Waiter) Não recebi recebi o ack= " + this.getAck_waiting() + " por isso vou reenviar o pacote e esperar novamente");
                            s.send_data((byte) 4, this.getClient().getId().getId(), this.getClient().getId().getIp(), this.getClient().getIp_anonGW().getHostAddress(), seq, nr_bytes,KeyGenerator.getInstance("AES").generateKey(),data);
                            time = 1000 * 1000 * 1000;
                        }
                        this.getClient().getLock_toPeer1().unlock();
                    }
                    break;

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // get's e set's

    public int getAck_waiting() {
        return ack_waiting;
    }

    public void setAck_waiting(int ack_waiting) {
        this.ack_waiting = ack_waiting;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getNr_bytes() {
        return nr_bytes;
    }

    public void setNr_bytes(int nr_bytes) {
        this.nr_bytes = nr_bytes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
