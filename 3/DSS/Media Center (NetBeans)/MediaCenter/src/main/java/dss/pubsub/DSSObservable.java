
package dss.pubsub;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jfc
 */
public class DSSObservable {
    private List<DSSObserver> observers;
    
    public DSSObservable() {
        this.observers = new ArrayList<>();
    }
    
    public void addObserver(DSSObserver o) {
        this.observers.add(o);
    }
    
    public void notifyObservers(Object value) {
        this.observers.forEach(o -> o.update(this, value));
    }
}
