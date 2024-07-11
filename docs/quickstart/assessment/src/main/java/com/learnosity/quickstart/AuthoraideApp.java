package com.learnosity.quickstart;

import learnositysdk.request.Init;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class AuthoraideApp extends App
{
    public String initOptions(String domain) {
        Map<String, String> security = createSecurityObject(domain);
        JSONObject request = createRequestObject();
        String secret = config.getProperty("consumerSecret");

        try {
            Init init = new Init("authoraide", security, secret, request);
            return init.generate();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
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

        JSONObject user = new JSONObject();
        user.put("id", "brianmoser");
        user.put("firstname", "Test");
        user.put("lastname", "Test");
        user.put("email", "test@test.com");
        request.put("user", user);

        return request;
    }
}
