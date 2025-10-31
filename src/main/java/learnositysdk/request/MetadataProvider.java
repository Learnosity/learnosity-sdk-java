package learnositysdk.request;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * --------------------------------------------------------------------------
 * Learnosity SDK - MetadataProvider
 * --------------------------------------------------------------------------
 *
 * Utility class for generating and managing metadata for API requests.
 * Provides consumer identifier, action, SDK language, and SDK version.
 *
 */
public class MetadataProvider {

    private static final String SDK_LANGUAGE = "Java";
    private static String SDK_VERSION = null;

    static {
        // Load SDK version from project.properties
        SDK_VERSION = loadSdkVersion();
    }

    /**
     * Load SDK version from project.properties file
     * @return SDK version string, or "unknown" if not found
     */
    private static String loadSdkVersion() {
        try {
            Properties props = new Properties();
            InputStream input = MetadataProvider.class.getClassLoader()
                    .getResourceAsStream("project.properties");
            if (input != null) {
                props.load(input);
                String version = props.getProperty("version");
                input.close();
                return version != null ? version : "unknown";
            }
        } catch (IOException e) {
            // Silently fail and use default
        }
        return "unknown";
    }

    /**
     * Get the SDK language
     * @return SDK language (Java)
     */
    public static String getSdkLanguage() {
        return SDK_LANGUAGE;
    }

    /**
     * Get the SDK version
     * @return SDK version
     */
    public static String getSdkVersion() {
        return SDK_VERSION;
    }

    /**
     * Get the SDK language and version as a formatted string
     * @return Formatted string like "Java:0.17.0"
     */
    public static String getSdkLanguageVersion() {
        return SDK_LANGUAGE + ":" + SDK_VERSION;
    }

    /**
     * Derive the action from URL and HTTP method
     * Examples:
     *   GET /itembank/activities -> get_/itembank/activities
     *   POST /session/scores -> set_/session/scores
     *
     * @param url The full URL or endpoint path
     * @param method The HTTP method (GET, POST, etc.)
     * @return The derived action string
     */
    public static String deriveAction(String url, String method) {
        if (url == null || url.isEmpty() || method == null || method.isEmpty()) {
            return "";
        }

        // Extract the path from the URL
        String path = extractPath(url);

        // Determine the action prefix based on HTTP method
        String actionPrefix = getActionPrefix(method);

        return actionPrefix + path;
    }

    /**
     * Extract the path from a full URL
     * Examples:
     *   https://data.learnosity.com/latest/itembank/items -> /itembank/items
     *   /itembank/items -> /itembank/items
     *
     * @param url The full URL or path
     * @return The extracted path
     */
    private static String extractPath(String url) {
        // If it's a full URL, extract the path component
        if (url.startsWith("http://") || url.startsWith("https://")) {
            // Find the path after the domain
            int pathStart = url.indexOf('/', 8); // Skip "https://"
            if (pathStart > 0) {
                // Find the path after /latest/ or similar version prefix
                String pathPart = url.substring(pathStart);
                // Remove version prefix like /latest/
                if (pathPart.contains("/latest/")) {
                    return pathPart.substring(pathPart.indexOf("/latest/") + 7); // +7 to skip "/latest"
                }
                return pathPart;
            }
        }
        // Already a path
        return url;
    }

    /**
     * Get the action prefix based on HTTP method
     * GET -> "get_"
     * POST -> "set_"
     * PUT -> "set_"
     * DELETE -> "delete_"
     * PATCH -> "patch_"
     *
     * @param method The HTTP method
     * @return The action prefix
     */
    private static String getActionPrefix(String method) {
        if (method == null) {
            return "get_";
        }

        String upperMethod = method.toUpperCase();
        switch (upperMethod) {
            case "GET":
                return "get_";
            case "POST":
                return "set_";
            case "PUT":
                return "set_";
            case "DELETE":
                return "delete_";
            case "PATCH":
                return "patch_";
            default:
                return "get_";
        }
    }

    /**
     * Extract consumer key from security packet
     * @param securityPacket The security packet (can be Map, JSONObject, or String)
     * @return The consumer key, or empty string if not found
     */
    public static String extractConsumer(Object securityPacket) {
        if (securityPacket == null) {
            return "";
        }

        try {
            if (securityPacket instanceof java.util.Map) {
                Object consumer = ((java.util.Map) securityPacket).get("consumer_key");
                return consumer != null ? consumer.toString() : "";
            } else if (securityPacket instanceof org.json.JSONObject) {
                org.json.JSONObject json = (org.json.JSONObject) securityPacket;
                if (json.has("consumer_key")) {
                    return json.getString("consumer_key");
                }
            } else if (securityPacket instanceof String) {
                org.json.JSONObject json = new org.json.JSONObject((String) securityPacket);
                if (json.has("consumer_key")) {
                    return json.getString("consumer_key");
                }
            }
        } catch (Exception e) {
            // Silently fail and return empty string
        }

        return "";
    }
}

