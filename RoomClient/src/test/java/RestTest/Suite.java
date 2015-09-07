package RestTest;

import RestTest.exceptions.ObjectException;
import RestTest.exceptions.RoomClosedException;
import RestTest.exceptions.ServiceException;
import RestTest.impl.ListenerImpl;
import RestTest.impl.RoomImpl;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by Tau on 06.09.2015.
 */
public class Suite {
    static BootStarter starter;
    static Room room;

    @BeforeClass
    public static void init(){
        starter = new BootStarter();
        starter.start();
        room = new RoomImpl("http://localhost:8080");
    }

    @Test
    public void removeTest(){
        room.open();
        boolean empty = false;
        try {
            empty = room.isEmpty();
        } catch (RoomClosedException e) {
            e.printStackTrace();
        }
        if (!empty){
            try {
                room.removeObject();
            } catch (RoomClosedException e) {
                e.printStackTrace();
            } catch (ObjectException e) {
                e.printStackTrace();
            }
        }
        boolean onError = false;
        try {
            room.removeObject();
        } catch (RoomClosedException e) {
            fail("В комнате должен быть обьект");
        } catch (ObjectException e) {
            onError = true;
            assertEquals(e.getMessage(), "В комнате нет обьекта");
        }
        assertTrue(onError);
        room.close();
    }

    @Test
    public void checkEmpty(){
        room.close();
        boolean onError = false;
        try {
            room.isEmpty();
        } catch (RoomClosedException e) {
            onError = true;
            assertEquals(e.getMessage(), "Нельзя проверить наличие обьекта, если дверь закрыта");
        }
        assertTrue(onError);
    }

    @Test
    public void addObject(){
        room.close();
        boolean onError = false;
        try {
            room.addObject(new Item("1", "New Item"));
        } catch (RoomClosedException e) {
            onError = true;
            assertEquals(e.getMessage(), "Нельзя добавить обьект, если дверь закрыта");
        } catch (ObjectException e) {
            System.out.println(e.getMessage());
            fail();
        }
        assertTrue(onError);
        onError = false;
        room.open();
        try {
            room.addObject(null);
        } catch (RoomClosedException e) {
            fail("Комната должна быть открыта");
        } catch (ObjectException e) {
            onError = true;
            assertEquals(e.getMessage(), "Нельзя добавить пустой обьект");
        }
        assertTrue(onError);
        try {
            room.addObject(new Item("2", "Two"));
        } catch (RoomClosedException e) {
            fail("Комната должна быть открыта");
        } catch (ObjectException e) {
            fail("Объект должен добавиться в комнату");
        }
        try {
            room.removeObject();
        } catch (RoomClosedException e) {
            e.printStackTrace();
        } catch (ObjectException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void subscribe(){
        room.open();
        boolean onError = false;
        try {
            room.subscribe(null);
        } catch (ServiceException e) {
            onError = true;
            assertEquals(e.getMessage(), "Пустой url");
        }
        assertTrue(onError);
        try {
            room.subscribe(new ListenerPath("http://localhost:8081"));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        boolean empty = false;
        try {
            empty = room.isEmpty();
        } catch (RoomClosedException e) {
            e.printStackTrace();
        }
        Item item = new Item("key", "Name");
        if (empty){
            try {
                room.addObject(item);
            } catch (RoomClosedException e) {
                e.printStackTrace();
            } catch (ObjectException e) {
                e.printStackTrace();
            }
        }
        ListenerController bean = starter.context.getBean(ListenerController.class);
        assertEquals(item, bean.getItem());
        try {
            room.removeObject();
        } catch (RoomClosedException e) {
            e.printStackTrace();
        } catch (ObjectException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void close(){
        starter.tearDown();
    }
}
