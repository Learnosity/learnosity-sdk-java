package com.learnosity.quickstart;

import learnositysdk.request.DataApi;
import learnositysdk.request.Remote;
import org.json.JSONObject;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

public class DataApiApp extends App
{
    private Header[] lastRequestHeaders;
    private String lastEndpoint = "";
    private String lastAction = "";
    private int lastStatusCode = 0;

    /**
     * Demonstrates the Data API with metadata headers
     * Retrieves items from the itembank
     */
    public String getItemsData() {
        String endpoint = "https://data.learnosity.com/latest/itembank/items";
        Map<String, Object> security = createSecurityObjectWithTimestamp("");
        String secret = config.getProperty("consumerSecret");

        try {
            // Create a DataApi instance to retrieve items
            // Note: Pass the full action format (method_endpoint) for metadata headers
            DataApi dataApi = new DataApi(endpoint, security, secret, "get_/itembank/items");

            // Make the request - this will include metadata headers:
            // X-Learnosity-Consumer: <consumer_key>
            // X-Learnosity-Action: get_/itembank/items
            // X-Learnosity-SDK: Java:0.17.0
            Remote remote = dataApi.request();

            // Store request info for display
            this.lastRequestHeaders = remote.getRequestHeaders();
            this.lastEndpoint = endpoint;
            this.lastAction = "get";  // HTTP method only for Request Information section
            this.lastStatusCode = remote.getStatusCode();

            JSONObject response = dataApi.requestJSONObject();

            // Escape HTML so tags are displayed as text, not rendered
            return escapeHtml(response.toString(2));
        } catch (Exception e) {
            e.printStackTrace();
            return escapeHtml("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Demonstrates retrieving questions from the itembank
     */
    public String getQuestionsData() {
        String endpoint = "https://data.learnosity.com/latest/itembank/questions";
        Map<String, Object> security = createSecurityObjectWithTimestamp("");
        String secret = config.getProperty("consumerSecret");

        try {
            // Create a DataApi instance to retrieve questions
            DataApi dataApi = new DataApi(endpoint, security, secret, "get");

            // Make the request - metadata headers will be included automatically
            JSONObject response = dataApi.requestJSONObject();

            // Escape HTML so tags are displayed as text, not rendered
            return escapeHtml(response.toString(2));
        } catch (Exception e) {
            e.printStackTrace();
            return escapeHtml("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Demonstrates the metadata that will be sent with requests
     */
    public String getMetadataInfo() {
        JSONObject metadata = new JSONObject();

        Map<String, Object> security = createSecurityObjectWithTimestamp("");
        String consumer = (String) security.get("consumer_key");

        metadata.put("consumer", consumer);
        metadata.put("action_example_1", "get_/itembank/items");
        metadata.put("action_example_2", "get_/itembank/questions");
        metadata.put("action_example_3", "set_/itembank/activities");
        metadata.put("sdk_language", "Java");
        metadata.put("sdk_version", "0.17.0");
        metadata.put("sdk_language_version", "Java:0.17.0");
        metadata.put("note", "These metadata headers are sent with every Data API request and are invisible to customers");

        return metadata.toString(2);
    }

    /**
     * Get the last endpoint that was called
     */
    public String getLastEndpoint() {
        return this.lastEndpoint;
    }

    /**
     * Get the last action that was performed
     */
    public String getLastAction() {
        return this.lastAction;
    }

    /**
     * Get the last status code
     */
    public int getLastStatusCode() {
        return this.lastStatusCode;
    }

    /**
     * Get the last request headers as a formatted table
     */
    public String getLastRequestHeadersTable() {
        if (this.lastRequestHeaders == null || this.lastRequestHeaders.length == 0) {
            return "<tr><td colspan='2'>No request headers captured yet. Make a request first.</td></tr>";
        }

        StringBuilder html = new StringBuilder();
        for (Header header : this.lastRequestHeaders) {
            html.append("<tr>");
            html.append("<td><strong>").append(escapeHtml(header.getName())).append("</strong></td>");
            html.append("<td>").append(escapeHtml(header.getValue())).append("</td>");
            html.append("</tr>");
        }

        return html.toString();
    }

    /**
     * Get only the metadata headers
     */
    public String getMetadataHeadersTable() {
        if (this.lastRequestHeaders == null || this.lastRequestHeaders.length == 0) {
            return "<tr><td colspan='2'>No metadata headers found.</td></tr>";
        }

        StringBuilder html = new StringBuilder();
        for (Header header : this.lastRequestHeaders) {
            String name = header.getName();
            if (name.startsWith("X-Learnosity-")) {
                html.append("<tr>");
                html.append("<td><strong style='color: #e91e63;'>").append(escapeHtml(name)).append("</strong></td>");
                html.append("<td>").append(escapeHtml(header.getValue())).append("</td>");
                html.append("</tr>");
            }
        }

        return html.toString();
    }

    /**
     * Escape HTML special characters so they display as text instead of being rendered
     */
    private String escapeHtml(String text) {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

    /**
     * Create security object for Data API
     * Note: DataApi class handles signature generation internally
     */
    private Map<String, Object> createSecurityObjectWithTimestamp(String domain) {
        var security = new HashMap<String, Object>();
        security.put("consumer_key", config.getProperty("consumer"));
        security.put("domain", "localhost");

        return security;
    }

    @Override
    String initOptions(String domain) {
        // Not used for Data API
        return "";
    }

    @Override
    Map<String, String> createSecurityObject(String domain) {
        var security = new HashMap<String, String>();
        security.put("consumer_key", config.getProperty("consumer"));
        if (!domain.isEmpty()) {
            security.put("domain", domain);
        }
        return security;
    }

    @Override
    JSONObject createRequestObject() {
        // Not used for Data API
        return new JSONObject();
    }
}

