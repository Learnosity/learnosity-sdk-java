package com.learnosity.quickstart;

import learnositysdk.request.Init;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class ItemsApp extends App
{
    public String initOptions(String domain) {
        Map<String, String> security = createSecurityObject(domain);
        JSONObject request = createRequestObject();
        String secret = config.getProperty("consumerSecret");
        try {
            Init init = new Init("items", security, secret, request);
            return init.generate();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    Map<String, String> createSecurityObject(String domain) {
        var security = new HashMap();
        security.put("domain", domain);
        security.put("consumer_key", config.getProperty("consumer"));
        return security;
    }

    JSONObject createRequestObject() {
        JSONObject request = new JSONObject();
        request.put("user_id", this.user_id);
        request.put("activity_template_id", "quickstart_examples_activity_template_001");
        request.put("session_id", this.session_id);
        request.put("activity_id", "quickstart_examples_activity_001");
        request.put("rendering_type", "assess");
        request.put("type", "submit_practice");
        request.put("name", "Items API Quickstart");
        request.put("state", "initial");
        return request;
    }
}
