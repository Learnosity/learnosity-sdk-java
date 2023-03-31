package com.learnosity.quickstart;

import learnositysdk.request.Init;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class AuthorApp extends App
{
    public String initOptions(String domain) {
        Map<String, String> security = createSecurityObject(domain);
        JSONObject request = createRequestObject();
        String secret = config.getProperty("consumerSecret");

        try {
            Init init = new Init("author", security, secret, request);
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
        request.put("mode", "item_list");
        request.put("reference", "my-item-reference");

        JSONObject config = new JSONObject();
        JSONObject config_item_edit = new JSONObject();
        JSONObject config_item_edit_item = new JSONObject();
        JSONObject config_item_edit_item_reference = new JSONObject();
        config_item_edit_item_reference.put("show", true);
        config_item_edit_item_reference.put("edit", true);
        config_item_edit_item.put("reference", config_item_edit_item_reference);
        config_item_edit_item.put("dynamic_content", true);
        config_item_edit_item.put("shared_passage", true);
        config_item_edit_item.put("enable_audio_recording", true);
        config_item_edit.put("item", config_item_edit_item);
        config.put("item_edit", config_item_edit);
        request.put("config", config);

        JSONObject user = new JSONObject();
        user.put("id", "brianmoser");
        user.put("firstname", "Test");
        user.put("lastname", "Test");
        user.put("email", "test@test.com");
        request.put("user", user);

        return request;
    }
}
