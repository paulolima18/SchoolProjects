package exceptions;

/**
 * Music not found exception.
 * @author Group_X
 */
public class MusicNotFound extends NullPointerException {
    MusicNotFound(int id) {
        super("Music with id:"+ id+ " not found!");
    }
}
