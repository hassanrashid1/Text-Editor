# Arabic Text Editor - Build and Run Instructions

## Prerequisites

- Java JDK 8 or higher
- MariaDB Server 10.x+ running on localhost:3306
- Database named `realeditor` must exist

## Database Setup

1. **Install MariaDB** (if not already installed)

2. **Create the database:**
   ```sql
   CREATE DATABASE realeditor;
   USE realeditor;
   -- Run the SQL scripts in resource/Database/ directory
   ```

3. **Update credentials** in `config.properties`:
   ```properties
   db.url = jdbc:mariadb://localhost:3306/realeditor
   db.username = root
   db.password = your_password_here
   ```

## Compilation

### Windows
```batch
build.bat
```

### Manual Compilation
```bash
javac -encoding UTF-8 -cp "resource/*;src" -d bin src/**/*.java
```

**Note:** UTF-8 encoding is required due to Arabic text in source files.

## Running the Application

### Windows
```batch
run.bat
```

### Manual
```bash
java -cp "resource/*;bin" Driver
```

## Known Issues

- **Issue #2:** ~~ADAT-Stemmer.v1.20180101.jar is corrupted~~ - RESOLVED: JAR is not used by application, safely excluded from classpath.
- **Issue #3:** Application requires MariaDB server to be running on localhost:3306

## Resolved Issues

- **Issue #4:** ~~Compilation requires UTF-8 encoding flag~~ - RESOLVED: build.bat now includes -encoding UTF-8 flag

## Testing

Test files are located in the `Testing/` directory:
- `BusinessLayerTests/` - Business logic and algorithm tests
- `DataLayerTests/` - Database and persistence layer tests
- `PresentationLayerTests/` - UI layer tests

To run tests, ensure JUnit 5 and Mockito are in the classpath.
