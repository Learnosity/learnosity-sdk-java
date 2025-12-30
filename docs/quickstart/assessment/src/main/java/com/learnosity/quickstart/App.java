package com.learnosity.quickstart;

import learnositysdk.request.Init;
import learnositysdk.request.Uuid;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.json.JSONObject;

public abstract class App
{
    Properties config = new Properties();
    String user_id = Uuid.generate();
    String session_id = Uuid.generate();
    String domain;
    String put;

    public App() {
        loadConfig();
    }

    private void loadConfig() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream iStream = null;
        try {
            iStream = classLoader.getResourceAsStream("config.properties");
            config.load(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (iStream != null) {
                try {
                    iStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    abstract String initOptions(String domain);

    abstract Map<String, String> createSecurityObject(String domain);

    abstract JSONObject createRequestObject();
}
