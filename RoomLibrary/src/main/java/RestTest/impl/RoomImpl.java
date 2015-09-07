package RestTest.impl;

import RestTest.Item;
import RestTest.ListenerPath;
import RestTest.Room;
import RestTest.exceptions.ObjectException;
import RestTest.exceptions.RoomClosedException;
import RestTest.exceptions.ServiceException;
import com.sun.istack.internal.Nullable;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Tau on 05.09.2015.
 */
public class RoomImpl implements Room {
    private String url;
    private RestTemplate template;
    private HttpStatus lastStatus;
    private String err;

    public RoomImpl(String url) {
        this.url = url;
        template = new RestTemplate();
        template.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return clientHttpResponse.getStatusCode()!=HttpStatus.OK;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                lastStatus=clientHttpResponse.getStatusCode();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                IOUtils.copy(clientHttpResponse.getBody(), bos);
                err=new String(bos.toByteArray());
            }
        });
    }

    public void open() {
        ResponseEntity responseEntity = template.postForEntity(url+"/open", null, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if (status.is2xxSuccessful()) System.out.println("Комната открыта");
        else throw new RuntimeException("Error from service "+status.value());
    }

    public void close() {
        ResponseEntity responseEntity = template.postForEntity(url+"/close", null, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if (status.is2xxSuccessful()) System.out.println("Комната закрыта");
        else throw new RuntimeException("Error from service "+status.value());
    }

    public Boolean isEmpty() throws RoomClosedException {
        ResponseEntity responseEntity = template.postForEntity(url+"/isempty", null, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if (status.is2xxSuccessful()) {
            System.out.println("Ответ пришёл");
            return new Boolean(responseEntity.getBody().toString());
        }
        if (status.value()==400) {
            throw new RoomClosedException(err);
        }
        else throw new RuntimeException("Error from service "+status.value());
    }

    public Item getObject() throws RoomClosedException {
        ResponseEntity responseEntity = null;
        HttpStatus status=null;
        String error=null;
        try {
            responseEntity = template.postForEntity(url + "/getobject", null, Item.class);
            status = responseEntity.getStatusCode();
        } catch (Exception e){
            status=lastStatus;
            error=err;
        }
        if (status.is2xxSuccessful()) {
            System.out.println("Ответ пришёл");
            if (responseEntity.getBody() == null) return null;
            return (Item) responseEntity.getBody();
        }
        if (status.value() == 400) {
            System.out.println(responseEntity.getBody());
            throw new RoomClosedException(err);
        } else throw new RuntimeException("Error from service " + status.value());
    }

    public void addObject(Item object) throws RoomClosedException, ObjectException {
        ResponseEntity responseEntity = template.postForEntity(url+"/addobject", object, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if (status.is2xxSuccessful()) System.out.println("Ответ пришёл");
        else if (status.value()==400) {
            System.out.println(err);
            if (err.equals("Нельзя добавить обьект, если дверь закрыта")){
                throw new RoomClosedException(err);
            }
            else throw new ObjectException(err);
        }
        else throw new RuntimeException("Error from service "+status.value());
    }

    public void removeObject() throws RoomClosedException, ObjectException {
        ResponseEntity responseEntity = template.postForEntity(url + "/removeobject", null, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if (status.is2xxSuccessful()) System.out.println("Обьект удалён");
        else if (status.value()==400){
            System.out.println(err);
            if (err.equals("Нельзя удалить обьект, если дверь закрыта")){
                throw new RoomClosedException(err);
            }
            else throw new ObjectException(err);
        }
        else throw new RuntimeException("Error from service "+status.value());
    }

    public void subscribe(ListenerPath listenerPath) throws ServiceException {
        ResponseEntity responseEntity = template.postForEntity(url+"/subscribe", listenerPath, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        if (status.is2xxSuccessful()) System.out.println("Подписка оформлена");
        else if (status.value()==400) throw new ServiceException(err);
        else throw new RuntimeException("Error from service "+status.value());
    }
}
