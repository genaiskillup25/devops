@echo off
setlocal

REM ===============================
REM PATH SETUP
REM ===============================
set KAFKA_HOME=C:\Users\abhinav.akhouri\kafka
set KAFKA_BIN=%KAFKA_HOME%\bin\windows

set LOGDIR1=C:\Users\abhinav.akhouri\kafka-data\logs-1
set LOGDIR2=C:\Users\abhinav.akhouri\kafka-data\logs-2
set LOGDIR3=C:\Users\abhinav.akhouri\kafka-data\logs-3

REM ===============================
REM CLEAN OLD DATA
REM ===============================
echo ========================================
echo CLEANING OLD DATA
echo ========================================

rmdir /s /q "%LOGDIR1%" 2>nul
rmdir /s /q "%LOGDIR2%" 2>nul
rmdir /s /q "%LOGDIR3%" 2>nul

mkdir "%LOGDIR1%"
mkdir "%LOGDIR2%"
mkdir "%LOGDIR3%"

REM ===============================
REM GENERATE CLUSTER ID (SAFE)
REM ===============================
echo.
echo ========================================
echo GENERATING CLUSTER ID
echo ========================================

for /f %%i in ('powershell -Command "[guid]::NewGuid().ToString()"') do set CLUSTER_ID=%%i

echo Cluster ID: %CLUSTER_ID%

IF "%CLUSTER_ID%"=="" (
    echo ERROR: Cluster ID generation failed
    pause
    exit /b 1
)

REM ===============================
REM FIX LOG4J ISSUE
REM ===============================
set KAFKA_LOG4J_OPTS=-Dlog4j.configuration=file:%KAFKA_HOME%\config\tools-log4j.properties

REM ===============================
REM FORMAT STORAGE (IMPORTANT: use CALL)
REM ===============================
echo.
echo ========================================
echo FORMATTING STORAGE
echo ========================================

call %KAFKA_BIN%\kafka-storage.bat format -t %CLUSTER_ID% -c %KAFKA_HOME%\config\server1.properties
call %KAFKA_BIN%\kafka-storage.bat format -t %CLUSTER_ID% -c %KAFKA_HOME%\config\server2.properties
call %KAFKA_BIN%\kafka-storage.bat format -t %CLUSTER_ID% -c %KAFKA_HOME%\config\server3.properties

echo Formatting completed.

REM ===============================
REM START KAFKA SERVERS
REM ===============================
echo.
echo ========================================
echo STARTING KAFKA SERVERS
echo ========================================

start "Kafka-1" cmd /k "%KAFKA_BIN%\kafka-server-start.bat %KAFKA_HOME%\config\server1.properties"
timeout /t 5 >nul

start "Kafka-2" cmd /k "%KAFKA_BIN%\kafka-server-start.bat %KAFKA_HOME%\config\server2.properties"
timeout /t 5 >nul

start "Kafka-3" cmd /k "%KAFKA_BIN%\kafka-server-start.bat %KAFKA_HOME%\config\server3.properties"

echo.
echo Kafka cluster started successfully.
pause