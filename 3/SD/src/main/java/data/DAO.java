package data;

/**
 * Data Access Objects...
 * @author Group_X
 */
public class DAO {
    public MusicDAO musicDAO;
    public UserDAO userDAO;

    public DAO(){
        this.musicDAO = new MusicDAO();
        this.userDAO = new UserDAO();
    }
}
