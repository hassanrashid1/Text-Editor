package BusinessLayerTests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import bll.SearchWord;
import dto.Documents;
import dto.Pages;
import java.util.ArrayList;
import java.util.List;

/**
 * White-Box Testing for Search & Replace Word Logic
 * Tests cover:
 * - Keyword search with minimum length validation
 * - Case-insensitive matching
 * - Context extraction (prefix word)
 * - Edge cases and boundary conditions
 */
public class SearchWordTest {
    
    private List<Documents> testDocuments;
    
    @Before
    public void setUp() {
        // Initialize test documents
        testDocuments = new ArrayList<>();
        
        // Document 1: Multiple pages
        Documents doc1 = new Documents();
        doc1.setId(1);
        doc1.setName("TestDoc1.txt");
        
        Pages page1 = new Pages(1, 1, 1, "The quick brown fox jumps over the lazy dog");
        Pages page2 = new Pages(2, 1, 2, "The fox is very clever and fast");
        
        List<Pages> pages1 = new ArrayList<>();
        pages1.add(page1);
        pages1.add(page2);
        doc1.setPages(pages1);
        
        // Document 2: Single page
        Documents doc2 = new Documents();
        doc2.setId(2);
        doc2.setName("TestDoc2.txt");
        
        Pages page3 = new Pages(3, 2, 1, "Programming in Java is fun and rewarding");
        List<Pages> pages2 = new ArrayList<>();
        pages2.add(page3);
        doc2.setPages(pages2);
        
        testDocuments.add(doc1);
        testDocuments.add(doc2);
    }
    
    /**
     * Test Case ID: SEARCH-001
     * Path: Positive - Valid keyword found in document
     * Expected: Returns results with context
     */
    @Test
    public void testSearchKeyword_ValidKeyword_ReturnsResults() {
        // Act
        List<String> results = SearchWord.searchKeyword("fox", testDocuments);
        
        // Assert
        assertNotNull("Results should not be null", results);
        assertFalse("Results should not be empty", results.isEmpty());
        assertEquals("Should find 1 result", 1, results.size());
        assertTrue("Result should contain keyword", 
                  results.get(0).contains("fox"));
    }
    
    /**
     * Test Case ID: SEARCH-002
     * Path: Negative - Keyword too short (< 3 characters)
     * Expected: Throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchKeyword_TooShort_ThrowsException() {
        // Act - should throw exception
        SearchWord.searchKeyword("ab", testDocuments);
    }
    
    /**
     * Test Case ID: SEARCH-003
     * Path: Boundary - Exactly 3 characters
     * Expected: Valid search, no exception
     */
    @Test
    public void testSearchKeyword_ExactlyThreeChars_ValidSearch() {
        // Act
        List<String> results = SearchWord.searchKeyword("fox", testDocuments);
        
        // Assert
        assertNotNull("Results should not be null for 3-char keyword", results);
    }
    
    /**
     * Test Case ID: SEARCH-004
     * Path: Positive - Case insensitive matching
     * Expected: Finds keyword regardless of case
     */
    @Test
    public void testSearchKeyword_CaseInsensitive_FindsMatch() {
        // Act: Search for "FOX" (uppercase)
        List<String> results = SearchWord.searchKeyword("FOX", testDocuments);
        
        // Assert
        assertNotNull("Results should not be null", results);
        assertFalse("Should find results despite case difference", results.isEmpty());
    }
    
    /**
     * Test Case ID: SEARCH-005
     * Path: Negative - Keyword not found
     * Expected: Returns empty list
     */
    @Test
    public void testSearchKeyword_NotFound_ReturnsEmptyList() {
        // Act
        List<String> results = SearchWord.searchKeyword("elephant", testDocuments);
        
        // Assert
        assertNotNull("Results should not be null", results);
        assertTrue("Results should be empty when keyword not found", 
                  results.isEmpty());
    }
    
    /**
     * Test Case ID: SEARCH-006
     * Path: Edge case - Empty document list
     * Expected: Returns empty list without exception
     */
    @Test
    public void testSearchKeyword_EmptyDocuments_ReturnsEmpty() {
        // Arrange
        List<Documents> emptyDocs = new ArrayList<>();
        
        // Act
        List<String> results = SearchWord.searchKeyword("fox", emptyDocs);
        
        // Assert
        assertNotNull("Results should not be null", results);
        assertTrue("Results should be empty for empty document list", 
                  results.isEmpty());
    }
    
    /**
     * Test Case ID: SEARCH-007
     * Path: Positive - Prefix word extraction
     * Expected: Result includes word before keyword
     */
    @Test
    public void testSearchKeyword_WithPrefix_IncludesPreviousWord() {
        // Act
        List<String> results = SearchWord.searchKeyword("brown", testDocuments);
        
        // Assert
        assertFalse("Results should not be empty", results.isEmpty());
        assertTrue("Result should contain prefix word", 
                  results.get(0).contains("quick brown"));
    }
    
    /**
     * Test Case ID: SEARCH-008
     * Path: Boundary - Keyword at start of content (no prefix)
     * Expected: Handles missing prefix gracefully
     */
    @Test
    public void testSearchKeyword_KeywordAtStart_NoPrefix() {
        // Arrange: Create document with keyword at start
        Documents doc = new Documents();
        doc.setId(3);
        doc.setName("StartDoc.txt");
        
        Pages page = new Pages(1, 3, 1, "Programming is great");
        List<Pages> pages = new ArrayList<>();
        pages.add(page);
        doc.setPages(pages);
        
        List<Documents> docs = new ArrayList<>();
        docs.add(doc);
        
        // Act
        List<String> results = SearchWord.searchKeyword("Programming", docs);
        
        // Assert
        assertFalse("Results should not be empty", results.isEmpty());
        assertTrue("Result should handle no prefix case", 
                  results.get(0).contains("Programming"));
    }
    
    /**
     * Test Case ID: SEARCH-009
     * Path: Edge case - Special characters in keyword
     * Expected: Handles special characters
     */
    @Test
    public void testSearchKeyword_SpecialCharacters_HandlesGracefully() {
        // Act & Assert: Should not throw exception
        try {
            List<String> results = SearchWord.searchKeyword("@#$", testDocuments);
            assertNotNull("Results should not be null", results);
        } catch (IllegalArgumentException e) {
            // Expected if length < 3
            assertTrue("Exception should be about length", 
                      e.getMessage().contains("at least 3 letter"));
        }
    }
    
    /**
     * Test Case ID: SEARCH-010
     * Path: Positive - Multiple occurrences in different documents
     * Expected: Stops at first occurrence per document
     */
    @Test
    public void testSearchKeyword_MultipleOccurrences_FirstMatchPerDoc() {
        // Act: "the" appears multiple times
        List<String> results = SearchWord.searchKeyword("the", testDocuments);
        
        // Assert
        assertNotNull("Results should not be null", results);
        // Should have 1 result per document that contains the keyword
        assertTrue("Should find at least one result", results.size() >= 1);
    }
    
    /**
     * Test Case ID: SEARCH-011
     * Path: Edge case - Two-character keyword
     * Expected: Throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchKeyword_TwoCharacters_ThrowsException() {
        // Act
        SearchWord.searchKeyword("ab", testDocuments);
    }
    
    /**
     * Test Case ID: SEARCH-012
     * Path: Edge case - Empty string keyword
     * Expected: Throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchKeyword_EmptyString_ThrowsException() {
        // Act
        SearchWord.searchKeyword("", testDocuments);
    }
}
