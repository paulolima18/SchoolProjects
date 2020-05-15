package exceptions;

/**
 * Could not login exception.
 * @author Group_X
 */
public class CouldNotLogin extends Exception {
    public CouldNotLogin()
    {
        super("Login information invalid!");
    }
}
