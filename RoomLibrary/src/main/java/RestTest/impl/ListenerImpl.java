package RestTest.impl;

import RestTest.IEventListener;
import RestTest.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Tau on 05.09.2015.
 */
public class ListenerImpl implements IEventListener {
    private String url;
    private RestTemplate template;

    public ListenerImpl(String url) {
        this.url = url;
        template = new RestTemplate();
    }

    public void onObjectInRoom(Item object) {
        ResponseEntity<String> response = template.postForEntity(url+"/onObjectInRoom", object, String.class);
        HttpStatus status = response.getStatusCode();
        if (status.is2xxSuccessful()) System.out.println("Объект в комнате " + object);
    }
}
