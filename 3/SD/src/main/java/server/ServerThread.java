package server;

import static java.lang.System.out;
import static model.Utils.MAX_BUFFER;

import controller.Coisa;
import controller.NotificationBuffer;
import data.DAO;
import model.Music;
import model.OnlineUsers;
import model.ServerLogic;
import model.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ServerThread class manages server side functionality.
 * @author Group_X
 */
public class ServerThread implements Runnable {
    /* Path to server file database */
    private static String PATH_DB = "DB/content_server/";
    private Socket socket;
    private ServerLogic serverLogic;

    private BufferedReader text_inputStream;
    private PrintWriter text_outputStream;
    private DataOutputStream file_outputStream;
    private DataInputStream file_inputStream;
    private OnlineUsers online;
    private User user;
    private NotificationBuffer notification;
    private Thread notification_thread;
    private Coisa coisa;

    ServerThread(Socket socket, OnlineUsers online, DAO dao, NotificationBuffer notification){
        this.socket = socket;
        this.online = online;
        // *******************************************
        this.notification = notification;
        this.coisa = new Coisa(notification);
        notification_thread = new Thread(this.coisa);
        // *******************************************
        this.serverLogic = new ServerLogic(dao.userDAO, dao.musicDAO);
        try
        {
            text_inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            text_outputStream = new PrintWriter(socket.getOutputStream(),true);
            /*  DataOutputStream send files to client!!! (Client downloading from server)
             */
            file_outputStream = new DataOutputStream(socket.getOutputStream());
            file_outputStream.flush();

            /*  DataInputStream receives files from client!!! (Client uploading to server)
             */
            file_inputStream = new DataInputStream(socket.getInputStream());
        }
        catch(IOException e)
        {
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            notification_thread.start();
            String response;
            boolean isRunning = true;
            while(isRunning) {

                if(notification.getString()!= null) {
                    out.println(notification.getString());
                    out.println("notification");
                    text_outputStream.println("notification");
                    text_outputStream.println(notification.getString());
                }
                else  {
                    out.println(notification.getString());
                    out.println("null notification");
                    text_outputStream.println("null");
                }

                response = text_inputStream.readLine();
                switch (response) {
                    case "login": {
                        login();
                        break;
                    }
                    case "logout": {
                        logout();
                        break;
                    }
                    case "register": {
                        register();
                        break;
                    }
                    case "search": {
                        search();
                        break;
                    }
                    case "download": {
                        download();
                        break;
                    }
                    case "upload": {
                        upload();
                        break;
                    }
                    case "exit":
                        isRunning = false;
                        break;
                    default:
                        out.println("default");
                        break;
                }
            }
            /* Streams being closed by server */
            file_inputStream.close();
            text_outputStream.close();
            text_inputStream.close();
            this.socket.close();
            out.println("Client disconnected");
        } catch (IOException e) {e.printStackTrace();}
        catch (NullPointerException e) { out.println("Wrong format given... disconnecting."); }
    }

    private void upload() throws IOException {
        try {
            /* receives metadata */
            String title = text_inputStream.readLine();
            String artist = text_inputStream.readLine();
            int year = Integer.parseInt(text_inputStream.readLine());
            String genre = text_inputStream.readLine();
            String filename = text_inputStream.readLine();
            long fileSize = Long.parseLong(text_inputStream.readLine());
            ArrayList<String> genre_list = new ArrayList<>();
            Collections.addAll(genre_list, genre.split(","));

            if(this.serverLogic.putMusic(title, artist, year, genre_list, PATH_DB + filename))
                text_outputStream.println("ok");
            else
                text_outputStream.println("error");

            this.notification.setString(title);
            this.notification.signal();
            /* receiving file */
            FileOutputStream fos = new FileOutputStream(PATH_DB + filename);
            int bytesRead;
            byte[] buffer = new byte[MAX_BUFFER];
            while (fileSize > 0 && (bytesRead = file_inputStream.read(buffer)) != -1) {
                fileSize -= bytesRead;
                fos.write(buffer, 0, bytesRead);
                fos.flush();
            }
            fos.close();

        } catch (FileNotFoundException e) {e.printStackTrace();}
    }

    private void search() throws IOException{
        String genre = text_inputStream.readLine();
        List<Music> music_list = this.serverLogic.search(genre);
        text_outputStream.println(music_list.size());
        for(Music music :music_list) {
            /* Sending metadata */
            this.text_outputStream.println(music.getId());
            this.text_outputStream.println(music.getTitle());
            this.text_outputStream.println(music.getArtist());
            this.text_outputStream.println(music.getYear());
            this.text_outputStream.println(music.getDownloads());
            String music_genre = "";
            for(String s : music.getGenre())
                music_genre += s +",";
            this.text_outputStream.println(music_genre);
        }
    }

    private void download() throws IOException {
        try {
            Music music = null;
            int musicId = Integer.parseInt(text_inputStream.readLine());
            /* get file class from db if exists */
            music = serverLogic.getMusic(musicId);
            if(music != null) {
                this.text_outputStream.println("ok");

                File file = new File(music.getPath());
                String musicTitle = music.getTitle();
                long fileSize = file.length();
                int music_downloads = music.getDownloads()+1;

                this.serverLogic.increaseDownloads(musicId, music_downloads);
                /* Sending metadata */
                this.text_outputStream.println(musicTitle);
                this.text_outputStream.println(fileSize);
                this.text_outputStream.println(music.getArtist());
                this.text_outputStream.println(music.getYear());
                this.text_outputStream.println(music.getFileName());
                this.text_outputStream.println(music_downloads);
                String genre = "";
                for(String s : music.getGenre())
                    genre += s +",";
                this.text_outputStream.println(genre);

                /* Sending file */
                int bytesRead;
                byte[] buffer = new byte[MAX_BUFFER];
                InputStream inputStream = new FileInputStream(music.getPath());
                while (fileSize > 0 && (bytesRead = inputStream.read(buffer)) != -1) {
                    fileSize -= bytesRead;
                    file_outputStream.write(buffer, 0, bytesRead);
                    file_outputStream.flush();
                }
            } else {
                text_outputStream.println("error");
            }
        } catch (FileNotFoundException e) {e.printStackTrace();}
    }

    private void register() throws IOException {
        String username, password, name;
        boolean answer;
        username = this.text_inputStream.readLine();
        password = this.text_inputStream.readLine();
        name = this.text_inputStream.readLine();
        answer = serverLogic.registerUser(username, password, name);

        if(answer) {
            this.text_outputStream.println("ok");
            this.text_outputStream.println(name);

            online.add(username);
            out.println("Online users:");
            for(String users: online.getSet())
                out.println(users);

            out.println("Client: '" + name +"' logged.");
        }
        else {
            this.text_outputStream.println("error");
        }
    }

    /**
     * Logs out of client account.
     */
    private void logout() {
        boolean exists = false;
        /* Returns username of current logged client */
        String username = this.serverLogic.getUsername();
        /* Checks client username */
        if(!username.equals(""))
            exists = online.remove(username);
        /* Response to username being online or not */
        if(!exists) {
            this.text_outputStream.println("error");
        }
        else {
            this.text_outputStream.println("ok");

            out.println("Online users:");
            for(String users: online.getSet())
                out.println(users);

            out.println("User: '"+username+"' logged out.");
        }
    }

    /**
     * Logs in with a client account.
     * @throws IOException -- In case of wrong input.
     */
    private void login() throws IOException{
        String username, password;
        String answer;
        username = this.text_inputStream.readLine();
        password = this.text_inputStream.readLine();
        // no verification done, because an uSeR cAn HaVe mULTipLe LogS aT tHe SaME TImE.
        // if(!online.contains(username)) {
        answer = serverLogic.loginUser(username, password);
        if(!online.contains(username)) {
            this.text_outputStream.println(answer);
            if (answer.equals("ok")) {
                String nickname = serverLogic.getUserNickname();
                this.text_outputStream.println(nickname);
                online.add(username);
                out.println("Client: '" + nickname + "' logged.");
            }
            out.println("Online users:");
            for (String s : online.getSet())
                out.println(s);
        }
        else {
            this.text_outputStream.println("logged");
            String nickname = serverLogic.getUserNickname();
            this.text_outputStream.println(nickname);
            out.println("Client: '" + nickname + "' logged. 'AGAIN'");

            // out.println("Attempt to login in already logged user: '"+username+"'.");
        }
    }

    /* UNUSED CODE

     * Sends a file to the client.
     * @param outputStream -- Output stream connected to socket.
     * @param fileName -- File name.
     * @throws IOException -- Files.copy(?)

    private void sendToClient(DataOutputStream outputStream, String fileName) throws IOException {
        File file = new File(PATH_DB, fileName);
        long filelen = file.length();

        out.println("File is: " + filelen + " bytes long");
        out.println("Sending: " + fileName);

        text_outputStream.println(fileName);
        text_outputStream.println(filelen);

        // FileOutputStream fos = new FileOutputStream("DB/content/"+ fileName);
        int bytesRead;
        byte[] buffer = new byte[2048];
        InputStream inputStream = new FileInputStream(PATH_DB + fileName);

        while ((bytesRead = inputStream.read(buffer)) != -1 && filelen > 0) {
            filelen -= bytesRead;
            outputStream.write(buffer, 0, bytesRead);
            outputStream.flush();
        }
        inputStream.close();
        // Files.copy(file.toPath(), outputStream);
    }

    private void msg_client(DataOutputStream outputStream, String msg) throws IOException{
        outputStream.writeUTF(msg);
        outputStream.flush();
    }

    void login(DataOutputStream dos, DataInputStream dis) throws IOException{
        System.out.println("Connected!");
        msg_client(dos, "Connected!");
        while(true) {
            // Receives name
            msg_client(dos, "Insert your name!");
            String name = dis.readUTF();
            if (name.equals("")) break;
            // Receives password
            msg_client(dos, "Insert your password!");
            String pwd = dis.readUTF();
            if (pwd.equals("")) break;
            try {
                System.out.println(name + ":" + pwd);
                msg_client(dos, "Successful login");
                msg_client(dos, "Closing Connection...");
                break;
                // send_to_client(pw, "Closing Connection...");
            } catch (NumberFormatException e) {
                System.out.println("Not a number!\nTry again");
            }
        }
    }
    */

}