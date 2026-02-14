package BusinessLayerTests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import bll.FacadeBO;
import bll.IEditorBO;
import dto.Documents;
import java.io.File;
import java.util.List;
import static org.mockito.Mockito.*;

/**
 * White-Box Testing for Facade Pattern Implementation
 * Tests verify:
 * - Facade delegates calls to underlying BO correctly
 * - Interface-based design allows mockability
 * - All CRUD operations pass through facade
 */
public class FacadeBOTest {
    
    private FacadeBO facade;
    private IEditorBO mockEditorBO;
    
    @Before
    public void setUp() {
        // Create mock of IEditorBO
        mockEditorBO = mock(IEditorBO.class);
        facade = new FacadeBO(mockEditorBO);
    }
    
    /**
     * Test Case ID: FACADE-001
     * Path: Create file delegation
     * Expected: Facade delegates to EditorBO.createFile()
     */
    @Test
    public void testCreateFile_DelegatesToBO() {
        // Arrange
        String fileName = "test.txt";
        String content = "Test content";
        when(mockEditorBO.createFile(fileName, content)).thenReturn(true);
        
        // Act
        boolean result = facade.createFile(fileName, content);
        
        // Assert
        assertTrue("Create file should return true", result);
        verify(mockEditorBO, times(1)).createFile(fileName, content);
    }
    
    /**
     * Test Case ID: FACADE-002
     * Path: Update file delegation
     * Expected: Facade delegates to EditorBO.updateFile()
     */
    @Test
    public void testUpdateFile_DelegatesToBO() {
        // Arrange
        int id = 1;
        String fileName = "test.txt";
        int pageNumber = 1;
        String content = "Updated content";
        when(mockEditorBO.updateFile(id, fileName, pageNumber, content)).thenReturn(true);
        
        // Act
        boolean result = facade.updateFile(id, fileName, pageNumber, content);
        
        // Assert
        assertTrue("Update file should return true", result);
        verify(mockEditorBO, times(1)).updateFile(id, fileName, pageNumber, content);
    }
    
    /**
     * Test Case ID: FACADE-003
     * Path: Delete file delegation
     * Expected: Facade delegates to EditorBO.deleteFile()
     */
    @Test
    public void testDeleteFile_DelegatesToBO() {
        // Arrange
        int id = 1;
        when(mockEditorBO.deleteFile(id)).thenReturn(true);
        
        // Act
        boolean result = facade.deleteFile(id);
        
        // Assert
        assertTrue("Delete file should return true", result);
        verify(mockEditorBO, times(1)).deleteFile(id);
    }
    
    /**
     * Test Case ID: FACADE-004
     * Path: Import file delegation
     * Expected: Facade delegates to EditorBO.importTextFiles()
     */
    @Test
    public void testImportTextFiles_DelegatesToBO() {
        // Arrange
        File mockFile = mock(File.class);
        String fileName = "import.txt";
        when(mockEditorBO.importTextFiles(mockFile, fileName)).thenReturn(true);
        
        // Act
        boolean result = facade.importTextFiles(mockFile, fileName);
        
        // Assert
        assertTrue("Import should return true", result);
        verify(mockEditorBO, times(1)).importTextFiles(mockFile, fileName);
    }
    
    /**
     * Test Case ID: FACADE-005
     * Path: Get file delegation
     * Expected: Facade delegates to EditorBO.getFile()
     */
    @Test
    public void testGetFile_DelegatesToBO() {
        // Arrange
        int id = 1;
        Documents mockDoc = new Documents();
        mockDoc.setId(id);
        when(mockEditorBO.getFile(id)).thenReturn(mockDoc);
        
        // Act
        Documents result = facade.getFile(id);
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Document ID should match", id, result.getId());
        verify(mockEditorBO, times(1)).getFile(id);
    }
    
    /**
     * Test Case ID: FACADE-006
     * Path: Get all files delegation
     * Expected: Facade delegates to EditorBO.getAllFiles()
     */
    @Test
    public void testGetAllFiles_DelegatesToBO() {
        // Arrange
        List<Documents> mockDocs = mock(List.class);
        when(mockEditorBO.getAllFiles()).thenReturn(mockDocs);
        
        // Act
        List<Documents> result = facade.getAllFiles();
        
        // Assert
        assertNotNull("Result should not be null", result);
        verify(mockEditorBO, times(1)).getAllFiles();
    }
    
    /**
     * Test Case ID: FACADE-007
     * Path: Facade pattern reduces complexity
     * Expected: Single point of entry for all operations
     */
    @Test
    public void testFacadePattern_SingleEntryPoint() {
        // This test verifies that facade provides simplified interface
        // by checking that we can perform multiple operations through single object
        
        // Arrange
        when(mockEditorBO.createFile(anyString(), anyString())).thenReturn(true);
        when(mockEditorBO.updateFile(anyInt(), anyString(), anyInt(), anyString())).thenReturn(true);
        when(mockEditorBO.deleteFile(anyInt())).thenReturn(true);
        
        // Act & Assert: All operations through same facade instance
        assertTrue(facade.createFile("file1.txt", "content"));
        assertTrue(facade.updateFile(1, "file1.txt", 1, "new content"));
        assertTrue(facade.deleteFile(1));
        
        // Verify facade used underlying BO for all operations
        verify(mockEditorBO, times(1)).createFile(anyString(), anyString());
        verify(mockEditorBO, times(1)).updateFile(anyInt(), anyString(), anyInt(), anyString());
        verify(mockEditorBO, times(1)).deleteFile(anyInt());
    }
}
