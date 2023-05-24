package com.learnosity.quickstart;

import learnositysdk.request.Init;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;


public class App
{
    private Properties config = new Properties();
    private String user_id = "$ANONYMIZED_USER_ID";
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
            Init init = new Init("reports", security, secret, request);
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

    // build a simple request object to render a report on a student session
    private Map<String, Object> createRequestObject() {
        var r = new HashMap();
        ArrayList<Object> reports = new ArrayList<Object>(); 
        r.put("reports", reports);
        var session_detail_report = new HashMap();
        reports.add(session_detail_report);
        session_detail_report.put("id", "session-detail-quickstart-report");
        session_detail_report.put("type", "session-detail-by-item");
        session_detail_report.put("user_id", this.user_id);
        session_detail_report.put("session_id", "8c393c87-77b6-4c14-8da7-75d39243e642");
    
        return r;
    }
}
