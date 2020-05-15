package data;

import model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class UserDAO implements Map<String,User> {

    private ReentrantLock lock;

    /**
     * Empty constructor DAO
     */
    public UserDAO () {
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

            String sql = "SELECT * FROM User WHERE username = '"+(String)key+"'";
            lock.lock();
            ResultSet rs = stm.executeQuery(sql);
            lock.unlock();
            if (rs.next()) {
                return rs.getString(1).equals((String) key);
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
    public User get(Object key) {

        Connection conn;

        try {

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MusicDB?useTimezone=true&serverTimezone=UTC","root","");

            User user = null;

            Statement stm = conn.createStatement();

            String sql = "SELECT * FROM User WHERE username='"+(String)key+"'";
            lock.lock();
            ResultSet rs = stm.executeQuery(sql);
            lock.unlock();
            if (rs.next()) {

                user = new User(rs.getString(1), rs.getString(2), rs.getString(3));
            }

            return user;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    @Override
    public User put(String key, User value) {

        Connection conn;

        try {

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MusicDB?useTimezone=true&serverTimezone=UTC","root","");

            User user = new User((String)value.getUsername(), (String)value.getPassword(), (String)value.getName());

            Statement stm = conn.createStatement();
            String sql = "INSERT INTO User VALUES('"+user.getUsername()+"','"
                                                    +user.getPassword()+"','"
                                                    +user.getName()+"');";
            lock.lock();
            stm.executeUpdate(sql);
            lock.unlock();
            return user;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    @Override
    public User remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends User> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<User> values() {
        return null;
    }

    @Override
    public Set<Entry<String, User>> entrySet() {
        return null;
    }
}
