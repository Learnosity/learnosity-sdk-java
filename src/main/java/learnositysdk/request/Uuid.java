package learnositysdk.request;

/**
 * UUID utility for generating UUIDv4 identifiers.
 * Commonly used for user_id and session_id in Learnosity API requests.
 * 
 * This utility provides a consistent API across Learnosity SDKs,
 * allowing developers to use the same patterns regardless of language.
 * 
 * Example usage:
 * <pre>
 * import learnositysdk.request.Uuid;
 * 
 * String userId = Uuid.generate();
 * String sessionId = Uuid.generate();
 * </pre>
 */
public class Uuid {
    
    /**
     * Generate a UUIDv4 string.
     * 
     * This method wraps java.util.UUID.randomUUID() to provide a consistent
     * API across Learnosity SDKs.
     * 
     * @return A UUIDv4 string in the format xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx
     */
    public static String generate() {
        return java.util.UUID.randomUUID().toString();
    }
}

