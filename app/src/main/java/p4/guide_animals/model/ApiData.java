package p4.guide_animals.model;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Alex on 30.10.2016.
 */

public class ApiData {
    public final String clientId;
    public final String host;
    public final String api_request;
    public final String api_process;

    private ApiData(String clientId, String host, String api_request, String api_process) {
        this.clientId = clientId;
        this.host = host;
        this.api_request = api_request;
        this.api_process = api_process;
    }

    public static ApiData getFromProperties(Context context) {
        Properties prop = loadProperties(context);
        return new ApiData(
                prop.getProperty("client_id"),
                prop.getProperty("host"),
                prop.getProperty("api_request"),
                prop.getProperty("api_process"));
    }

    private static Properties loadProperties(Context context) {
        InputStream is = null;
        try {
            is = context.getAssets().open("app.properties");
            Properties prop = new Properties();
            prop.load(is);
            return prop;
        } catch (IOException e) {
            throw new IllegalStateException("no properties file found", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // does nothing
                }
            }
        }
    }
}
