package server;

import client.View;
import controller.NotificationBuffer;
import data.DAO;
import model.OnlineUsers;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listening...
 * @author Group_X
 */
public class Server {
    public static void main(String[] args) throws Exception {
        View.clearScreen();
        ServerSocket ss = new ServerSocket(12345);
        OnlineUsers online = new OnlineUsers();
        DAO dataAccessObject = new DAO();
        NotificationBuffer notification = new NotificationBuffer();
        System.out.println("Listening...");
        while(true){
            Socket socket = ss.accept();
            Thread request = new Thread(new ServerThread(socket, online, dataAccessObject, notification));
            request.start();
        }
    }
}