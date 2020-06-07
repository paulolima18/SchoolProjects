import UDP_Agent.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AnonGW {

    // Variaveis de instância

    public static int DEFAULT_PORT_TCP = 80;
    public static int DEFAULT_PORT_UDP = 6666;
    public static int nr_clients = 0;
    private String ip;
    // Server de atender pedidos
    private ServerSocket server;
    private String target_server_socket;
    // DatagramSocket
    private DatagramSocket datagramSocket;
    // Estrutura com ip's dos restantes anonGW
    private List<String> ips_anongw;
    // Estruturas para lidar com clientes conectados
    private HashMap<Client.Id, Client> clients;
    // Estruturas para lidar com concorrencia e chegada de um novo cliente:
    private ReentrantLock lock;
    private boolean new_client;
    private Condition new_client_arrived;

    // Construtor

    public AnonGW(int port, String target_server, String[] ips_anongw) throws Exception{
        this.ip = InetAddress.getLocalHost().getHostAddress();
        this.server = new ServerSocket(DEFAULT_PORT_TCP,20,InetAddress.getByName(this.ip));
        this.target_server_socket = target_server;
        this.datagramSocket = new DatagramSocket(DEFAULT_PORT_UDP);
        this.ips_anongw = new ArrayList<>();
        for (String s : ips_anongw) this.ips_anongw.add(s);
        this.clients= new HashMap<>();
        this.lock = new ReentrantLock();
        this.new_client = false;
        this.new_client_arrived = lock.newCondition();

    }

    // Funcionalidades

    // Método que devolve um anonGW de forma random para contruir tunel
    public String getIP_AnonGW(){
        int randomNum = ThreadLocalRandom.current().nextInt(0,ips_anongw.size());
        return ips_anongw.get(randomNum);
    }

    // Main
    public static void main(String[] args) {
            String[] aux = Arrays.copyOfRange(args,2,args.length);
            System.out.println("### ANON SERVER ###");

            // receiver UDP sempre a trabalhar
            try {
                AnonGW anonGW = new AnonGW(Integer.parseInt(args[0]),args[1],aux);
                new Thread(new Receiver_UDP(anonGW.getDatagramSocket(),anonGW)).start();
                new Thread(()->{
                    while(true){
                        try {
                            anonGW.getLock().lock();
                            while (anonGW.getNew_client() == false) {
                                anonGW.getNew_client_arrived().await();
                            }
                            for (Client client : anonGW.getClients().values()) {
                                if (client.getState() == 0){
                                    Socket server = new Socket(anonGW.getTarget_server_socket(),DEFAULT_PORT_TCP);
                                    Sender_UDP s = new Sender_UDP(anonGW.getDatagramSocket());
                                    s.send_fst_confimation_request_connection((byte)3, client.getId().getId(), client.getId().getIp(),client.getIp_anonGW().getHostAddress());
                                    client.setState(1);
                                    System.out.println("> (Peer 2) Recebi um pedido de conexão UDP, e enviei confimação");
                                    AnonGW_Worker worker1 = new AnonGW_Worker(client,server,2,anonGW.getDatagramSocket());
                                    AnonGW_Worker worker2 = new AnonGW_Worker(client,server,3,anonGW.getDatagramSocket());
                                    AnonGW_Worker worker3 = new AnonGW_Worker(client,server,4,anonGW.getDatagramSocket());
                                    AnonGW_Worker worker4 = new AnonGW_Worker(client,server,5,anonGW.getDatagramSocket());
                                    Thread t1 = new Thread(worker1);
                                    Thread t2 = new Thread(worker2);
                                    Thread t3 = new Thread(worker3);
                                    Thread t4 = new Thread(worker4);
                                    t1.start();
                                    t2.start();
                                    t3.start();
                                    t4.start();
                                    /*
                                    t1.join();
                                    t2.join();
                                    t3.join();
                                    t4.join();
                                    */

                                }
                            }
                            anonGW.setNew_client(false);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        finally {
                            anonGW.getLock().unlock();
                        }

                    }
                }).start();

                // receiver TCP sempre a trabalhar
                while (true){
                    System.out.println("> Server TCP is running and waiting for new connection...");
                    Socket client_socket = anonGW.getServer().accept();
                    new Thread(()->{
                        System.out.println("> (Peer 1) Connection TCP received! (new client arrived)");
                        try {
                            Client client = new Client(new Client.Id(nr_clients,anonGW.getIp()),InetAddress.getByName(anonGW.getIP_AnonGW()),0);
                            anonGW.getClients().put(new Client.Id(nr_clients++,anonGW.getIp()), client);
                            AnonGW_Worker worker1 = new AnonGW_Worker(client,client_socket,0,anonGW.getDatagramSocket());
                            AnonGW_Worker worker2 = new AnonGW_Worker(client,client_socket,1,anonGW.getDatagramSocket());
                            AnonGW_Worker worker3 = new AnonGW_Worker(client,client_socket,6,anonGW.getDatagramSocket());
                            AnonGW_Worker worker4 = new AnonGW_Worker(client,client_socket,7,anonGW.getDatagramSocket());
                            Thread t1 = new Thread(worker1);
                            Thread t2 = new Thread(worker2);
                            Thread t3 = new Thread(worker3);
                            Thread t4 = new Thread(worker4);
                            t1.start();
                            t2.start();
                            t3.start();
                            t4.start();
                            t1.join();
                            anonGW.getLock().lock();
                            anonGW.getClients().remove(client.getId());
                            System.out.println("(Peer 1) Apaguei o cliente = " + client.getId().toString());
                            anonGW.getLock().unlock();
                            t2.join();
                            t3.join();
                            t4.join();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }

    // get's e set's

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public String getTarget_server_socket() {
        return target_server_socket;
    }

    public void setTarget_server_socket(String target_server_socket) {
        this.target_server_socket = target_server_socket;
    }

    public List<String> getIps_anongw() {
        return ips_anongw;
    }

    public void setIps_anongw(List<String> ips_anongw) {
        this.ips_anongw = ips_anongw;
    }

    public HashMap<Client.Id, Client> getClients() {
        return clients;
    }

    public void setClients(HashMap<Client.Id, Client> clients) {
        this.clients = clients;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }

    public boolean getNew_client() {
        return new_client;
    }

    public void setNew_client(boolean new_client) {
        this.new_client = new_client;
    }

    public Condition getNew_client_arrived() {
        return new_client_arrived;
    }

    public void setNew_client_arrived(Condition new_client_arrived) {
        this.new_client_arrived = new_client_arrived;
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }
}
