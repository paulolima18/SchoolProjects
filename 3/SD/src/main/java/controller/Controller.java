package controller;

import model.Music;
import model.User;

import java.io.*;
import java.net.Socket;
import java.util.*;

import static model.Utils.*;

public class Controller {

    private static String PATH_DB = "DB/content/";
    private User user;
    private boolean status;
    private HashMap<Integer, Music> musics;
    private BufferedReader stdin;
    private BufferedReader text_inputStream;
    private PrintWriter text_outputStream;
    private DataOutputStream file_outputStream;
    private DataInputStream file_inputStream;

    public Controller(Socket s) throws IOException {
        /* Standard Input */
        this.stdin = new BufferedReader(new InputStreamReader(System.in));
        this.status = OFFLINE;
        this.musics = new HashMap<>();
        /* Channel Input / Output */
        this.text_inputStream = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.text_outputStream = new PrintWriter(s.getOutputStream(),true); /* Remove auto-flush later */

        /*  DataOutputStream send files to server!!! (Uploads)
         -> Current declaration is not being used.
         */
        file_outputStream = new DataOutputStream(s.getOutputStream());
        file_outputStream.flush();

        /*  DataInputStream receives files from server. (Downloads)
         */
        file_inputStream = new DataInputStream(s.getInputStream());
    }

    public String getNotification() {
        try {
            return text_inputStream.readLine();
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }

    public String printNotification() {
        try {
            return text_inputStream.readLine();
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }

    public boolean upload(String title, String artist, int year, String genre, String filename) throws IOException {
        boolean result = false;
        File file = new File(PATH_DB+filename);
        /* send code to server to upload */
        text_outputStream.println(UPLOAD);
        /* file exists/not */
        if(file.exists() && file.isFile()) {
            long fileSize = file.length();

            /* sending metadata */
            text_outputStream.println(title);
            text_outputStream.println(artist);
            text_outputStream.println(year);
            text_outputStream.println(genre);
            text_outputStream.println(filename);
            text_outputStream.println(fileSize);

            if(text_inputStream.readLine().equals("ok")) {
                /* Sending file */
                int bytesRead;
                byte[] buffer = new byte[MAX_BUFFER];
                InputStream inputStream = new FileInputStream(PATH_DB + filename);
                while (fileSize > 0 && (bytesRead = inputStream.read(buffer)) != -1) {
                    fileSize -= bytesRead;
                    file_outputStream.write(buffer, 0, bytesRead);
                    file_outputStream.flush();
                }
                result = true;
            }
        }
        return result;
    }

    public boolean download(String musicId) throws IOException {
        boolean result = false;
        /* send code to server to download */
        text_outputStream.println(DOWNLOAD);
        /* send file id */
        text_outputStream.println(musicId);
        /* receive response: file exists/not in server */
        String response = text_inputStream.readLine();
        if(response.equals("ok")) {
            result = true;
            int musicID_int =  Integer.parseInt(musicId);
            /* Receives music title */
            String musicTitle = text_inputStream.readLine();
            /* Receives file size */
            long size_long = Long.parseLong(text_inputStream.readLine());
            /* Receives artist/s name */
            String artist = text_inputStream.readLine();
            /* Song year */
            int year = Integer.parseInt(text_inputStream.readLine());
            /* File name */
            String filename = text_inputStream.readLine();
            /* Amount of downloads */
            int amount_downloads = Integer.parseInt(text_inputStream.readLine());
            /* Music genre/s */
            String genre = text_inputStream.readLine();
            ArrayList<String> genre_list = new ArrayList<>();
            Collections.addAll(genre_list, genre.split(","));
            musics.put(musicID_int, new Music(musicID_int, musicTitle, artist, year, PATH_DB+filename, amount_downloads, genre_list));

            FileOutputStream fos = new FileOutputStream(PATH_DB + filename);
            int bytesRead;
            byte[] buffer = new byte[MAX_BUFFER];
            while (size_long > 0 && (bytesRead = file_inputStream.read(buffer)) != -1) {
                size_long -= bytesRead;
                fos.write(buffer, 0, bytesRead);
                fos.flush();
            }
            fos.close();
        }
        return result;
    }

    public void setStatus(boolean status) { this.status = status; }

    public boolean getStatus() { return this.status; }

    public Music getMusic(int musicId) {
        return musics.get(musicId).clone();
    }

    public List<Music> search(String genre) throws IOException {
        List<Music> list_musics = null;
        /* send code to server to login */
        text_outputStream.println(SEARCH);
        /* genre to search for */
        text_outputStream.println(genre);
        /* amount of musics found */
        int musics_found = Integer.parseInt(text_inputStream.readLine());
        if(musics_found > 0) {
            list_musics = new ArrayList<>();
            while(musics_found > 0) {
                int musicID_int =  Integer.parseInt(text_inputStream.readLine());
                /* Receives music title */
                String musicTitle = text_inputStream.readLine();
                /* Receives artist/s name */
                String artist = text_inputStream.readLine();
                /* Song year */
                int year = Integer.parseInt(text_inputStream.readLine());
                /* Amount of downloads */
                int amount_downloads = Integer.parseInt(text_inputStream.readLine());
                /* Music genre/s */
                String genre_response = text_inputStream.readLine();
                ArrayList<String> genre_list = new ArrayList<>();
                Collections.addAll(genre_list, genre_response.split(","));
                Music music = new Music(musicID_int, musicTitle, artist, year, amount_downloads, genre_list);
                list_musics.add(music);
                musics_found--;
            }
        }
        return list_musics;
    }

    public String login(String username, String password) throws IOException {
        String response;
        /* send code to server to login */
        text_outputStream.println(LOGIN);
        /* send username and password */
        text_outputStream.println(username);
        text_outputStream.println(password);
        /* receive response */
        response = text_inputStream.readLine();
        if(response.equals("ok") || response.equals("logged"))
            setUser(username);
        return response;
    }

    public boolean register(String username, String password, String name) throws IOException {
        /* send code to server to register */
        text_outputStream.println(REGISTER);
        /* send username, password, and name */
        text_outputStream.println(username);
        text_outputStream.println(password);
        text_outputStream.println(name);
        /* receive response */
        String response = text_inputStream.readLine();
        boolean registered = response.equals("ok");
        if(registered)
            setUser(username);
        return registered;
    }

    public boolean logout() throws IOException {
        boolean result = false;
        /* send code to server to logout */
        text_outputStream.println(LOGOUT);
        if(text_inputStream.readLine().equals("ok")) {
            result = true;
            this.user = new User();
        }
        return result;
    }

    private void setUser(String username) throws IOException {
        this.user = new User(username, "null", text_inputStream.readLine());
    }
    public void exit() {
        /* send code to server to exit */
        text_outputStream.println(EXIT);
    }
    public String getNickname() {
        return user.getName();
    }
}
