package learnositysdk.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Unit tests for the Uuid utility class.
 */
public class UuidTest {

    // UUIDv4 format: xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx
    // where y is one of [8, 9, a, b]
    private static final Pattern UUID_V4_PATTERN = Pattern.compile(
        "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
        Pattern.CASE_INSENSITIVE
    );

    @Test
    public void testGenerateReturnsString() {
        System.out.println("Uuid: Generate returns String");
        
        String uuid = Uuid.generate();
        
        assertNotNull(uuid, "Generated UUID should not be null");
        assertTrue(uuid instanceof String, "Generated UUID should be a String");
    }

    @Test
    public void testGenerateReturnsValidUuidV4Format() {
        System.out.println("Uuid: Generate returns valid UUIDv4 format");
        
        String uuid = Uuid.generate();
        
        assertEquals(36, uuid.length(), "UUID should be 36 characters long");
        assertTrue(UUID_V4_PATTERN.matcher(uuid).matches(), 
            "UUID should match UUIDv4 format: " + uuid);
    }

    @Test
    public void testGenerateReturnsUniqueValues() {
        System.out.println("Uuid: Generate returns unique values");
        
        String uuid1 = Uuid.generate();
        String uuid2 = Uuid.generate();
        
        assertNotEquals(uuid1, uuid2, "Two generated UUIDs should be different");
    }

    @Test
    public void testGenerateMultipleUniqueValues() {
        System.out.println("Uuid: Generate 1000 unique UUIDs");
        
        Set<String> uuids = new HashSet<>();
        int count = 1000;
        
        for (int i = 0; i < count; i++) {
            uuids.add(Uuid.generate());
        }
        
        assertEquals(count, uuids.size(), 
            "All " + count + " generated UUIDs should be unique");
    }

    @Test
    public void testGenerateConsistentFormat() {
        System.out.println("Uuid: Generate consistent format across multiple calls");
        
        for (int i = 0; i < 100; i++) {
            String uuid = Uuid.generate();
            assertTrue(UUID_V4_PATTERN.matcher(uuid).matches(), 
                "UUID should consistently match UUIDv4 format: " + uuid);
        }
    }

    @Test
    public void testGenerateContainsHyphens() {
        System.out.println("Uuid: Generate contains hyphens in correct positions");
        
        String uuid = Uuid.generate();
        
        assertEquals('-', uuid.charAt(8), "UUID should have hyphen at position 8");
        assertEquals('-', uuid.charAt(13), "UUID should have hyphen at position 13");
        assertEquals('-', uuid.charAt(18), "UUID should have hyphen at position 18");
        assertEquals('-', uuid.charAt(23), "UUID should have hyphen at position 23");
    }

    @Test
    public void testGenerateVersion4Indicator() {
        System.out.println("Uuid: Generate has version 4 indicator");
        
        String uuid = Uuid.generate();
        
        // The 15th character (index 14) should be '4' for UUIDv4
        assertEquals('4', uuid.charAt(14), 
            "UUID should have '4' at position 14 (version indicator)");
    }

    @Test
    public void testGenerateVariantIndicator() {
        System.out.println("Uuid: Generate has correct variant indicator");
        
        String uuid = Uuid.generate();
        
        // The 20th character (index 19) should be 8, 9, a, or b
        char variantChar = uuid.charAt(19);
        assertTrue(variantChar == '8' || variantChar == '9' || 
                   variantChar == 'a' || variantChar == 'b' ||
                   variantChar == 'A' || variantChar == 'B',
            "UUID should have variant indicator (8, 9, a, or b) at position 19, got: " + variantChar);
    }
}

