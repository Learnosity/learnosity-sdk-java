package com.learnosity.quickstart;

import learnositysdk.request.Init;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class App
{
    private Properties config = new Properties();
    private String user_id = "demos-user";
    private String domain;
    private String put;

    public App() {
        loadConfig();
    }

    public String initOptions(String domain) {
        Map<String, String> security = createSecurityObject(domain);
        Map<String, Object> request = createRequestObject();
        String secret = config.getProperty("consumerSecret");
        try {
            Init init = new Init("author", security, secret, request);
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

    // this is working currently as is.
    // need to create other nested stuff as a map like user as an example.
    private Map<String, Object> createRequestObject() {
        var r = new HashMap();
        r.put("mode", "item_list");
        Map<String, Object> user = new HashMap();
        r.put("user", user);
        user.put("id", this.user_id);
        user.put("firstname", "Demos");
        user.put("lastname", "User");
        user.put("email", "demos@learnosity.com");
        Map<String, Object> config = new HashMap();
        r.put("config", config);
        Map<String, Object> item_edit = new HashMap();
        config.put("item_edit", item_edit);
        Map<String, Object> item = new HashMap();
        item_edit.put("item", item);
        Map<String, Object> reference = new HashMap();
        item.put("reference", reference);
        reference.put("show", true);
        reference.put("edit", true);
        item.put("dynamic_content", true);
        item.put("shared_passage", true);
        item.put("enable_audio_recording", true);       
        return r;
    }
}
