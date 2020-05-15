package interfaces;

import model.Music;
import java.time.LocalDate;

/**
 * Needed methods for the Store implementation.
 * @author Group_X
 */
public interface InterfaceStore {
    int search(String title);
    int getDownloads(int id);
    Music search(int id);
    String getArtist(int id);
    String getTitle(int id);
    String getGenre(int id);
    LocalDate getDate(int id);
}