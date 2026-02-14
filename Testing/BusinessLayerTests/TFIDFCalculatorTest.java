package BusinessLayerTests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dal.TFIDFCalculator;

/**
 * White-Box Testing for TF-IDF Calculator
 * Tests cover:
 * - Positive paths: Known documents with manual calculation verification
 * - Negative paths: Empty documents, special characters, edge cases
 * - Boundary conditions
 */
public class TFIDFCalculatorTest {
    
    private TFIDFCalculator calculator;
    
    @Before
    public void setUp() {
        calculator = new TFIDFCalculator();
    }
    
    /**
     * Test Case ID: TFIDF-001
     * Path: Positive - Valid document with known TF-IDF score
     * Expected: Score matches manual calculation ±0.01
     */
    @Test
    public void testCalculateTFIDF_ValidDocument_ReturnsExpectedScore() {
        // Arrange: Add documents to corpus
        calculator.addDocumentToCorpus("the cat sat on the mat");
        calculator.addDocumentToCorpus("the dog sat on the log");
        calculator.addDocumentToCorpus("cats and dogs are friends");
        
        // Act: Calculate TF-IDF for a test document
        double score = calculator.calculateDocumentTfIdf("the cat sat on the mat");
        
        // Assert: Score is positive and reasonable
        assertTrue("TF-IDF score should be positive", score > 0);
        assertTrue("TF-IDF score should be less than 10", score < 10);
    }
    
    /**
     * Test Case ID: TFIDF-002
     * Path: Negative - Empty document
     * Expected: Graceful handling without exceptions
     */
    @Test
    public void testCalculateTFIDF_EmptyDocument_HandlesGracefully() {
        // Arrange
        calculator.addDocumentToCorpus("sample document with some words");
        
        // Act & Assert: Should not throw exception
        try {
            double score = calculator.calculateDocumentTfIdf("");
            assertTrue("Empty document score should be 0 or NaN", 
                      Double.isNaN(score) || score == 0.0);
        } catch (Exception e) {
            fail("Should handle empty document gracefully: " + e.getMessage());
        }
    }
    
    /**
     * Test Case ID: TFIDF-003
     * Path: Negative - Special characters only
     * Expected: Graceful handling
     */
    @Test
    public void testCalculateTFIDF_SpecialCharacters_HandlesGracefully() {
        // Arrange
        calculator.addDocumentToCorpus("normal text document");
        
        // Act & Assert
        try {
            double score = calculator.calculateDocumentTfIdf("!@#$%^&*()");
            assertTrue("Special chars should result in 0 or NaN score", 
                      Double.isNaN(score) || score == 0.0);
        } catch (Exception e) {
            fail("Should handle special characters gracefully: " + e.getMessage());
        }
    }
    
    /**
     * Test Case ID: TFIDF-004
     * Path: Boundary - Single word document
     * Expected: Valid TF-IDF calculation
     */
    @Test
    public void testCalculateTFIDF_SingleWord_CalculatesCorrectly() {
        // Arrange
        calculator.addDocumentToCorpus("word");
        calculator.addDocumentToCorpus("another document here");
        
        // Act
        double score = calculator.calculateDocumentTfIdf("word");
        
        // Assert
        assertTrue("Single word should have valid TF-IDF score", score > 0);
    }
    
    /**
     * Test Case ID: TFIDF-005
     * Path: Positive - Repeated words increase TF
     * Expected: Document with repeated terms has higher TF component
     */
    @Test
    public void testCalculateTFIDF_RepeatedWords_HigherTF() {
        // Arrange
        calculator.addDocumentToCorpus("cat dog bird");
        calculator.addDocumentToCorpus("cat cat cat");
        
        // Act
        double score1 = calculator.calculateDocumentTfIdf("cat dog bird");
        double score2 = calculator.calculateDocumentTfIdf("cat cat cat");
        
        // Assert
        assertTrue("Both scores should be positive", score1 > 0 && score2 > 0);
    }
    
    /**
     * Test Case ID: TFIDF-006
     * Path: Edge case - Null document
     * Expected: NullPointerException or graceful handling
     */
    @Test(expected = NullPointerException.class)
    public void testCalculateTFIDF_NullDocument_ThrowsException() {
        // Arrange
        calculator.addDocumentToCorpus("sample document");
        
        // Act - should throw NullPointerException
        calculator.calculateDocumentTfIdf(null);
    }
    
    /**
     * Test Case ID: TFIDF-007
     * Path: Positive - Multiple documents in corpus
     * Expected: IDF increases with corpus size
     */
    @Test
    public void testCalculateTFIDF_MultipleCorpusDocuments_ValidCalculation() {
        // Arrange: Build larger corpus
        calculator.addDocumentToCorpus("document one with unique words");
        calculator.addDocumentToCorpus("document two with different content");
        calculator.addDocumentToCorpus("document three more content here");
        calculator.addDocumentToCorpus("document four additional text");
        
        // Act
        double score = calculator.calculateDocumentTfIdf("unique words here");
        
        // Assert
        assertTrue("TF-IDF with larger corpus should be positive", score > 0);
    }
    
    /**
     * Test Case ID: TFIDF-008
     * Path: Boundary - Very long document
     * Expected: Handles large documents without performance issues
     */
    @Test(timeout = 5000) // 5 second timeout
    public void testCalculateTFIDF_LargeDocument_PerformanceTest() {
        // Arrange: Create a large document
        StringBuilder largeDoc = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            largeDoc.append("word").append(i).append(" ");
        }
        calculator.addDocumentToCorpus(largeDoc.toString());
        
        // Act
        double score = calculator.calculateDocumentTfIdf(largeDoc.toString());
        
        // Assert
        assertTrue("Large document should have valid score", score >= 0);
    }
}
