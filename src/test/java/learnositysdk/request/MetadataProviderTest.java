package learnositysdk.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MetadataProvider utility class
 */
public class MetadataProviderTest {

    @BeforeEach
    public void setUp() {
        // Any setup needed
    }

    @Test
    public void testGetSdkLanguage() {
        String language = MetadataProvider.getSdkLanguage();
        assertEquals("Java", language, "SDK language should be Java");
    }

    @Test
    public void testGetSdkVersion() {
        String version = MetadataProvider.getSdkVersion();
        assertNotNull(version, "SDK version should not be null");
        assertFalse(version.isEmpty(), "SDK version should not be empty");
    }

    @Test
    public void testGetSdkLanguageVersion() {
        String sdkVersion = MetadataProvider.getSdkLanguageVersion();
        assertTrue(sdkVersion.contains(":"), "SDK version should contain colon separator");
        assertTrue(sdkVersion.startsWith("Java:"), "SDK version should start with Java:");
    }

    @Test
    public void testDeriveActionGetRequest() {
        String action = MetadataProvider.deriveAction("https://data.learnosity.com/latest/itembank/items", "GET");
        assertEquals("get_/itembank/items", action, "GET action should be derived correctly");
    }

    @Test
    public void testDeriveActionPostRequest() {
        String action = MetadataProvider.deriveAction("https://data.learnosity.com/latest/session/scores", "POST");
        assertEquals("set_/session/scores", action, "POST action should be derived correctly");
    }

    @Test
    public void testDeriveActionPutRequest() {
        String action = MetadataProvider.deriveAction("https://data.learnosity.com/latest/itembank/activities", "PUT");
        assertEquals("set_/itembank/activities", action, "PUT action should be derived correctly");
    }

    @Test
    public void testDeriveActionDeleteRequest() {
        String action = MetadataProvider.deriveAction("https://data.learnosity.com/latest/itembank/items/123", "DELETE");
        assertEquals("delete_/itembank/items/123", action, "DELETE action should be derived correctly");
    }

    @Test
    public void testDeriveActionPatchRequest() {
        String action = MetadataProvider.deriveAction("https://data.learnosity.com/latest/session/scores", "PATCH");
        assertEquals("patch_/session/scores", action, "PATCH action should be derived correctly");
    }

    @Test
    public void testDeriveActionWithPathOnly() {
        String action = MetadataProvider.deriveAction("/itembank/items", "GET");
        assertEquals("get_/itembank/items", action, "Action should work with path only");
    }

    @Test
    public void testDeriveActionWithEmptyUrl() {
        String action = MetadataProvider.deriveAction("", "GET");
        assertEquals("", action, "Empty URL should return empty action");
    }

    @Test
    public void testDeriveActionWithNullUrl() {
        String action = MetadataProvider.deriveAction(null, "GET");
        assertEquals("", action, "Null URL should return empty action");
    }

    @Test
    public void testDeriveActionWithNullMethod() {
        String action = MetadataProvider.deriveAction("/itembank/items", null);
        assertEquals("", action, "Null method should return empty action");
    }

    @Test
    public void testDeriveActionWithUnknownMethod() {
        String action = MetadataProvider.deriveAction("/itembank/items", "UNKNOWN");
        assertEquals("get_/itembank/items", action, "Unknown method should default to GET");
    }

    @Test
    public void testExtractConsumerFromMap() {
        Map<String, String> securityPacket = new HashMap<>();
        securityPacket.put("consumer_key", "test_consumer_123");
        securityPacket.put("domain", "localhost");

        String consumer = MetadataProvider.extractConsumer(securityPacket);
        assertEquals("test_consumer_123", consumer, "Consumer should be extracted from Map");
    }

    @Test
    public void testExtractConsumerFromJSONObject() {
        JSONObject securityPacket = new JSONObject();
        securityPacket.put("consumer_key", "test_consumer_456");
        securityPacket.put("domain", "localhost");

        String consumer = MetadataProvider.extractConsumer(securityPacket);
        assertEquals("test_consumer_456", consumer, "Consumer should be extracted from JSONObject");
    }

    @Test
    public void testExtractConsumerFromJsonString() {
        String securityPacket = "{\"consumer_key\": \"test_consumer_789\", \"domain\": \"localhost\"}";

        String consumer = MetadataProvider.extractConsumer(securityPacket);
        assertEquals("test_consumer_789", consumer, "Consumer should be extracted from JSON string");
    }

    @Test
    public void testExtractConsumerNotFound() {
        Map<String, String> securityPacket = new HashMap<>();
        securityPacket.put("domain", "localhost");

        String consumer = MetadataProvider.extractConsumer(securityPacket);
        assertEquals("", consumer, "Empty string should be returned when consumer_key not found");
    }

    @Test
    public void testExtractConsumerFromNull() {
        String consumer = MetadataProvider.extractConsumer(null);
        assertEquals("", consumer, "Empty string should be returned for null input");
    }

    @Test
    public void testExtractConsumerFromInvalidJson() {
        String consumer = MetadataProvider.extractConsumer("invalid json");
        assertEquals("", consumer, "Empty string should be returned for invalid JSON");
    }
}

