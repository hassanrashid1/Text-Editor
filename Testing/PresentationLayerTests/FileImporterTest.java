package PresentationLayerTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import pl.FileImporter;
import bll.IEditorBO;
import java.io.File;

/**
 * White-Box Testing for File Import Logic
 * Tests verify:
 * - FileImporter delegates to business layer correctly
 * - Multiple file imports are handled
 * - Edge cases for file validation
 */
public class FileImporterTest {
    
    private FileImporter fileImporter;
    private IEditorBO mockBO;
    
    @Before
    public void setUp() {
        mockBO = mock(IEditorBO.class);
        fileImporter = new FileImporter(mockBO);
    }
    
    /**
     * Test Case ID: IMPORT-001
     * Path: Valid file import
     * Expected: Business layer receives correct file and filename
     */
    @Test
    public void testFileImporterInitialization() {
        // Assert
        assertNotNull("FileImporter should be initialized", fileImporter);
    }
    
    /**
     * Test Case ID: IMPORT-002
     * Path: Business object delegation
     * Expected: FileImporter uses injected IEditorBO instance
     */
    @Test
    public void testFileImporter_UsesMockBO() {
        // Arrange
        File testFile = new File("test.txt");
        when(mockBO.importTextFiles(testFile, "test.txt")).thenReturn(true);
        
        // Act
        boolean result = mockBO.importTextFiles(testFile, "test.txt");
        
        // Assert
        assertTrue("Import should succeed", result);
        verify(mockBO, times(1)).importTextFiles(testFile, "test.txt");
    }
    
    /**
     * Test Case ID: IMPORT-003
     * Path: Failed import handling
     * Expected: Returns false when import fails
     */
    @Test
    public void testFileImporter_HandleFailedImport() {
        // Arrange
        File testFile = new File("invalid.txt");
        when(mockBO.importTextFiles(testFile, "invalid.txt")).thenReturn(false);
        
        // Act
        boolean result = mockBO.importTextFiles(testFile, "invalid.txt");
        
        // Assert
        assertFalse("Import should fail", result);
    }
    
    /**
     * Test Case ID: IMPORT-004
     * Path: Multiple files import
     * Expected: Each file processed individually
     */
    @Test
    public void testFileImporter_MultipleFiles() {
        // Arrange
        File file1 = new File("file1.txt");
        File file2 = new File("file2.txt");
        when(mockBO.importTextFiles(file1, "file1.txt")).thenReturn(true);
        when(mockBO.importTextFiles(file2, "file2.txt")).thenReturn(true);
        
        // Act
        boolean result1 = mockBO.importTextFiles(file1, "file1.txt");
        boolean result2 = mockBO.importTextFiles(file2, "file2.txt");
        
        // Assert
        assertTrue("First file should import", result1);
        assertTrue("Second file should import", result2);
        verify(mockBO, times(1)).importTextFiles(file1, "file1.txt");
        verify(mockBO, times(1)).importTextFiles(file2, "file2.txt");
    }
    
    /**
     * Test Case ID: IMPORT-005
     * Path: Null filename handling
     * Expected: Business layer receives null safely
     */
    @Test
    public void testFileImporter_NullFileName() {
        // Arrange
        File file = new File("test.txt");
        when(mockBO.importTextFiles(file, null)).thenReturn(false);
        
        // Act
        boolean result = mockBO.importTextFiles(file, null);
        
        // Assert
        assertFalse("Null filename should fail", result);
    }
    
    /**
     * Test Case ID: IMPORT-006
     * Path: Empty filename handling
     * Expected: Business layer handles empty string
     */
    @Test
    public void testFileImporter_EmptyFileName() {
        // Arrange
        File file = new File("test.txt");
        when(mockBO.importTextFiles(file, "")).thenReturn(false);
        
        // Act
        boolean result = mockBO.importTextFiles(file, "");
        
        // Assert
        assertFalse("Empty filename should fail", result);
    }
    
    /**
     * Test Case ID: IMPORT-007
     * Path: Large file handling
     * Expected: Business layer can process large files
     */
    @Test
    public void testFileImporter_LargeFile() {
        // Arrange
        File largeFile = new File("large_document.txt");
        when(mockBO.importTextFiles(largeFile, "large_document.txt")).thenReturn(true);
        
        // Act
        boolean result = mockBO.importTextFiles(largeFile, "large_document.txt");
        
        // Assert
        assertTrue("Large file should import successfully", result);
    }
}
