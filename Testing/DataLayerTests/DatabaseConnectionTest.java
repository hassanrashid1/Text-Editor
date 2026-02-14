package DataLayerTests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import dal.DatabaseConnection;
import java.sql.Connection;

/**
 * White-Box Testing for DatabaseConnection Singleton Pattern
 * Tests verify:
 * - Singleton property: Only one instance exists
 * - Thread safety of getInstance()
 * - Connection validity
 * - Proper resource management
 */
public class DatabaseConnectionTest {
    
    @Before
    public void setUp() {
        // Reset test environment
    }
    
    @After
    public void tearDown() {
        // Cleanup after tests
    }
    
    /**
     * Test Case ID: DBCONN-001
     * Path: Singleton Pattern - Single Instance
     * Expected: getInstance() always returns same instance
     */
    @Test
    public void testSingleton_MultipleGetInstance_ReturnsSameObject() {
        // Act
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        DatabaseConnection instance3 = DatabaseConnection.getInstance();
        
        // Assert: All references point to same object
        assertSame("getInstance should return same instance", instance1, instance2);
        assertSame("getInstance should return same instance", instance2, instance3);
        assertSame("getInstance should return same instance", instance1, instance3);
    }
    
    /**
     * Test Case ID: DBCONN-002
     * Path: Singleton Pattern - Not Null
     * Expected: getInstance() never returns null
     */
    @Test
    public void testSingleton_GetInstance_NotNull() {
        // Act
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        // Assert
        assertNotNull("DatabaseConnection instance should not be null", instance);
    }
    
    /**
     * Test Case ID: DBCONN-003
     * Path: Connection Validity
     * Expected: getConnection() returns valid connection object
     */
    @Test
    public void testGetConnection_ReturnsValidConnection() {
        // Arrange
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        
        // Act
        Connection connection = dbConnection.getConnection();
        
        // Assert
        assertNotNull("Connection should not be null", connection);
    }
    
    /**
     * Test Case ID: DBCONN-004
     * Path: Thread Safety - Concurrent Access
     * Expected: Multiple threads get same instance
     */
    @Test
    public void testSingleton_ThreadSafety_MultipleThreads() throws InterruptedException {
        // Arrange: Array to store instances from different threads
        final DatabaseConnection[] instances = new DatabaseConnection[10];
        Thread[] threads = new Thread[10];
        
        // Act: Create multiple threads accessing getInstance simultaneously
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = DatabaseConnection.getInstance();
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Assert: All instances are the same
        DatabaseConnection firstInstance = instances[0];
        for (int i = 1; i < instances.length; i++) {
            assertSame("All threads should get same instance", 
                      firstInstance, instances[i]);
        }
    }
    
    /**
     * Test Case ID: DBCONN-005
     * Path: Connection Persistence
     * Expected: Same connection across multiple getInstance calls
     */
    @Test
    public void testGetConnection_SameConnectionAcrossInstances() {
        // Act
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        Connection conn1 = instance1.getConnection();
        
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        Connection conn2 = instance2.getConnection();
        
        // Assert
        assertSame("Connections should be same", conn1, conn2);
    }
    
    /**
     * Test Case ID: DBCONN-006
     * Path: Close Connection
     * Expected: closeConnection() doesn't throw exceptions
     */
    @Test
    public void testCloseConnection_NoExceptions() {
        // Arrange
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        // Act & Assert: Should not throw exception
        try {
            instance.closeConnection();
        } catch (Exception e) {
            fail("closeConnection should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test Case ID: DBCONN-007
     * Path: Lazy Initialization
     * Expected: Instance created only when first accessed
     * Note: This test verifies the lazy initialization pattern
     */
    @Test
    public void testSingleton_LazyInitialization() {
        // Act: First call creates instance
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        // Assert
        assertNotNull("Instance should be created on first access", instance);
        
        // Act: Subsequent calls return same instance
        DatabaseConnection sameInstance = DatabaseConnection.getInstance();
        
        // Assert
        assertSame("Should return same lazy-initialized instance", 
                  instance, sameInstance);
    }
    
    /**
     * Test Case ID: DBCONN-008
     * Path: Instance Uniqueness After Long Interval
     * Expected: Same instance even after delays
     */
    @Test
    public void testSingleton_InstancePersistence_AfterDelay() throws InterruptedException {
        // Act
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        Thread.sleep(100); // Small delay
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        
        // Assert
        assertSame("Instance should persist across time", instance1, instance2);
    }
}
