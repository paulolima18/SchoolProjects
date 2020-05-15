package model;

import interfaces.InterfaceStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The class Store implements a music library.
 * @author Group_X
 */
public class Store implements InterfaceStore {

    private Map<Integer, Music> library = new HashMap<Integer, Music>();
    public Store() {
        Music music = new Music(0, "Fade To Black", "Metalica", 1974, "DB/contet_server/Metallica_Fade-to-black.mp3", new ArrayList<>());
        library.put(0, music);
    }
    @Override
    public int search(String title) {
        return 0;
    }

    @Override
    public int getDownloads(int id) {
        return 0;
    }

    @Override
    public Music search(int id) {
        return library.get(id);
    }

    @Override
    public String getArtist(int id) {
        return library.get(id).getArtist();
    }

    @Override
    public String getTitle(int id) {
        return null;
    }

    @Override
    public String getGenre(int id) {
        return null;
    }

    @Override
    public LocalDate getDate(int id) {
        return null;
    }
}
