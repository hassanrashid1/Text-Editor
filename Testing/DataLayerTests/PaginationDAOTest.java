package DataLayerTests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import dal.PaginationDAO;
import dto.Pages;
import java.lang.reflect.Method;
import java.util.List;

/**
 * White-Box Testing for Pagination Logic
 * Tests verify:
 * - Content splitting based on word limits (100 chars per page)
 * - Page numbering sequence
 * - Edge cases: empty content, single character, exact boundaries
 * - Boundary conditions
 * 
 * Control Flow Analysis:
 * - Node 1 (Start): Method entry
 * - Node 2: Check if fileContent is null or empty
 * - Node 3: Return empty page list
 * - Node 4: Loop through file content
 * - Node 5: Check page size / end of content
 * - Node 6: Add page and reset
 * - Node 7 (End): Return pages
 * 
 * Independent Paths:
 * P1: n1 -> n2 -> n3 -> n7 (null/empty content)
 * P2: n1 -> n2 -> n4 -> n5 -> n6 -> n4 -> n7 (normal pagination)
 * P3: n1 -> n2 -> n4 -> n5 -> n7 (content exactly at boundary)
 */
public class PaginationDAOTest {
    
    private Method paginateMethod;
    
    @Before
    public void setUp() throws Exception {
        // Use reflection to access package-private static method
        paginateMethod = PaginationDAO.class.getDeclaredMethod("paginate", String.class);
        paginateMethod.setAccessible(true);
    }
    
    /**
     * Test Case ID: PAGE-001
     * Path: P1 - Null content
     * Expected: Returns list with single empty page
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_NullContent_ReturnsEmptyPage() throws Exception {
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, (String) null);
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Should return one page", 1, result.size());
        assertEquals("Page number should be 1", 1, result.get(0).getPageNumber());
        assertEquals("Content should be empty", "", result.get(0).getPageContent());
    }
    
    /**
     * Test Case ID: PAGE-002
     * Path: P1 - Empty string content
     * Expected: Returns list with single empty page
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_EmptyString_ReturnsEmptyPage() throws Exception {
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, "");
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Should return one page", 1, result.size());
        assertEquals("Page number should be 1", 1, result.get(0).getPageNumber());
    }
    
    /**
     * Test Case ID: PAGE-003
     * Path: P2 - Content less than page size
     * Expected: Single page with all content
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_SmallContent_SinglePage() throws Exception {
        // Arrange: Content less than 100 characters
        String content = "This is a small text that fits in one page.";
        
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, content);
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Should return one page", 1, result.size());
        assertEquals("Content should match", content, result.get(0).getPageContent());
        assertEquals("Page number should be 1", 1, result.get(0).getPageNumber());
    }
    
    /**
     * Test Case ID: PAGE-004
     * Path: P2 - Content exactly at page size boundary (100 chars)
     * Expected: Single page with exact content
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_ExactBoundary_SinglePage() throws Exception {
        // Arrange: Exactly 100 characters
        String content = "a".repeat(100);
        
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, content);
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Should return one page", 1, result.size());
        assertEquals("Content length should be 100", 100, 
                    result.get(0).getPageContent().length());
    }
    
    /**
     * Test Case ID: PAGE-005
     * Path: P2 - Content requiring multiple pages
     * Expected: Multiple pages with correct pagination
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_LargeContent_MultiplePages() throws Exception {
        // Arrange: 250 characters = 3 pages (100 + 100 + 50)
        String content = "a".repeat(250);
        
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, content);
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 3 pages", 3, result.size());
        
        // Verify page numbers are sequential
        assertEquals("First page number", 1, result.get(0).getPageNumber());
        assertEquals("Second page number", 2, result.get(1).getPageNumber());
        assertEquals("Third page number", 3, result.get(2).getPageNumber());
        
        // Verify content distribution
        assertEquals("First page size", 100, result.get(0).getPageContent().length());
        assertEquals("Second page size", 100, result.get(1).getPageContent().length());
        assertEquals("Third page size", 50, result.get(2).getPageContent().length());
    }
    
    /**
     * Test Case ID: PAGE-006
     * Path: P2 - Content with exactly 2 page sizes (200 chars)
     * Expected: Exactly 2 full pages
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_TwoFullPages_CorrectSplit() throws Exception {
        // Arrange: Exactly 200 characters
        String content = "b".repeat(200);
        
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, content);
        
        // Assert
        assertEquals("Should return 2 pages", 2, result.size());
        assertEquals("First page should be full", 100, 
                    result.get(0).getPageContent().length());
        assertEquals("Second page should be full", 100, 
                    result.get(1).getPageContent().length());
    }
    
    /**
     * Test Case ID: PAGE-007
     * Path: P2 - Single character content
     * Expected: One page with single character
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_SingleCharacter_OnePage() throws Exception {
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, "A");
        
        // Assert
        assertEquals("Should return one page", 1, result.size());
        assertEquals("Content should be single char", "A", 
                    result.get(0).getPageContent());
    }
    
    /**
     * Test Case ID: PAGE-008
     * Path: P2 - Content with 101 characters
     * Expected: 2 pages (100 + 1)
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_JustOverBoundary_TwoPages() throws Exception {
        // Arrange: 101 characters
        String content = "c".repeat(101);
        
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, content);
        
        // Assert
        assertEquals("Should return 2 pages", 2, result.size());
        assertEquals("First page should be 100 chars", 100, 
                    result.get(0).getPageContent().length());
        assertEquals("Second page should be 1 char", 1, 
                    result.get(1).getPageContent().length());
    }
    
    /**
     * Test Case ID: PAGE-009
     * Path: P2 - Special characters and Unicode
     * Expected: Handles special characters correctly
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_SpecialCharacters_CorrectPagination() throws Exception {
        // Arrange: Mix of special characters
        String content = "!@#$%^&*()[]{}|\\:;\"'<>,.?/~`\n\t\r";
        
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, content);
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Should return one page", 1, result.size());
    }
    
    /**
     * Test Case ID: PAGE-010
     * Path: P2 - Arabic/Urdu text (multilingual)
     * Expected: Handles Unicode text correctly
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_UnicodeText_CorrectPagination() throws Exception {
        // Arrange: Arabic text
        String content = "السلام عليكم مرحبا بك في اختبار الترقيم";
        
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, content);
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertTrue("Should return at least one page", result.size() >= 1);
    }
    
    /**
     * Test Case ID: PAGE-011
     * Path: P2 - Very large content (stress test)
     * Expected: Handles large content efficiently
     */
    @Test(timeout = 5000)
    @SuppressWarnings("unchecked")
    public void testPaginate_LargeContent_Performance() throws Exception {
        // Arrange: 10,000 characters = 100 pages
        String content = "d".repeat(10000);
        
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, content);
        
        // Assert
        assertEquals("Should return 100 pages", 100, result.size());
        assertEquals("Last page number should be 100", 100, 
                    result.get(99).getPageNumber());
    }
    
    /**
     * Test Case ID: PAGE-012
     * Path: P2 - Mixed content with newlines and spaces
     * Expected: Preserves content structure
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testPaginate_MixedContent_PreservesStructure() throws Exception {
        // Arrange
        String content = "Line 1\nLine 2\rLine 3\r\nLine 4  with  spaces";
        
        // Act
        List<Pages> result = (List<Pages>) paginateMethod.invoke(null, content);
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertTrue("Content should be preserved", 
                  result.get(0).getPageContent().contains("Line 1"));
    }
}
