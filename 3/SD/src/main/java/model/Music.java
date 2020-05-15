package model;

import java.util.ArrayList;
import java.util.List;

/**
 * The class Music defines a music file and its metadata for the App.
 * @author Group_X
 */
public class Music {

    private final int id;
    private final String title;
    private final String artist;
    private final int year;
    private final List<String> genre;
    private final String path;
    private int downloads;

    public Music(int id, String title, String artist, int year, String path, int downloads, List<String> genre) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.artist = artist;
        this.genre = genre;
        this.path = path;
        this.downloads = downloads;
    }

    public Music(int id, String title, String artist, int year, int downloads, List<String> genre) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.artist = artist;
        this.genre = genre;
        this.path = null;
        this.downloads = downloads;
    }

    public Music(int id, String title, String artist, int year, String path, List<String> genre) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.genre = genre;
        this.year = year;
        this.downloads = 0;
    }

    public List<String> getGenre() {
        return new ArrayList<>(this.genre);
    }

    public String getFileName() {
        return this.path.split("/")[2];
    }

    public int getDownloads() {
        return this.downloads;
    }

    public int getId() {
        return this.id;
    }
    public int getYear() {
        return this.year;
    }

    public String getArtist() {
        return this.artist;
    }
    public String getTitle() {
        return this.title;
    }
    public String getPath() {
        return this.path;
    }
    public Music clone(){
        return new Music(this.id, this.title, this.artist, this.year, this.path, this.downloads, this.genre);
    }
    /*
    public byte[] serializeData () throws IOException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( stream );

        out.writeBoolean(isHappy);
        out.writeShort( slope );

        // Serialize bar which will just append to this byte stream
        bar.doSerializeData(out);

        // Return the serialized object.
        byte[] data = stream.toByteArray();

        // Clean up.
        stream.close();

        return data;
    } */
}