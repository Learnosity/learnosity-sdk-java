package com.learnosity.quickstart;

import learnositysdk.request.Init;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;

public class ReportsApp extends App
{
    public String initOptions(String domain) {
        Map<String, String> security = createSecurityObject(domain);
        JSONObject request = createRequestObject();
        String secret = config.getProperty("consumerSecret");

        try {
            Init init = new Init("reports", security, secret, request);
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
        JSONObject report = new JSONObject();
        report.put("id", "session-detail");
        report.put("type", "session-detail-by-item");
        report.put("user_id", this.user_id);
        report.put("session_id", this.session_id);

        JSONArray reports = new JSONArray();
        reports.put(report);

        JSONObject request = new JSONObject();
        request.put("reports", reports);

        return request;
    }
}
