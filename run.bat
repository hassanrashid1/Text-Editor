@echo off
REM Run script for Arabic Text Editor

echo Starting Arabic Text Editor...
java -cp "resource/mariadb-java-client-3.4.1.jar;resource/log4j-api-2.20.0.jar;resource/log4j-core-2.20.0.jar;resource/ADAT-Lemmatization.v1.20180101.jar;resource/ADAT-Racineur.v1.20180101.jar;resource/AlKhalilDiacritizer.jar;resource/AlKhalilMorphoSys2.jar;resource/Pos_tagger.jar;bin" Driver
