package RestTest;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tau on 05.09.2015.
 */
@Scope("singleton")
@RestController
public class ListenerController implements IEventListener {
    private Item item;
    @RequestMapping(value = "/onObjectInRoom",method = {RequestMethod.POST})
    public void onObjectInRoom(@RequestBody Item object) {
        item = object;
        System.out.println("Обьект добавлен в комнату");
    }

    public Item getItem() {
        return item;
    }
}
