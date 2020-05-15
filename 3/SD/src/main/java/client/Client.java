package client;

import controller.Controller;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Client class handles client side functionality.
 * @author Group_X
 */
public class Client {
    /* Path to client memory */
    public static void main(String[] args) {
        View.clearScreen();
        try {
            Socket s = new Socket("localhost", 12345);
            Controller controller = new Controller(s);
            View view = new View(controller);
            view.run();
            s.close();
        } catch (ConnectException e) { System.out.println("Server offline.");}
        catch (IOException e) { e.printStackTrace();}
    }

}
