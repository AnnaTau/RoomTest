package RestTest;

import RestTest.exceptions.ObjectException;
import RestTest.exceptions.RoomClosedException;
import RestTest.exceptions.ServiceException;

/**
 * Created by Tau on 05.09.2015.
 */
public interface Room {
    void open();
    void close();
    Boolean isEmpty() throws RoomClosedException;
    Item getObject() throws RoomClosedException;
    void addObject(Item object) throws RoomClosedException, ObjectException;
    void removeObject() throws RoomClosedException, ObjectException;
    void subscribe(ListenerPath listenerPath) throws ServiceException;
}
