@echo off
REM Build script for Arabic Text Editor
REM Compiles Java source files with UTF-8 encoding

echo Compiling Arabic Text Editor...

javac -encoding UTF-8 -cp "resource/mariadb-java-client-3.4.1.jar;resource/log4j-api-2.20.0.jar;resource/log4j-core-2.20.0.jar;resource/ADAT-Lemmatization.v1.20180101.jar;resource/ADAT-Racineur.v1.20180101.jar;resource/AlKhalilDiacritizer.jar;resource/AlKhalilMorphoSys2.jar;resource/Pos_tagger.jar;src" -d bin src/Driver.java src/bll/*.java src/dal/*.java src/dto/*.java src/pl/*.java

if %ERRORLEVEL% EQU 0 (
    echo Build successful!
    echo Run with: run.bat
) else (
    echo Build failed!
    exit /b 1
)
