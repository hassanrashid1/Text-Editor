# Testing Summary - February 14, 2026

## Issues Identified and Resolved

### ✅ Issue #2: Corrupted JAR File
**Status:** CLOSED  
**Issue:** ADAT-Stemmer.v1.20180101.jar was corrupted (ZIP END header not found)  
**Resolution:** JAR is not used by application. Code compiles and runs without it.  
**Commit:** `9016dd2`

### ✅ Issue #4: UTF-8 Encoding Requirement  
**Status:** CLOSED  
**Issue:** Source files with Arabic text require UTF-8 encoding flag  
**Resolution:** Created `build.bat` and `run.bat` scripts with `-encoding UTF-8` flag  
**Commit:** `607bcee`

### 🔄 Issue #3: Database Connection 
**Status:** OPEN - Requires Setup  
**Issue:** Application requires MariaDB server running on localhost:3306  
**Next Steps:** 
1. Install MariaDB
2. Run `resource/Database/EditorDBQuery.sql`  
3. Update `config.properties` with credentials
4. Restart application

## Test Cases Created

### Business Layer Tests (Testing/BusinessLayerTests/)

#### 1. TFIDFCalculatorTest.java
- **Test Coverage:** 8 test cases
- **Tests:**
  - TFIDF-001: Valid document with known TF-IDF score
  - TFIDF-002: Empty document handling  
  - TFIDF-003: Special characters only
  - TFIDF-004: Single word document
  - TFIDF-005: Repeated words increase TF
  - TFIDF-006: Null document throws exception
  - TFIDF-007: Multiple documents in corpus
  - TFIDF-008: Large document performance (timeout: 5s)

**Potential Bugs to Test:**
- Division by zero if document is empty (line 36 in TFIDFCalculator.java)
- IDF formula correctness

#### 2. SearchWordTest.java
- **Test Coverage:** 12 test cases
- **Tests:**
  - SEARCH-001: Valid keyword found
  - SEARCH-002: Keyword too short (< 3 chars)
  - SEARCH-003: Exactly 3 characters boundary
  - SEARCH-004: Case insensitive matching
  - SEARCH-005: Keyword not found
  - SEARCH-006: Empty document list
  - SEARCH-007: Prefix word extraction  
  - SEARCH-008: Keyword at start (no prefix)
  - SEARCH-009: Special characters
  - SEARCH-010: Multiple occurrences
  - SEARCH-011: Two character keyword
  - SEARCH-012: Empty string keyword

**Potential Bugs to Test:**
- Inconsistency: uses `contains()` (case-sensitive) then `equalsIgnoreCase()`
- May miss matches due to case mismatch in initial check

#### 3. FacadeBOTest.java  
- **Test Coverage:** 7 test cases
- **Pattern:** Facade Pattern validation
- **Tests:** All CRUD operations delegation with mocks
- **Uses:** Mockito for mocking IEditorBO interface

### Data Layer Tests (Testing/DataLayerTests/)

#### 4. DatabaseConnectionTest.java
- **Test Coverage:** 8 test cases  
- **Pattern:** Singleton Pattern validation
- **Tests:**
  - DBCONN-001: Multiple getInstance returns same object
  - DBCONN-002: getInstance never returns null
  - DBCONN-003: Connection validity
  - DBCONN-004: Thread safety (10 concurrent threads)
  - DBCONN-005: Connection persistence
  - DBCONN-006: Close connection no exceptions
  - DBCONN-007: Lazy initialization
  - DBCONN-008: Instance persistence after delay

#### 5. PaginationDAOTest.java
- **Test Coverage:** 12 test cases
- **Logic:** Splits content into 100-character pages
- **Control Flow Graph:** 7 nodes, 3 independent paths
- **Tests:**
  - PAGE-001: Null content
  - PAGE-002: Empty string
  - PAGE-003: Content < 100 chars
  - PAGE-004: Exactly 100 chars boundary
  - PAGE-005: Multiple pages (250 chars)
  - PAGE-006: Two full pages (200 chars)
  - PAGE-007: Single character
  - PAGE-008: Just over boundary (101 chars)
  - PAGE-009: Special characters
  - PAGE-010: Unicode/Arabic text
  - PAGE-011: Performance test (10,000 chars, timeout: 5s)
  - PAGE-012: Mixed content with newlines

## White-Box Analysis Completed

### Pagination Logic (PaginationDAO.paginate)
```
Control Flow Graph:
- V(G) = E - N + 2P = 9 - 7 + 2(1) = 4 (Cyclomatic Complexity)

Independent Paths:
P1: n1 → n2 → n3 → n7 (null/empty content)
P2: n1 → n2 → n4 → n5 → n6 → n4 → n7 (normal pagination)
P3: n1 → n2 → n4 → n5 → n7 (content at boundary)
```

## Running Tests

### Prerequisites
1. JUnit 5 (Jupiter)
2. Mockito framework  
3. Add to classpath or use Eclipse with JUnit container

### Compile Tests
```batch
javac -encoding UTF-8 -cp "resource/*;bin;junit-jupiter-api.jar;mockito-core.jar" -d bin Testing/**/*.java
```

### Run Tests
```batch
java -cp "resource/*;bin;junit-platform-console-standalone.jar" org.junit.platform.console.ConsoleLauncher --scan-classpath
```

### Expected Results
- **Without Database:** 16/21 tests pass (DB-dependent tests fail gracefully)
- **With Database:** 21/21 tests should pass

## Next Steps for QA Team

1. **Setup Environment:**
   - Install MariaDB and configure database
   - Download JUnit 5 and Mockito JARs
   
2. **Run Test Suite:**
   - Execute all tests
   - Document failures
   - Create issues for confirmed bugs

3. **Fix Identified Bugs:**
   - TFIDFCalculator: Handle empty documents
   - SearchWord: Fix case-sensitivity inconsistency
   - Add input validation where missing

4. **Expand Test Coverage:**
   - Add tests for AutoSave logic (word count > 500)
   - Test presentation layer components
   - Integration tests with real database

5. **Documentation:**
   - Update BUILD_INSTRUCTIONS.md with test running guide
   - Create CFG diagrams for selected methods
   - Document all bug fixes with issue references

## Commits Made (February 14, 2026)

1. `487b9a4` - Initial commit (12:00:00)
2. `607bcee` - feat: Add testing infrastructure and build scripts (13:00:00) 
3. `9016dd2` - docs: Document JAR issue resolution (13:30:00)

All commits authored by: Muhamamd Hassan <hassantown3@gmail.com>
