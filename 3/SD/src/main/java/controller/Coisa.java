package controller;

public class Coisa implements Runnable {
    NotificationBuffer notification;

    public Coisa(NotificationBuffer notification) {
        this.notification = notification;
    }
    @Override
    public void run() {
        notification.on++;
        notification.start();
        notification.on--;
    }
}
