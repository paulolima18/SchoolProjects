package model;

import data.MusicDAO;
import data.UserDAO;

import java.util.List;

public class ServerLogic {

    private UserDAO userDB;
    private MusicDAO musicDB;
    private User user;

    public ServerLogic(UserDAO userDAO, MusicDAO musicDAO) {
        this.userDB = userDAO;
        this.musicDB = musicDAO;
        this.user = new User();
    }

    public ServerLogic() {
        this.userDB = new UserDAO();
        this.musicDB = new MusicDAO();
        this.user = new User();
    }

    public User getUser() {
        return user.clone();
    }

    public Music getMusic(int musicId) {
        Music music = null;
        if (this.musicDB.containsKey(musicId)) {
            music = this.musicDB.get(musicId);
        }
        return music;
    }

    public boolean putMusic(String title, String artist, int year, List<String> genre_list, String path) {
        boolean result = false;
        if(this.musicDB.insertMusic(new Music(-1, title, artist, year, path, genre_list)))
            result = true;
        return result;
    }

    public List<Music> search(String genre) {
        return this.musicDB.search(genre);
    }

    public void increaseDownloads(int idMusic, int downloads) {
        musicDB.increaseDownloads(idMusic, downloads);
    }

    public String loginUser(String username, String password) {

        String loginResult;

        if (this.userDB.containsKey(username)) {
            this.user = this.userDB.get(username);
            if (this.user.verifyCredentials(password)) loginResult = "ok";
            else loginResult = "wrong";

        } else {
            loginResult = "empty";
        }
        return loginResult;
    }

    public boolean registerUser(String username, String password, String name) {
        boolean registerResult;
        if (this.userDB.containsKey(username)) {
            registerResult = false;
        } else {
            this.user = this.userDB.put(username, new User(username, password, name));
            registerResult = true;
        }
        return registerResult;
    }

    public String getUsername() {
        return this.user.getUsername();
    }
    public String getUserNickname() {
        return this.user.getName();
    }
}
