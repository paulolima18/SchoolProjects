package model;

import java.util.HashSet;

public class OnlineUsers {
    private HashSet<String> online;

    public OnlineUsers() {
        online = new HashSet<>();
    }

    public synchronized boolean contains(String username) {
        return online.contains(username);
    }

    public synchronized boolean remove(String username) {
        return online.remove(username);
    }

    public synchronized HashSet<String> getSet() {
        return new HashSet<>(online);
    }

    public synchronized void add(String username) {
        online.add(username);
    }
}
