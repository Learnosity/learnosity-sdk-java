package com.learnosity.quickstart;

import learnositysdk.request.Init;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class App
{
    private Properties config = new Properties();
    private UUID user_id = UUID.randomUUID();
    private UUID session_id = UUID.randomUUID();
    private String domain;
    private String put;

    public App() {
        loadConfig();
    }

    public String initOptions(String domain) {
        Map<String, String> security = createSecurityObject(domain);
        Map<String, String> request = createRequestObject();
        String secret = config.getProperty("consumerSecret");
        try {
            Init init = new Init("items", security, secret, request);
            return init.generate();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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

    private Map<String, String> createSecurityObject(String domain) {
        var s = new HashMap();
        s.put("domain", domain);
        s.put("consumer_key", config.getProperty("consumer"));
        return s;
    }

    private Map<String, String> createRequestObject() {
        var r = new HashMap();
        r.put("user_id", this.user_id);
        r.put("activity_template_id", "quickstart_examples_activity_template_001");
        r.put("session_id", this.session_id);
        r.put("activity_id", "quickstart_examples_activity_001");
        r.put("rendering_type", "assess");
        r.put("type", "submit_practice");
        r.put("name", "Items API Quickstart");
        r.put("state", "initial");
        return r;
    }
}
