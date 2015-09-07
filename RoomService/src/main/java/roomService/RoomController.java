package roomService;

import RestTest.IEventListener;
import RestTest.Item;
import RestTest.ListenerPath;
import RestTest.Room;
import RestTest.exceptions.ObjectException;
import RestTest.exceptions.RoomClosedException;
import RestTest.exceptions.ServiceException;
import RestTest.impl.ListenerImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by Tau on 05.09.2015.
 */
@RestController
@Scope("singleton")
public class RoomController implements Room {
    boolean doorOpen;
    Item item;
    ArrayList<IEventListener> listeners = new ArrayList<>();

    @RequestMapping(value = "/open",method = {RequestMethod.POST})
    public void open(){
        if (!doorOpen) doorOpen = true;
        System.out.println("Дверь открыта");
    }

    @RequestMapping(value = "/close",method = {RequestMethod.POST})
    public void close(){
        if (doorOpen) doorOpen = false;
        System.out.println("Дверь закрыта");
    }

    @RequestMapping(value = "/isempty",method = {RequestMethod.POST})
    public Boolean isEmpty() throws RoomClosedException {
        if (!doorOpen) throw new RoomClosedException("Нельзя проверить наличие обьекта, если дверь закрыта");
        if (item==null) return Boolean.TRUE;
        else return Boolean.FALSE;
    }

    @RequestMapping(value = "/getobject",method = {RequestMethod.POST})
    public Item getObject() throws RoomClosedException {
        if (!doorOpen) throw new RoomClosedException("Нельзя взять обьект, если дверь закрыта");
        return item;
    }

    @RequestMapping(value = "/addobject",method = {RequestMethod.POST})
    public void addObject(@RequestBody Item object) throws RoomClosedException, ObjectException {
        if (!doorOpen) throw new RoomClosedException("Нельзя добавить обьект, если дверь закрыта");
        if (object==null) throw new ObjectException("Нельзя добавить пустой обьект");
        if (item!=null) throw new ObjectException("В комнате уже существует объект");
        item = object;
        System.out.println("Now item is "+item);
        for (IEventListener l: listeners){
            try {
                l.onObjectInRoom(item);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/removeobject",method = {RequestMethod.POST})
    public void removeObject() throws RoomClosedException, ObjectException {
        if (!doorOpen) throw new RoomClosedException("Нельзя удалить обьект, если дверь закрыта");
        if (item==null) throw new ObjectException("В комнате нет обьекта");
        item=null;
        System.out.println("Объект удалён");
    }

    @RequestMapping(value = "/subscribe",method = {RequestMethod.POST})
    public void subscribe(@RequestBody ListenerPath listenerPath) throws ServiceException {
        if (listenerPath.getUrl()==null) throw new ServiceException("Пустой url");
        listeners.add(new ListenerImpl(listenerPath.getUrl()));
        System.out.println("Подписка оформлена на адрес " + listenerPath.getUrl());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public String handleException(Exception e,HttpServletResponse resp) {
        System.out.println(e.getMessage());
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return e.getMessage();
    }

    @ExceptionHandler(ObjectException.class)
    @ResponseBody
    public String handleObjectException(Exception e,HttpServletResponse resp) {
        System.out.println(e.getMessage());
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return e.getMessage();
    }

    @ExceptionHandler(RoomClosedException.class)
    @ResponseBody
    public String handleRoomClosedException(Exception e,HttpServletResponse resp) {
        System.out.println(e.getMessage());
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return e.getMessage();
    }
}
