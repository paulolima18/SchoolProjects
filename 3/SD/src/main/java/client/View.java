package client;

import controller.Controller;
import model.Music;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import static java.lang.System.out;
import static model.Utils.OFFLINE;
import static model.Utils.ONLINE;

public class View
{
    private Scanner sc;
    private Controller controller;

    public View(Controller controller)
    {
        this.controller = controller;
        this.sc = new Scanner(System.in);
    }

    public void run() {
        logo();
        help();
        String response;
        do {
            if(!controller.getNotification().equals("null"))
                out.println("This awesome music was just uploaded:"+ controller.printNotification());

            response = command_reader();
            String[] list = response.split(" ");
            clearScreen();
            switch (list[0].toLowerCase()) {
                case "login":
                    /* Syntax: login */
                    if(controller.getStatus())
                        out.println("Already logged in");
                    else
                        login();
                    break;
                case "logout":
                    /* Syntax: logout */
                    logout();
                    break;
                case "register":
                    /* Syntax: register */
                    if(controller.getStatus())
                        out.println("Logout first!");
                    else
                        register();
                    break;
                case "search":
                    /* Syntax: search */
                    if(controller.getStatus())
                        search();
                    else
                        out.println("Login first!");
                    break;
                case "download":
                    /* Syntax: download */
                    if(controller.getStatus())
                        download();
                    else
                        out.println("Login first!");
                    break;
                case "upload":
                    /* Syntax: upload */
                    if(controller.getStatus())
                        upload();
                    else
                        out.println("Login first!");
                    break;
                case "exit":
                    if(controller.getStatus())
                        logout();
                    controller.exit();
                    break;
                case "help":
                    help();
                    break;
                default:
                    unknown_cmd();
            }
        }while(!(response).equals("exit"));
    }

    private void upload() {
        out.print("Title: ");
        String title = sc.nextLine();
        out.print("Artist/s: ");
        String artist = sc.nextLine();
        out.print("Song year: ");
        int year = Integer.parseInt(sc.nextLine());
        out.print("Genre (Syntax: Rock,Pop): ");
        String genre = sc.nextLine();
        out.print("File name (syntax: example.mp3): ");
        String path = sc.nextLine();
        try {
            if(controller.upload(title, artist, year, genre, path))
                out.println("Upload successful!");
            else
                out.println("Incorrect input...");
        } catch (IOException e) {e.printStackTrace();}

    }

    private void search() {
        String genre = client_searchGenre();
        try {
            List<Music> list_musics = controller.search(genre);
            if(list_musics != null) {
                out.println("Search results:");
                for(Music music : list_musics) {
                    out.println("ID: "+music.getId() +"; " +
                                "Title: "+music.getTitle()+"; "+
                                "Artist/s: "+music.getArtist()+"; "+
                                "Year: "+music.getYear()+"; "+
                                "Genre/s: "+music.getGenre()+"; "+
                                "Downloads: "+music.getDownloads()+";");
                }
            }else
                out.println("No results found!");
        } catch (IOException e) {e.printStackTrace();}
    }

    private void download() {
        String musicId = client_musicId();
        try {
            if(controller.download(musicId)) {
                out.println("Music downloaded successfully");
                Music music = controller.getMusic(Integer.parseInt(musicId));
                out.println("| Music info |");
                out.println("ID: "+music.getId());
                out.println("Title: "+music.getTitle());
                out.println("Artist/s: "+music.getArtist());
                out.println("Year: "+music.getYear());
                out.println("Genre/s: "+music.getGenre());
                out.println("Downloads: "+music.getDownloads());
            }
            else
                out.println("Music ID not found.");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void register(){
        String username = client_username();
        String password = client_password();
        String name = client_name();
        clearScreen();
        boolean registered;
        try {
            registered = controller.register(username, password, name);
            if(registered) {
                controller.setStatus(ONLINE);
                out.println("Account created!\nWelcome to Distributed Sounds: '" + controller.getNickname()+"'");
            }
            else {
                out.println("Username: " + username + " already exists...");
            }
        } catch (IOException e) { out.println("Incorrect register syntax..."); }
    }

    private void logout() {
        try {
            if (controller.logout()) {
                controller.setStatus(OFFLINE);
                out.println("Logged out!");
            }
            else
                out.println("Login first!");
        } catch (IOException e) { out.println("Error during logout..."); }
    }

    private void login(){
        String username = client_username();
        String password = client_password();
        clearScreen();
        String result;
        try {
            result = controller.login(username, password);
            switch (result) {
                case "ok":
                    controller.setStatus(ONLINE);
                    out.println("Welcome to Distributed Sounds: '" + controller.getNickname()+"'");
                    break;
                case "logged":
                    controller.setStatus(ONLINE);
                    out.println("Already online elsewhere, logged in eitherway");
                    out.println("Welcome to Distributed Sounds: '" + controller.getNickname()+"'");
                    break;
                case "wrong":
                    out.println("Wrong username");
                    break;
                case "empty":
                    out.println("Username: " + username + " doesn't exist...");
                    break;
                default:
                    out.println("Error logging");
                    break;
            }
        } catch (IOException e) { out.println("Incorrect login syntax..."); }
    }

    public void logo()
    {
        System.out.println("\n" +
                "______ _     _        _ _           _           _   _____                       _     \n" +
                "|  _  (_)   | |      (_) |         | |         | | /  ___|                     | |    \n" +
                "| | | |_ ___| |_ _ __ _| |__  _   _| |_ ___  __| | \\ `--.  ___  _   _ _ __   __| |___ \n" +
                "| | | | / __| __| '__| | '_ \\| | | | __/ _ \\/ _` |  `--. \\/ _ \\| | | | '_ \\ / _` / __|\n" +
                "| |/ /| \\__ \\ |_| |  | | |_) | |_| | ||  __/ (_| | /\\__/ / (_) | |_| | | | | (_| \\__ \\\n" +
                "|___/ |_|___/\\__|_|  |_|_.__/ \\__,_|\\__\\___|\\__,_| \\____/ \\___/ \\__,_|_| |_|\\__,_|___/\n" +
                "                                                                                      \n" );
    }

    public String command_reader()
    {
        out.print("> ");
        return sc.nextLine();
    }

    public String client_searchGenre()
    {
        out.print("Music genre: ");
        return sc.nextLine();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public String client_musicId()
    {
        out.print("Music ID: ");
        return sc.nextLine();
    }

    public String client_username()
    {
        out.print("Username : ");
        return sc.nextLine();
    }

    public String client_password()
    {
        out.print("Password : ");
        return sc.nextLine();
    }

    public String client_name()
    {
        out.print("Name : ");
        return sc.nextLine();
    }

    // ----------------------------------------- Commands ----------------------------------------------------------- //

    public void help()
    {
        out.println("========= COMMANDS =========");
        String[] commands = { "Login","Register","Search","Download","Upload","Logout"};
        for(String cmd : commands)
            out.println("- " + cmd);
        out.println("============================");
    }



    public void unknown_cmd()
    {
        out.println(" - command not found - ");
    }
}