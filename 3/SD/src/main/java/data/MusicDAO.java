package data;

import model.Music;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class MusicDAO implements Map<String, Music> {

    private ReentrantLock lock;

    /**
     * Empty constructor DAO
     */
    public MusicDAO () {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.lock = new ReentrantLock();
        } catch (ClassNotFoundException e) {

            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        Connection conn;

        try {

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MusicDB?useTimezone=true&serverTimezone=UTC","root","");

            Statement stm = conn.createStatement();

            String sql = "SELECT * FROM Music WHERE idMusic = '"+(Integer)key+"'";

            lock.lock();
            ResultSet rs = stm.executeQuery(sql);
            lock.unlock();

            if (rs.next()) {
                return rs.getInt(1) == ((Integer) key);
            }

            return false;

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Music get(Object key) {

        Connection conn;

        try {

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MusicDB?useTimezone=true&serverTimezone=UTC","root","");

            Music music = null;

            Statement stm = conn.createStatement();

            String sql = "SELECT DISTINCT g.genre from Music m, Music_has_Genre mg, Genre g WHERE mg.genre_name = g.genre and mg.idMusic ="+(Integer)key;

            lock.lock();
            ResultSet rs = stm.executeQuery(sql);


            ArrayList<String> genre = new ArrayList<>();
            while (rs.next()){
                genre.add(rs.getString(1));
            }

            stm = conn.createStatement();

            sql = "SELECT m.* from Music m WHERE m.idMusic = "+(Integer)key;

            rs = stm.executeQuery(sql);
            lock.unlock();

            if(rs.next()) {
                music = new Music(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getInt(6),
                        genre);
            }
            return music;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    @Override
    public Music put(String key, Music value) {
        return null;
    }

    public boolean insertMusic(Music value) {
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MusicDB?useTimezone=true&serverTimezone=UTC","root","");

            Statement stm = conn.createStatement();
            String sql = "INSERT INTO Music (title, artist, year, path, downloads) VALUES ('"+value.getTitle()+"','"+
                                value.getArtist()+"',"+value.getYear()+",'"+value.getPath()+"', 0)";
            boolean result = false;
            lock.lock();
            stm.executeUpdate(sql);

            sql = "select idMusic from Music where title='"+value.getTitle()+
                    "' and artist='"+value.getArtist()+
                    "' and year="+value.getYear()+
                    " and path='"+value.getPath()+
                    "' and downloads = 0";
            ResultSet rs = stm.executeQuery(sql);
            int idMusic = -1;
            if(rs.next()) {
               idMusic =  rs.getInt(1);
            }

            for(String genre : value.getGenre()) {
                sql = "INSERT INTO Genre (genre) SELECT '"+genre+"' WHERE NOT EXISTS (SELECT * FROM Genre where genre='"+genre+"')";
                stm.executeUpdate(sql);
            }
            for(String genre : value.getGenre()) {
                sql = "INSERT INTO Music_has_Genre VALUES ("+idMusic+",'"+genre+"');";
                stm.executeUpdate(sql);
                result = true;
            }
            lock.unlock();
            return result;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public List<Music> search(String genre) {
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MusicDB?useTimezone=true&serverTimezone=UTC","root","");

            Statement stm = conn.createStatement();
            String sql = "CALL search('"+genre+"');";
            lock.lock();
            ResultSet rs = stm.executeQuery(sql);
            ResultSet rs2;
            ArrayList<Music> results = new ArrayList<>();
            while(rs.next()) {
                stm = conn.createStatement();
                sql = "SELECT DISTINCT g.genre from Music m, Music_has_Genre mg, Genre g WHERE mg.genre_name = g.genre and mg.idMusic ="+rs.getInt(1);
                rs2 = stm.executeQuery(sql);
                ArrayList<String> genre_list = new ArrayList<>();
                while (rs2.next()){
                    genre_list.add(rs2.getString(1));
                }
                Music music = new Music(rs.getInt(1),
                                        rs.getString(2),
                                        rs.getString(3),
                                        rs.getInt(4),
                                        rs.getInt(5),
                                        genre_list);
                results.add(music);
            }
            lock.unlock();
            return results;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    public void increaseDownloads(int idMusic, int downloads) {
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MusicDB?useTimezone=true&serverTimezone=UTC","root","");

            Statement stm = conn.createStatement();
            String sql = "UPDATE Music SET downloads = "+ downloads + " where idMusic="+idMusic+";";
            lock.lock();
            stm.executeUpdate(sql);
            lock.unlock();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    @Override
    public Music remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Music> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<Music> values() {
        return null;
    }

    @Override
    public Set<Entry<String, Music>> entrySet() {
        return null;
    }
}
