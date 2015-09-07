package RestTest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Tau on 05.09.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class ListenerPath {
    private String url;

    public ListenerPath(String url) {
        this.url = url;
    }

    public ListenerPath() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
