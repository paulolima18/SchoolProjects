package controller;

public class NotificationBuffer {

    private String music_title;
    private int used;
    public int on;

    public NotificationBuffer() {
        this.used = 0;
        this.on = 0;
        this.music_title = null;
    }

    public synchronized void setString(String music_name){
        this.music_title = music_name;
    }

    public synchronized String getString(){
        return this.music_title;
    }

    public synchronized void start() {
        try {
            while(true) {
                this.wait();
                // System.out.println(music_title);
                if(used == on) {
                    setString(null);
                    used = 0;
                }
                this.used++;
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    public synchronized void signal() {
        this.notifyAll();
    }
}